package us.jawsoft.web.CacheFilter.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Collections;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import us.jawsoft.web.CacheFilter.Cache;
import us.jawsoft.web.CacheFilter.CacheItem;
import us.jawsoft.web.CacheFilter.CacheManager;

/**
 */
public class CacheFilterAdmin extends HttpServlet {

    // -------------------------------------------------------------- Constants

    /**
     * Output content type
     */
    private static final String CONTENT_TYPE = "text/html";

    /**
     * Used for URL Encoding
     */
    private static final String ENCODING = "UTF-8";

    // the following are used for sorting purposes

    /**
     * Sort by key.
     */
    private static final int KEY = 0;

    /**
     * Sort by id.
     */
    private static final int ID = 1;

    /**
     * Sort by creation time.
     */
    private static final int CREATED = 2;

    /**
     * Sort by last update time.
     */
    private static final int UPDATED = 3;

    /**
     * Sort by size.
     */
    private static final int SIZE = 4;



    // ----------------------------------------------------- Instance Variables
    // none

    // ----------------------------------------------------------- Constructors
    // none

    // --------------------------------------------------------- Public Methods

    /**
     * Initialize global variables
     *
     * @param config The ServletConfig object
     *
     * @throws A ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * Clean up resources
     */
    public void destroy() {
    }

    /**
     * Process the HTTP Post request.
     *
     * @param req The HTTP request
     * @param resp The servlet response
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        doGet(req, resp);
    }

    /**
     * Process the HTTP Get request.
     *
     * @param request The HTTP request
     * @param response The servlet response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        // get the CacheManager instance
        CacheManager mgr = CacheManager.getInstance(getServletContext());
        Cache cache = mgr.getCache(request, PageContext.APPLICATION_SCOPE);

        // Generate the output
        response.setContentType(CONTENT_TYPE);

        PrintWriter out = response.getWriter();

        generateHeader(out);

        out.println("<hr>");

        generateForm(out);

        out.println("<hr>");

        // parameters
        out.println("<b>Parameters</b><br><br>");
        out.print("<table width=\"100%\" style=\"font-size:8pt;");
        out.print("font-family:verdana;vertical-align:middle;");
        out.println("background-color:black\">");
        out.println("<tr>");
        out.println("<td bgcolor=\"#DDDDDD\">");
        out.println("<pre>");
        out.print("Type of Caching: ");

        if(mgr.useMemoryCaching()) {
            out.println("Memory");
        } else {
            out.println("Disk");

            out.println("Cache Path: " + mgr.getCachePath());

            out.print("Clear Cache on Startup: ");

            if (mgr.getClearCacheOnStartup()) {
                out.println("True");
            } else {
                out.println("False");
            }
        }

        out.print("Cache Refresh Time: " + mgr.getCacheRefreshTime());
        out.println(" seconds</pre>");
        out.println("</td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("<hr>");

        // show output
        String paramValue = request.getParameter("flushEntireCache");

        if ((paramValue != null) && (paramValue.length() > 0)) {
            cache.flushAll();
            out.println("<br><font color=red>Cache Cleared</font><br>");
        }

        paramValue = request.getParameter("flushCacheEntry");

        if ((paramValue != null) && (paramValue.length() > 0)) {
            cache.flushEntry(paramValue);
            out.println("<br><font color=red>Cache Entry [ " + paramValue +
                        " ] Cleared</font><br>");
        }

        // show output
        paramValue = request.getParameter("sort");

        int sortOrder = 0;

        if ((paramValue != null) && (paramValue.length() > 0)) {
            try {
                sortOrder = Integer.parseInt(paramValue);
            } catch (Exception e) {
                sortOrder = 0;
            }
        }

        generateCacheList(cache, sortOrder, out);

        out.println("<hr>");

        out.println("</body></html>");
    }


    // ----------------------------------------------------- Protected  Methods
    // none


    // -------------------------------------------------------- Private Methods

    /**
     * Generate the HTML header.
     *
     * @param out The PrintWriter
     */
    private void generateHeader(PrintWriter out) {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Cache Filter Administration</title>");
        out.println("<style>");
        out.println("<!--");
        out.println("body {font-family:Tahoma}");
        out.println(".cache {font-family:Lucida Console,Courier New,Arial;");
        out.println("        font-size:8pt;}");
        out.println(".cacheH {font-family:Lucida Console,Courier New,Arial;");
        out.println("         font-size:8pt;background-color:#EEEEEE;}");
        out.println(".header {font-family:Lucida Console,Courier New,Arial;");
        out.println("         font-size:8pt;background-color:#EEEEEE;");
        out.println("         color:#336699;text-align:center;}");
        out.println("A:link {color:#336699;text-decoration:underline;}");
        out.println("A:visited {color:#336699;text-decoration:none;}");
        out.println("A:active {color:#336699;text-decoration:none;}");
        out.println("A:hover {color:red;text-decoration:none;}");
        out.println("//-->");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<center><h2>Cache Filter Administration</h2></center>");

    }

    /**
     * Generate the HTML form.
     *
     * @param out The PrintWriter
     */
    private void generateForm(PrintWriter out) {
        out.print("<a href=\"CacheFilterAdmin\">");
        out.println("Cache Filter Administration Home</a>");
        out.println("&nbsp;&nbsp;&nbsp;");
        out.print("<a href=\"CacheFilterAdmin?flushEntireCache=1\">");
        out.println("Flush Entire Cache</a>");
        //out.println("<form name=\"theForm\" method=\"post\" ");
        //out.println("      action=\"CacheFilterAdmin\">");
        //out.println("<input type=\"submit\">");
        //out.println("<input type=\"reset\">");
        //out.println("</form>");
    }

    /**
     * Generate the HTML table of the cached entries.
     *
     * @param cache The cache
     * @param out The PrintWriter
     */
    private void generateCacheList(Cache cache, int sortOrder, PrintWriter out) {
        out.println("<b>Cache Contents</b><br><br><center>");

        out.println("<table border=1 width=\"100%\">");
        out.println("<tr>");
        out.println("    <td class=\"header\"><a href=\"CacheFilterAdmin?sort=0\">KEY</a></td>");
        out.println("    <td class=\"header\"><a href=\"CacheFilterAdmin?sort=1\">ID</a></td>");
        out.println("    <td class=\"header\"><a href=\"CacheFilterAdmin?sort=2\">CREATED</a></td>");
        out.println("    <td class=\"header\"><a href=\"CacheFilterAdmin?sort=3\">UPDATED</a></td>");
        out.println("    <td class=\"header\"><a href=\"CacheFilterAdmin?sort=4\">SIZE</a></td>");
        out.println("    <td class=\"header\">FLUSH</center></td>");
        out.println("</tr>");

        List<CacheItem> items = sort(cache.getCacheItems(), sortOrder);
        int rowNum = 0;
        String tdClass;
        long totalSize = 0;

        for (CacheItem i : items) {

            rowNum++;

            if ((rowNum % 2) == 0) {
                tdClass = "class=\"cache\"";
            } else {
                tdClass = "class=\"cacheH\"";
            }

            out.println("<tr>");
            out.println("    <td " + tdClass + ">" + i.getKey() + "</td>");
            out.println("    <td " + tdClass + ">" + i.getId() + "</td>");

            SimpleDateFormat sdf =
                new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");

            String s = sdf.format(new Date(i.getCreateTime()));

            out.println("    <td " + tdClass + ">" + s + "</td>");

            long l = i.getLastUpdateTime();

            if (l == 0) {
                out.println("    <td " + tdClass + ">&nbsp;</td>");
            } else {
                s = sdf.format(new Date(l));
                out.println("    <td " + tdClass + ">" + s + "</td>");
            }

            long size = i.getSize();
            totalSize += size;

            out.println("    <td " + tdClass + ">" + size + "</td>");

            String encodedKey = null;

            try {
                encodedKey = URLEncoder.encode(i.getKey(), ENCODING);
            } catch (UnsupportedEncodingException uee) {
                // do nothing
            }

            out.print("    <td " + tdClass + ">");
            out.print("<a href=\"CacheFilterAdmin?flushCacheEntry=");
            out.println(encodedKey + "\">Flush</a></td>");
            out.println("</tr>");

        }

        out.println("</table>");
        out.println("</center>");
        out.println("<font class=\"cache\">NOTE: SIZE is in bytes.</font>");

        out.println("<br><hr><b>Cache Details</b><br><br>");
        out.print("<table width=\"100%\" style=\"font-size:8pt;");
        out.print("font-family:verdana;vertical-align:middle;");
        out.println("background-color:black\">");
        out.println("<tr>");
        out.println("<td bgcolor=\"#DDDDDD\">");
        out.println("<pre>");
        out.println("Number of Items: " + rowNum + "</font>");
        out.print("Total Size: " + totalSize + " bytes ");

        float f = (float)totalSize / 1000;
        DecimalFormat df = new DecimalFormat("0.00");

        out.println("(" + df.format(f) + " kb)");

        out.print("Average Item Size: ");

        if (rowNum > 0) {
            f = (float)totalSize / rowNum;
        } else {
            f = 0;
        }

        out.print(df.format(f) + " bytes ");

        f /= 1000;

        out.print("(" + df.format(f) + " kb)");

        out.println("</pre>");
        out.println("</td>");
        out.println("</tr>");
        out.println("</table>");
    }

    /**
     * Sort the enumeration
     *
     * @param items
     * @param type The sort type
     *
     * @return The ArrayList of sorted <code>CacheItem</code>s
     */

    private List sort(List<CacheItem> items, int type) {
        List<CacheItem> a = new ArrayList<CacheItem>(items);

        if (type == KEY) {
            Collections.sort(a, new KeyComparator());
        } else if (type == ID) {
            Collections.sort(a, new IdComparator());
        } else if (type == CREATED) {
            Collections.sort(a, new CreateTimeComparator());
        } else if (type == UPDATED) {
            Collections.sort(a, new UpdateTimeComparator());
        } else if (type == SIZE) {
            Collections.sort(a, new SizeComparator());
        }

        return a;
    }

    // the following are inner classes that implement <code>Comparator<code>
    // for sorting purposes

    class CreateTimeComparator implements Comparator {
        public int compare(Object item, Object anotherItem) {
            Date time1 = new Date(((CacheItem) item).getCreateTime());
            Date time2 = new Date(((CacheItem) anotherItem).getCreateTime());

            return time1.compareTo(time2);
        }
    }

    class UpdateTimeComparator implements Comparator {
        public int compare(Object item, Object anotherItem) {
            Date time1 = new Date(((CacheItem) item).getLastUpdateTime());
            Date time2 = new Date(((CacheItem) anotherItem).getLastUpdateTime());

            return time1.compareTo(time2);
        }
    }

    class IdComparator implements Comparator {
        public int compare(Object item, Object anotherItem) {
            String id1 = ((CacheItem) item).getId().toUpperCase();
            String id2 = ((CacheItem) anotherItem).getId().toUpperCase();

            return id1.compareTo(id2);
        }
    }

    class KeyComparator implements Comparator {
        public int compare(Object item, Object anotherItem) {
            String key1 = ((CacheItem) item).getKey().toUpperCase();
            String key2 = ((CacheItem) anotherItem).getKey().toUpperCase();

            return key1.compareTo(key2);
        }
    }

    class SizeComparator implements Comparator {
        public int compare(Object item, Object anotherItem) {
            long size1 = ((CacheItem) item).getSize();
            long size2 = ((CacheItem) anotherItem).getSize();

            if (size1 < size2) {
               return -1;
            } else if (size1 == size2) {
                return 0;
            } else if (size1 > size2) {
                return 1;
            }

            return 0;
        }
    }
}
