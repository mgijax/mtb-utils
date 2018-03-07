package us.jawsoft.web.CacheFilter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import org.apache.log4j.Logger;

/**
 * CacheFilter is a Java Servlet filter that caches server output.
 *
 * <p>The cache is either stored in memory or on disk depending on the
 * parameters specified in a properties file.  The properties file is specified
 * as an <code>init-parameter</code> in the <code>&lt;filter&gt;</code> section
 * of the <code>web.xml</code> file.</p>
 *
 * @version 1.0
 * @author mjv@jawsoft.us
 */
public class CacheFilter implements Filter {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    /**
     * The logger for this class.
     */
    private final static Logger log = 
            Logger.getLogger(CacheFilter.class.getName());

    /**
     * The servlet context.
     */
    private ServletContext servletContext;

    /**
     * The <code>CacheManager</code> is responseible for managing the cache.
     */
    private CacheManager mgr = null;

    /**
     * CacheFilter scope - default is PageContext.APPLICATION_SCOPE, but can
     * have any one of the following values: (PageContext.SESSION_SCOPE,
     * PageContext.APPLICATION_SCOPE, PageContext.REQUEST_SCOPE,
     * PageContext.PAGE_SCOPE)
     */
    private int cacheScope = PageContext.APPLICATION_SCOPE;

    /**
     * Time before cache should be refreshed - default one hour (in seconds)
     */
    private int cacheTime = 60 * 60;

    private String antiPattern = null;


    // ----------------------------------------------------------- Constructors
    // none


    // --------------------------------------------------------- Public Methods

    /**
     * Filter clean up.
     */
    public void destroy() {
    }

    /**
     * The doFilter method is called before the actually request.  The URI
     * is used as a key for the cache.  If the key is already in the cache,
     * the content is attempted to be retrieved.  Once the content is retrieved
     * it is sent back as the response. If the content cannot be retrieved or
     * the key is not in the cache, the response of the invoked URL is cached
     * by wrapping the <code>HttpServletResponse</code> object so that the
     * output stream can be caught. The output stream is essentially split into
     * two streams with the {@link CacheServletOutputStream} class. One stream
     * works as normal while the other is stored as a {@link CacheContent}
     * object.
     *
     * @param request The servlet request
     * @param response The servlet response
     * @param chain The filter chain
     *
     * @throws ServletException IOException
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
        throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        boolean cachedEntry = false;

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        StringBuffer uri = constructURI(httpRequest);

        if (log.isDebugEnabled()) {
            log.debug("Start of request!");
            log.debug("Request received for " + uri.toString());
        }

        if (antiPattern != null) {
            if (uri.toString().indexOf(antiPattern) > 0) {
                if (log.isDebugEnabled()) {
                    log.debug("Antipattern matched, so caching is bypassed");
                }
                chain.doFilter(request, response);
                return;
            }
        }

        String key = mgr.generateEntryKey(null, httpRequest, cacheScope);
        Cache cache = mgr.getCache(httpRequest, cacheScope);

        try {
            if (log.isDebugEnabled()) {
                log.debug("Attempting to retrieve entry from cache...");
                log.debug("Using KEY: " + key);
            }

            CacheContent respContent =
                (CacheContent)cache.getFromCache(key, cacheTime);

            if (log.isDebugEnabled()) {
                log.debug("Entry found in cache!");
                log.debug("Utilizing cached entry's content.");
            }

            cachedEntry = true;

            respContent.writeTo(response);
        } catch (NeedsRefreshException nre) {
            boolean updateSucceeded = false;

            try {
                if (log.isDebugEnabled()) {
                    log.debug("Entry NOT found in cache!");
                    log.debug("Creating new entry in the cache.");
                }

                CacheHttpServletResponseWrapper cacheResponse =
                    new CacheHttpServletResponseWrapper(
                        (HttpServletResponse)response);

                chain.doFilter(request, cacheResponse);
                cacheResponse.flushBuffer();

                // Only cache if the response was 200
                if (cacheResponse.getStatus() == HttpServletResponse.SC_OK) {
                    //Store as the cache content the result of the response
                    cache.putInCache(key,
                                     cacheResponse.getContent(),
                                     uri.toString());
                    updateSucceeded = true;
                }
            } finally {
                if (!updateSucceeded) {
                    cache.cancelUpdate(key);
                }
            }
        } catch (Exception e){
          log.debug(e);
          chain.doFilter(request, response);
          return;
        }

        long finishTime = System.currentTimeMillis();

        if (log.isDebugEnabled()) {
            StringBuffer sb = new StringBuffer();
            sb.append("[CacheFilter][");
            sb.append(httpRequest.getRemoteAddr());
            sb.append("][");
            sb.append(uri);
            sb.append("][");
            sb.append((finishTime - startTime));
            sb.append(" msec]");

            if (cachedEntry) {
                sb.append("[CACHE]");
            } else {
                sb.append("[NEW]");
            }

            log.debug(sb.toString());
            log.debug("End of request!");
        }
    }

    /**
     * Initialize the filter. This retrieves a {@link CacheManager} and
     * configures the filter based on any initialization parameters.<p>
     * The supported initialization parameters are:
     * <ul>
     * <li><b>propertiesFile</b> - the path to the properties file</li>
     * <li><b>scope</b> - the default scope to cache content in. Acceptable
     * values are <code>application</code> (default), <code>session</code>,
     * <code>request</code> and
     * <code>page</code>.
     * </ul>
     *
     * @param filterConfig The filter configuration
     */
    public void init(FilterConfig filterConfig) {
        // Get whatever settings we want...
        this.servletContext = filterConfig.getServletContext();

        // attempt to use the properties file which overrides any parameters
        // in the filter-init section
        String file = null;

        try {
            file = filterConfig.getInitParameter("propertiesFile");

            if ((file != null) && (file.length() != 0)) {
                // get the CacheManager instance
                mgr = CacheManager.getInstance(servletContext, file);
            }
        } catch (Exception e) {
            log.error("Unable to initialize Cache!", e);
        }
        
        // if there is no file, use the filter initialization parameters
        if (mgr == null) {
            Properties properties = new Properties();
            
            // memory or disk caching
            String value = 
                filterConfig.getInitParameter(CacheManager.CACHE_MEMORY_KEY);
            
            if ((value != null) && (value.length() != 0)) {
                properties.setProperty(CacheManager.CACHE_MEMORY_KEY, value);
            }
            
            // clear cache on startup
            value = 
                filterConfig.getInitParameter(CacheManager.CACHE_CLEAR_KEY);
                
            if ((value != null) && (value.length() != 0)) {
                properties.setProperty(CacheManager.CACHE_CLEAR_KEY, value);
            }
            
            // cache path
            value = 
                filterConfig.getInitParameter(CacheManager.CACHE_PATH_KEY);
                
            if ((value != null) && (value.length() != 0)) {
                properties.setProperty(CacheManager.CACHE_PATH_KEY, value);
            }
                
            // cache refresh period
            value = 
                filterConfig.getInitParameter(CacheManager.CACHE_REFRESH_KEY);
                
            if ((value != null) && (value.length() != 0)) {
                properties.setProperty(CacheManager.CACHE_REFRESH_KEY, value);
            }

            // cache anti pattern
            value = 
                filterConfig.getInitParameter(CacheManager.CACHE_DONOT_CACHE_KEY);
            
            if ((value != null) && (value.length() != 0)) {
                properties.setProperty(CacheManager.CACHE_DONOT_CACHE_KEY, value);
                this.antiPattern = value;
            }
                
            // get the CacheManager instance
            mgr = CacheManager.getInstance(servletContext, properties);
            
        }

        cacheTime = mgr.getCacheRefreshTime();

        try {
            String scopeString = filterConfig.getInitParameter("scope");

            if ("session".equals(scopeString)) {
                cacheScope = PageContext.SESSION_SCOPE;
            } else if ("application".equals(scopeString)) {
                cacheScope = PageContext.APPLICATION_SCOPE;
            } else if ("request".equals(scopeString)) {
                cacheScope = PageContext.REQUEST_SCOPE;
            } else if ("page".equals(scopeString)) {
                cacheScope = PageContext.PAGE_SCOPE;
            }
        } catch (Exception e) {
            // do nothing
        }
    }


    // ------------------------------------------------------ Protected Methods
    // none


    // -------------------------------------------------------- Private Methods

    /**
     * Construct the URI that invoked the filter.
     *
     * @param request An HttpServletRequest
     *
     * @return The URI as a StringBuffer
     */
    private StringBuffer constructURI(HttpServletRequest request) {
        Map paramMap = request.getParameterMap();
        Set paramSet = new TreeMap(paramMap).entrySet();

        StringBuffer buf = new StringBuffer(request.getRequestURI());

        boolean first = true;

        for (Iterator it = paramSet.iterator(); it.hasNext();) {
            Entry entry = (Entry) it.next();
            String[] values = (String[]) entry.getValue();

            for (int i = 0; i < values.length; i++) {
                String key = (String) entry.getKey();

                if ((key.length() != 10) || !"jsessionid".equals(key)) {
                    if (first) {
                        first = false;
                        buf.append('?');
                    } else {
                        buf.append('&');
                    }

                    buf.append(key).append('=').append(values[i]);
                }
            }
        }

        return buf;
    }
}
