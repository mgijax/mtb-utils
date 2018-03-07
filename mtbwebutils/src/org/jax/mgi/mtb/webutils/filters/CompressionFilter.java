/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbwebutils/src/org/jax/mgi/mtb/webutils/filters/CompressionFilter.java,v 1.1 2008/07/17 17:03:28 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.webutils.filters;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jax.mgi.mtb.webutils.filters.compression.CompressionResponseWrapper;

/**
 * Implementation of javax.servlet.Filter used to compress
 * the ServletResponse if it is bigger than a threshold.
 */

public class CompressionFilter implements Filter {

    /**
     * The filter configuration object we are associated with.  If this value
     * is null, this filter instance is not currently configured.
     */
    private FilterConfig filterConfig = null;

    /**
     * Minimal reasonable threshold
     */
    private final int nMinThreshold = 128;


    /**
     * The threshold number to compress
     */
    protected int nCompressionThreshold;

    /**
     * Debug level for this filter
     */
    private int nDebug = 0;

    /**
     * Place this filter into service.
     *
     * @param filterConfig The filter configuration object
     */

    public void init(FilterConfig filterConfig) {

        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            String strValue = filterConfig.getInitParameter("debug");
            if (strValue!=null) {
                nDebug = Integer.parseInt(strValue);
            } else {
                nDebug = 0;
            }
            String strTemp = filterConfig.getInitParameter("compressionThreshold");
            if (strTemp!=null) {
                nCompressionThreshold = Integer.parseInt(strTemp);
                if (nCompressionThreshold != 0 && nCompressionThreshold < nMinThreshold) {
                    if (nDebug > 0) {
                        System.out.println("compressionThreshold should be either 0 - no compression or >= " + nMinThreshold);
                        System.out.println("compressionThreshold set to " + nMinThreshold);
                    }
                    nCompressionThreshold = nMinThreshold;
                }
            } else {
                nCompressionThreshold = 0;
            }

        } else {
            nCompressionThreshold = 0;
        }
    }

    /**
    * Take this filter out of service.
    */
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * The doFilter method of the Filter is called by the container
     * each time a request/response pair is passed through the chain due
     * to a client request for a resource at the end of the chain.
     * The FilterChain passed into this method allows the Filter to pass on the
     * request and response to the next entity in the chain.<p>
     * This method first examines the request to check whether the client support
     * compression. <br>
     * It simply just pass the request and response if there is no support for
     * compression.<br>
     * If the compression support is available, it creates a
     * CompressionServletResponseWrapper object which compresses the content and
     * modifies the header if the content length is big enough.
     * It then invokes the next entity in the chain using the FilterChain object
     * (chain.doFilter()), <br>
     **/

    public void doFilter (ServletRequest request, ServletResponse response,
                         FilterChain chain ) throws IOException, ServletException {

        if (nDebug > 0) {
            System.out.println("@doFilter");
        }

        if (nCompressionThreshold == 0) {
            if (nDebug > 0) {
                System.out.println("doFilter gets called, but compressionTreshold is set to 0 - no compression");
            }
            chain.doFilter(request, response);
            return;
        }

        boolean bSupportCompression = false;
        if (request instanceof HttpServletRequest) {
            if (nDebug > 1) {
                System.out.println("requestURI = " + ((HttpServletRequest)request).getRequestURI());
            }

            // Are we allowed to compress ?
            String strGZip = (String) ((HttpServletRequest)request).getParameter("gzip");
            if ("false".equals(strGZip)) {
                if (nDebug > 0) {
                    System.out.println("got parameter gzip=false --> don't compress, just chain filter");
                }
                chain.doFilter(request, response);
                return;
            }

            Enumeration e =
                ((HttpServletRequest)request).getHeaders("Accept-Encoding");
            while (e.hasMoreElements()) {
                String strName = (String)e.nextElement();
                if (strName.indexOf("gzip") != -1) {
                    if (nDebug > 0) {
                        System.out.println("supports compression");
                    }
                    bSupportCompression = true;
                } else {
                    if (nDebug > 0) {
                        System.out.println("no support for compresion");
                    }
                }
            }
        }

        if (!bSupportCompression) {
            if (nDebug > 0) {
                System.out.println("doFilter gets called wo compression");
            }
            chain.doFilter(request, response);
            return;
        } else {
            if (response instanceof HttpServletResponse) {
                CompressionResponseWrapper wrappedResponse =
                    new CompressionResponseWrapper((HttpServletResponse)response);
                wrappedResponse.setDebugLevel(nDebug);
                wrappedResponse.setCompressionThreshold(nCompressionThreshold);
                if (nDebug > 0) {
                    System.out.println("doFilter gets called with compression");
                }
                try {
                    chain.doFilter(request, wrappedResponse);
                } finally {
                    wrappedResponse.finishResponse();
                }
                return;
            }
        }
    }

    /**
     * Set filter config
     * This function is equivalent to init. Required by Weblogic 6.1
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        init(filterConfig);
    }

    /**
     * Return filter config
     * Required by Weblogic 6.1
     */
    public FilterConfig getFilterConfig() {
        return this.filterConfig;
    }

}

