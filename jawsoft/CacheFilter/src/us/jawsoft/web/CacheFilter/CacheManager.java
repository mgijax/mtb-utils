package us.jawsoft.web.CacheFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

/**
 * A CacheManager creates, flushes and manages the cache.
 * <p>
 * This is stored in a static instance, per web app context.
 *
 * @version 1.0
 */
public class CacheManager
    implements Serializable {

    // -------------------------------------------------------------- Constants

    /**
     * Name of the properties file.
     */
    public final static String PROPERTIES_FILENAME = "CacheFilter.properties";

    /**
     *  Set property cache.use.host.domain.in.key=true to add domain
     *  information to key generation for hosting multiple sites.
     */
    public final static String CACHE_USE_HOST_DOMAIN_KEY
                                              = "cache.use.host.domain.in.key";

    /**
     * Used for disk caching properties.
     */
    public final static String CACHE_PATH_KEY = "cache.path";

    /**
     * Servlet context's temp directory attribute.
     */
    public final static String TMPDIR = "javax.servlet.context.tempdir";

    /**
     * Disk caching default path.
     */
    public final static String CACHE_PATH_DEFAULT = "CacheFilter";

    /**
     * The cache key that is used to store the cache in context.
     */
    public final static String CACHE_KEY_KEY = "cache.key";

    /**
     * Default value for the cache key used to store the cache in context.
     */
    public final static String CACHE_KEY_DEFAULT = "_CacheFilter";

    /**
     * The cache key that is used to store the cache in context.
     */
    public final static String CACHE_REFRESH_KEY = "cache.refresh";

    /**
     * The cache key that is used to store the cache in context.
     */
    public final static String CACHE_DONOT_CACHE_KEY = "cache.do.not.cache";
    
    /**
     * Constant for session scope's name.
     */
    public final static String SESSION_SCOPE_NAME = "session";

    /**
     * Constant for application scope's name.
     */
    public final static String APPLICATION_SCOPE_NAME = "application";

    /**
     * Key used to store the current scope in the configuration.
     */
    public final static String HASH_KEY_SCOPE = "scope";

    /**
     * The key for the CacheManager stored in the ServletContext.
     */
    public final static String CACHE_ADMINISTRATOR_KEY = "_CacheFilter_Admin";

    /**
     * Key used to store the current session ID in the configuration.
     */
    public final static String HASH_KEY_SESSION_ID = "sessionId";

    /**
     * Key used to store the servlet container temporary directory
     * in the configuration.
     */
    public final static String CONTEXT_TMPDIR = "context.tempdir";

    /**
     * The string to use as a file separator.
     */
    public final static String FILE_SEPARATOR = "/";

    /**
     * The character to use as a file separator.
     */
    public final static char FILE_SEPARATOR_CHAR = FILE_SEPARATOR.charAt(0);

    /**
     * Constant for Key generation.
     */
    public final static short AVERAGE_KEY_LENGTH = 30;

    /**
     * Usable caracters for key generation
     */
    public static final String m_strBase64Chars =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    /**
     * Used to determine between memory caching and disk caching.
     */
    public final static String CACHE_MEMORY_KEY = "cache.memory";

    /**
     * Used for cache clearing on startup.
     */
    public final static String CACHE_CLEAR_KEY = "cache.clear.on.startup";

    /**
     * Used for cache clearing on startup, default value is TRUE.
     */
    public final static boolean CACHE_CLEAR_DEFAULT = true;

    // ----------------------------------------------------- Instance Variables

    /**
     * The logger for this class.
     */
    private final static org.apache.logging.log4j.Logger log =
            org.apache.logging.log4j.LogManager.getLogger(CacheManager.class.getName());

    /**
     * Whether or not to use the hos name in the key.
     */
    private boolean useHostDomainInKey = false;

    /**
     * Whether or not to use memory caching.
     */
    private boolean useMemoryCaching = true;

    /**
     * Path to the cache.
     */
    private String cachePath = null;

    /**
     * Whether or not to clear the cache on startup.
     */
    private boolean cacheClearOnStartup = CACHE_CLEAR_DEFAULT;

    /**
     * Map containing the flush times of different scopes
     */
    private Map<Integer, Date> flushTimes;

    /**
     * The ServletContext
     */
    private transient ServletContext context;

    /**
     * Key to use for storing and retrieving Object in contexts.
     */
    private String cacheKey;

    /**
     * Specifies the time the item should remain in the cache before a refresh.
     */
    private int cacheRefreshTime = 60 * 60;  // 1 hr default

    /**
     * Contains all thr properties for the cache.
     */
    protected Properties properties = null;


    // ----------------------------------------------------------- Constructors

    /**
     * Create the cache manager.
     *
     * This will reset all the flush times and load the properties file.
     *
     * @param context The ServletContext
     * @param fileName The name of the properties file to load
     */
    private CacheManager(ServletContext context, String fileName) {
        log.info("CacheManager instantiated");
        this.context = context;
        loadProps(fileName);
        initCacheParameters();
        flushTimes = new Hashtable<Integer, Date>();
        initHostDomainInKey();
    }

    /**
     * Create the cache manager.
     *
     * This will reset all the flush times and reset the properties.
     *
     * @param context The ServletContext
     * @param properties The properties
     */
    private CacheManager(ServletContext context, Properties properties) {
        log.info("CacheManager instantiated");
        this.context = context;
        this.properties = properties;
        initCacheParameters();
        flushTimes = new Hashtable<Integer, Date>();
        initHostDomainInKey();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Obtain an instance of the CacheManager
     *
     * @param context The ServletContext that this CacheManager is a Singleton 
     * under
     * @return Returns the CacheManager instance for this context
     */
    public static CacheManager getInstance(ServletContext context) {
        Properties nullProperties = null;
        
        return getInstance(context, nullProperties);
    }

    /**
     * Obtain an instance of the CacheManager
     *
     * @param context The ServletContext that this CacheManager is a Singleton
     * under.
     * @param file The name of the properties file.
     *
     * @return Returns the CacheManager instance for this context
     */
    public static CacheManager getInstance(ServletContext context, 
                                           String file) {
        CacheManager mgr = 
            (CacheManager)context.getAttribute(CACHE_ADMINISTRATOR_KEY);

        // First time we need to create the manager and store it in the
        // servlet context
        if (mgr == null) {
            mgr = new CacheManager(context, file);
            context.setAttribute(CACHE_ADMINISTRATOR_KEY, mgr);
            mgr.getAppScopeCache(context);
        }

        return mgr;
    }

    /**
     * Obtain an instance of the CacheManager
     *
     * @param context The ServletContext that this CacheManager is a Singleton
     * under.
     * @param properties A <code>Properties</code> object
     *
     * @return Returns the CacheManager instance for this context
     */
    public static CacheManager getInstance(ServletContext context, 
                                           Properties properties) {
        CacheManager mgr = 
            (CacheManager)context.getAttribute(CACHE_ADMINISTRATOR_KEY);

        // First time we need to create the manager and store it in the
        // servlet context
        if (mgr == null) {
            mgr = new CacheManager(context, properties);
            context.setAttribute(CACHE_ADMINISTRATOR_KEY, mgr);
            mgr.getAppScopeCache(context);
        }

        return mgr;
    }

    /**
     * Shuts down the cache manager. This should usually only be called
     * when the controlling application shuts down.
     *
     * @param context The ServletContext in which the manager should be
     * destroyed
     */
    public static void destroyInstance(ServletContext context) {
        CacheManager mgr =
            (CacheManager)context.getAttribute(CACHE_ADMINISTRATOR_KEY);

        if (mgr != null) {
            // Finalize the application scope cache
            Cache cache = (Cache) context.getAttribute(mgr.getCacheKey());

            if (cache != null) {
                context.removeAttribute(mgr.getCacheKey());
                context.removeAttribute(CACHE_ADMINISTRATOR_KEY);
                cache = null;
            }

            mgr = null;
        }
    }

    /**
     * Grabs the cache for the specified scope
     *
     * @param request The current request
     * @param scope The scope of this cache 
     * (<code>PageContext.APPLICATION_SCOPE</code> or 
     * <code>PageContext.SESSION_SCOPE</code>)
     *
     * @return The cache
     */
    public Cache getCache(HttpServletRequest request, int scope) {
        if (scope == PageContext.APPLICATION_SCOPE) {
            return 
                getAppScopeCache(request.getSession(true).getServletContext());
        }

        if (scope == PageContext.SESSION_SCOPE) {
            return getSessionScopeCache(request.getSession(true));
        }

        throw new RuntimeException("The supplied scope value of " + scope +
                                   " is invalid. Acceptable values are " +
                                   "PageContext.APPLICATION_SCOPE and " +
                                   "PageContext.SESSION_SCOPE");
    }

    /**
     * A convenience method to retrieve the application scope cache
     *
     * @param context the current <code>ServletContext</code>
     *
     * @return the application scope cache. If none is present, one will
     * be created.
     */
    public Cache getAppScopeCache(ServletContext context) {
        Cache cache = null;
        Object obj = context.getAttribute(getCacheKey());

        if ((obj == null) || !(obj instanceof Cache)) {
            cache = createCache(PageContext.APPLICATION_SCOPE, null);
            context.setAttribute(getCacheKey(), cache);
        } else {
            cache = (Cache)obj;
        }

        return cache;
    }

    /**
     * A convenience method to retrieve the session scope cache
     *
     * @param session the current <code>HttpSession</code>
     * @return the session scope cache for this session. If none is present,
     * one will be created.
     */
    public Cache getSessionScopeCache(HttpSession session) {
        Cache cache = null;
        Object obj = session.getAttribute(getCacheKey());

        if ((obj == null) || !(obj instanceof Cache)) {
            cache = createCache(PageContext.SESSION_SCOPE, session.getId());
            session.setAttribute(getCacheKey(), cache);
        } else {
            cache = (Cache) obj;
        }

        return cache;
    }

    /**
     * Get the cache key from the properties. Set it to a default value if it
     * is not present in the properties
     *
     * @return The cache.key property or the CACHE_KEY_DEFAULT
     */
    public String getCacheKey() {
        if (cacheKey == null) {
            cacheKey = getProperty(CACHE_KEY_KEY);

            if (cacheKey == null) {
                cacheKey = CACHE_KEY_DEFAULT;
            }
        }

        return cacheKey;
    }

    /**
     * Set the flush time for a specific scope to a specific time
     *
     * @param date  The time to flush the scope
     * @param scope The scope to be flushed
     */
    public void setFlushTime(Date date, int scope) {
        synchronized (flushTimes) {
            if (date != null) {
                flushTimes.put(Integer.valueOf(scope), date);
            } else {
                throw new IllegalArgumentException("setFlushTime called " +
                                                   "with a null date.");
            }
        }
    }

    /**
     * Set the flush time for a specific scope to the current time.
     *
     * @param scope The scope to be flushed
     */
    public void setFlushTime(int scope) {
        setFlushTime(new Date(), scope);
    }

    /**
     * Get the flush time for a particular scope.
     *
     * @param scope The scope to get the flush time for.
     *
     * @return A date representing the time this scope was last flushed.
     * Returns null if it has never been flushed.
     */
    public Date getFlushTime(int scope) {
        synchronized (flushTimes) {
            return (Date) flushTimes.get(Integer.valueOf(scope));
        }
    }

    /**
     * Retrieve an item from the cache
     *
     * @param scope The cache scope
     * @param request The servlet request
     * @param key The key of the object to retrieve
     * @param refreshPeriod The time interval specifying if an entry needs 
     * refresh
     *
     * @return The requested object
     *
     * @throws NeedsRefreshException
     */
    public Object getFromCache(int scope, HttpServletRequest request,
                               String key, int refreshPeriod)
        throws NeedsRefreshException {

        Cache cache = getCache(request, scope);
        key = this.generateEntryKey(key, request, scope);
        return cache.getFromCache(key, refreshPeriod);
    }

    /**
     * Checks if the given scope was flushed more recently than the CacheItem 
     * provided. Used to determine whether to refresh the particular CacheItem.
     *
     * @param cacheEntry The cache entry which we're seeing whether to refresh
     * @param scope The scope we're checking
     *
     * @return Whether or not the scope has been flushed more recently than 
     * this cache entry was updated.
     */
    public boolean isScopeFlushed(CacheItem cacheEntry, int scope) {
        Date flushDateTime = getFlushTime(scope);

        if (flushDateTime == null) {
            return false;
        } else {
            long lastUpdate = cacheEntry.getLastUpdateTime();
            return (flushDateTime.getTime() >= lastUpdate);
        }
    }

    /**
     * Cancels a pending cache update. This should only be called by a thread
     * that received a {@link NeedsRefreshException} and was unable to generate
     * some new cache content.
     *
     * @param scope The cache scope
     * @param request The <code>HttpServletRequest</code> request
     * @param key The cache entry key to cancel the update of.
     */
    public void cancelUpdate(int scope, 
                             HttpServletRequest request, 
                             String key) {
        Cache cache = getCache(request, scope);
        key = this.generateEntryKey(key, request, scope);
        cache.cancelUpdate(key);
    }

    /**
     * Flush all scopes at a particular time
     *
     * @param date The time to flush the scope
     */
    public void flushAll(Date date) {
        synchronized (flushTimes) {
            setFlushTime(date, PageContext.APPLICATION_SCOPE);
            setFlushTime(date, PageContext.SESSION_SCOPE);
            setFlushTime(date, PageContext.REQUEST_SCOPE);
            setFlushTime(date, PageContext.PAGE_SCOPE);
        }

    }

    /**
     * Flush all scopes instantly.
     */
    public void flushAll() {
        flushAll(new Date());
    }

    /**
     * Generates a cache entry key.
     *
     * If the string key is not specified, the HTTP request URI and QueryString
     * is used. Operating systems that have a filename limitation less than 255
     * or have filenames that are case insensitive may have issues with key
     * generation where two distinct pages map to the same key.
     * <p>
     * POST Requests (which have no distinguishing query string) may also
     * generate identical keys for what is actually different pages.
     *
     * @param key The key entered by the user
     * @param request The current request
     * @param scope The scope this cache entry is under
     *
     * @return The generated cache key
     */
    public String generateEntryKey(String key,
                                   HttpServletRequest request,
                                   int scope) {
        return generateEntryKey(key, request, scope, null, null);
    }

    /**
     * Generates a cache entry key.
     *
     * If the string key is not specified, the HTTP request URI and QueryString
     * is used.  Operating systems that have a filename limitation less than
     * 255 or have filenames that are case insensitive may have issues with key
     * generation where two distinct pages map to the same key.
     * <p>
     * POST Requests (which have no distinguishing query string) may also
     * generate identical keys for what is actually different pages.
     *
     * @param key The key entered by the user
     * @param request The current request
     * @param scope The scope this cache entry is under
     * @param language The ISO-639 language code to distinguish different pages
     * in application scope
     *
     * @return The generated cache key
     */
    public String generateEntryKey(String key, HttpServletRequest request,
                                   int scope, String language) {
        return generateEntryKey(key, request, scope, language, null);
    }

    /**
     * Generates a cache entry key.
     * <p>
     * If the string key is not specified, the HTTP request URI and QueryString
     * is used. Operating systems that have a filename limitation less than 255
     * or have filenames that are case insensitive may have issues with key
     * generation where two distinct pages map to the same key.
     * <p>
     * POST Requests (which have no distinguishing query string) may also
     * generate identical keys for what is actually different pages.
     *
     * @param key The key entered by the user
     * @param request The current request
     * @param scope The scope this cache entry is under
     * @param language The ISO-639 language code to distinguish different pages
     * in application scope
     * @param suffix The ability to put a suffix at the end of the key
     * @return The generated cache key
     */
    public String generateEntryKey(String key, HttpServletRequest request,
                                   int scope, String language, String suffix) {
        /**
         * Used for generating cache entry keys.
         */
        StringBuffer cBuffer = new StringBuffer(AVERAGE_KEY_LENGTH);

        // Append the language if available
        if (language != null) {
            cBuffer.append(FILE_SEPARATOR).append(language);
        }

        // Servers for multiple host domains need this distinction in the key
        if (useHostDomainInKey) {
            cBuffer.append(FILE_SEPARATOR).append(request.getServerName());
        }

        if (key != null) {
            cBuffer.append(FILE_SEPARATOR).append(key);
        } else {
            String generatedKey = request.getRequestURI();

            if (generatedKey.charAt(0) != FILE_SEPARATOR_CHAR) {
                cBuffer.append(FILE_SEPARATOR_CHAR);
            }

            cBuffer.append(generatedKey);
            cBuffer.append('_').append(request.getMethod()).append('_');

            generatedKey = getSortedQueryString(request);

            if (generatedKey != null) {
                try {
                    MessageDigest digest = MessageDigest.getInstance("MD5");
                    byte[] b = digest.digest(generatedKey.getBytes());
                    cBuffer.append('_');

                    // Base64 encoding allows for unwanted slash characters.
                    cBuffer.append(toBase64(b).replace('/', '_'));
                } catch (Exception e) {
                    // Ignore query string
                }
            }
        }

        // Do we want a suffix
        if ((suffix != null) && (suffix.length() > 0)) {
            cBuffer.append(suffix);
        }

        return cBuffer.toString();
    }

    /**
     * Log error messages to commons logging.
     *
     * @param message   Message to log.
     */
    public void logError(String message) {
        //log.error(message);
    }

    /**
     * Put an object in the cache
     *
     * @param scope The cache scope
     * @param request The servlet request
     * @param key The object key
     * @param content The object to add
     */
    public void putInCache(int scope, HttpServletRequest request,
                           String key, Object content) {
        Cache cache = getCache(request, scope);
        key = this.generateEntryKey(key, request, scope);
        cache.putInCache(key, content);
    }

    /**
     * Determine what type of caching is being used.
     *
     * @return <code>true</code> if memory caching is being used,
     * <code>false</code> otherwise.
     */
    public boolean useMemoryCaching() {
        return useMemoryCaching;
    }

    /**
     * Retrieve the path to the cache.
     *
     * @return The path to the cache or <code>null</code> if memory caching
     * is being used.
     */
    public String getCachePath() {
        return cachePath;
    }

    /**
     * Flush the entire cache.
     */
    public void emptyCache() {
        flushAll();
    }

    /**
     * Determine if the cache will be cleared on application startup.
     *
     * @return <code>true</code> if the cached will be cleared,
     * <code>false</code> otherwise.
     */
    public boolean getClearCacheOnStartup() {
        return this.cacheClearOnStartup;
    }

    /**
     * Get the refresh time of an item in the cache.
     *
     * @return An integer representing the refresh time
     */
    public int getCacheRefreshTime() {
        return this.cacheRefreshTime;
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Retrieves the value of one of the properties.
     *
     * @param key The key assigned to the property
     * @return Property value, or <code>null</code> if the property could not
     * be found.
     */
    protected String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Creates a string that contains all of the request parameters and their
     * values in a single string. This is very similar to
     * <code>HttpServletRequest.getQueryString()</code> except the parameters
     * are sorted by name, and if there is a <code>jsessionid</code> parameter
     * it is filtered out.<p>
     * If the request has no parameters, this method returns <code>null</code>.
     */
    protected String getSortedQueryString(HttpServletRequest request) {
        Map paramMap = request.getParameterMap();

        if (paramMap.isEmpty()) {
            return null;
        }

        Set paramSet = new TreeMap(paramMap).entrySet();

        StringBuffer buf = new StringBuffer();

        boolean first = true;

        for (Iterator it = paramSet.iterator(); it.hasNext();) {
            Entry entry = (Entry) it.next();
            String[] values = (String[]) entry.getValue();

            for (int i = 0; i < values.length; i++) {
                String key = (String) entry.getKey();

                if ((key.length() != 10) || !"jsessionid".equals(key)) {
                    if (first) {
                        first = false;
                    } else {
                        buf.append('&');
                    }

                    buf.append(key).append('=').append(values[i]);
                }
            }
        }

        // We get a 0 length buffer if the only parameter was a jsessionid
        if (buf.length() == 0) {
            return null;
        } else {
            return buf.toString();
        }
    }


    // -------------------------------------------------------- Private Methods

    /**
     * Convert a byte array into a Base64 string (as used in mime formats).
     *
     * @param aValue AN array of <code>byte</code>s
     *
     * @return The Base64 string
     */
    private static String toBase64(byte[] aValue) {
        int byte1;
        int byte2;
        int byte3;
        int iByteLen = aValue.length;
        StringBuffer tt = new StringBuffer();

        for (int i = 0; i < iByteLen; i += 3) {
            boolean bByte2 = (i + 1) < iByteLen;
            boolean bByte3 = (i + 2) < iByteLen;
            byte1 = aValue[i] & 0xFF;
            byte2 = (bByte2) ? (aValue[i + 1] & 0xFF) : 0;
            byte3 = (bByte3) ? (aValue[i + 2] & 0xFF) : 0;

            tt.append(m_strBase64Chars.charAt(byte1 / 4));
            tt.append(m_strBase64Chars.charAt((byte2 / 16) +
                                              ((byte1 & 0x3) * 16)));
            tt.append(((bByte2) ?
                m_strBase64Chars.charAt((byte3 / 64) + ((byte2 & 0xF) * 4))
                :
                '='));

            tt.append(((bByte3) ?
                m_strBase64Chars.charAt(byte3 & 0x3F)
                :
                '='));
        }

        return tt.toString();
    }

    /**
     * Create a cache
     *
     * @param scope The cache scope
     * @param sessionId The sessionId for with the cache will be created
     *
     * @return A new cache
     */
    private ServletCache createCache(int scope, String sessionId) {


        ServletCache newCache = new ServletCache(this, scope);

        properties.setProperty(HASH_KEY_SCOPE, "" + scope);

        if (sessionId != null) {
            properties.setProperty(HASH_KEY_SESSION_ID, sessionId);
        }

        return newCache;
    }

    /**
     * Set property cache.use.host.domain.in.key=true to add domain information
     * to key generation for hosting multiple sites
     */
    private void initHostDomainInKey() {
        String propStr = getProperty(CACHE_USE_HOST_DOMAIN_KEY);

        useHostDomainInKey = "true".equalsIgnoreCase(propStr);
    }

    /**
     * Initialize the core cache parameters from the configuration properties.
     */
    private void initCacheParameters() {

        // cache memory
        String value = getProperty(CACHE_MEMORY_KEY);

        if ((value != null) && (value.equalsIgnoreCase("false"))) {
            useMemoryCaching = false;
            log.info("CACHE - USING DISK CACHING");
        } else {
            useMemoryCaching = true;
            log.info("CACHE - USING IN MEMORY CACHING");
        }

        // cache path
        value = getProperty(CACHE_PATH_KEY);

        if ((value != null) && (value.length() != 0)) {
            cachePath = value;
        } else {
            cachePath = ((File)context.getAttribute(TMPDIR)).getAbsolutePath();
        }

        log.info("CACHE - PATH = " + cachePath);

        // cache clear on startup
        value = getProperty(CACHE_CLEAR_KEY);

        if ((value != null) && (value.equalsIgnoreCase("false"))) {
            cacheClearOnStartup = false;
        } else {
            cacheClearOnStartup = true;
            deleteCachedFiles();
        }

        log.info("CACHE - WILL " +
                           (cacheClearOnStartup ? "" : "NOT ") +
                           "BE CLEARED CACHE ON STARTUP");

        // cache refresh time
        value = getProperty(CACHE_REFRESH_KEY);

        if (value != null) {
            try {
                cacheRefreshTime = Integer.parseInt(value);
            } catch (Exception e) {
                // do nothing
            }
        }

        log.info("CACHE - REFRESH TIME = " + cacheRefreshTime);
    }

    /**
     * Load the properties file (<code>CacheFilter.properties</code>)
     * from the classpath. If the file cannot be found or loaded, an error
     * will be logged and no properties will be set.
     *
     * @param file The name of the properties file.
     */
    private void loadProps(String file) {
        log.info("LOADING PROPERTIES (" + file + ")");

        // if we cannot load the properties file, use the defaults
        properties = new Properties();

        // attempt to load from file
        if ((file != null) && (file.length() != 0)) {
            log.info("ATTEMPTING TO LOAD PROPERTIES FROM FILE " + file);
            properties = loadPropertiesFromFile(file);
        }

        // attempt to load from file in the class path
        if (properties == null) {
            log.info("ATTEMPTING TO LOAD PROPERTIES FROM STREAM " + 
                  PROPERTIES_FILENAME);

            properties = loadPropertiesFromStream(PROPERTIES_FILENAME);
        }

    }

    /**
     * Load properties from a file.
     *
     * @param fileName The name of the properties file.
     */
    private Properties loadPropertiesFromFile(String fileName) {
        // Read properties file
        Properties props = new Properties();

        try {
            props.load(new FileInputStream(fileName));
        } catch (IOException ioe) {
            log.fatal("Error Loading Properties from file!", ioe);
            return null;
        }

        return props;
    }

    /**
     * Load properties from a file located in the classpath.
     *
     * @param fileName The name of the properties file.
     */
    private Properties loadPropertiesFromStream(String fileName) {
        Properties props = new Properties();
        InputStream in = null;

        try {
            in = getClass().getResourceAsStream(fileName);
            props.load(in);
        } catch (Exception e) {
            log.error("Error Loading Properties from stream!", e);
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                // Ignore errors that occur while closing file
            }
        }

        return props;
    }

    /**
     * Delete all cached files.
     */
    private void deleteCachedFiles() {
        log.info("Clearing cache...");
        if (!useMemoryCaching()) {
            String path = getCachePath();

            log.info("DELETING FILES AT: " + path);

            if (path != null) {
                boolean success = deleteFiles(new File(path));

                if (success) {
                    log.info("CACHE CLEARED");
                }
            }
        }
        log.info("Cache cleared!");
    }

    /**
     * Delete all files in a directory that end in </code>.cache</code.
     *
     * @param dir The <code>File</code> handle to the directory
     *
     * @return <code>true if all files were successfully deleted,
     * <code>false</code> otherwise
     */
    private boolean deleteFiles(File dir) {
        File f = null;

        try {

            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    if (children[i].endsWith(".cache")) {
                        f = new File(dir.getCanonicalPath() + 
                                     File.separator + 
                                     children[i]);

                        //log.info("ATTEMPTING TO DELETE: " +
                                //f.getCanonicalPath());

                        if (f.delete()) {
                            //log.info(" DELETED!");
                        } else {
                            //log.info(" CANNOT DELETE!");
                        }
                    }
                }
            }

        } catch (IOException ioe) {
            log.error("Unable to delete files!", ioe);
            return false;
        }

        // The directory is now empty
        return true;
    }
}
