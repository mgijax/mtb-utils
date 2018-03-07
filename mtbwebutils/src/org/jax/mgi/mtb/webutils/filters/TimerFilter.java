/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbwebutils/src/org/jax/mgi/mtb/webutils/filters/TimerFilter.java,v 1.1 2008/07/17 17:03:28 sbn Exp $
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
import org.apache.log4j.Logger;

/**
 * Simple filter to display how long a request took to process.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/07/17 17:03:28 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbwebutils/src/org/jax/mgi/mtb/webutils/filters/TimerFilter.java,v 1.1 2008/07/17 17:03:28 sbn Exp $
 * @see javax.servlet.Filter
 */
public class TimerFilter implements Filter {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    private FilterConfig filterConfig = null;
    private static Logger log = Logger.getLogger(TimerFilter.class.getName());

    // ----------------------------------------------------------- Constructors
    // none

    // --------------------------------------------------------- Public Methods

    /**
     * Initialize the filter.
     *
     * @param config A FilterConfig</code object
     * @throws javax.servlet.ServletException
     */
    public void init(FilterConfig config)
        throws ServletException {
        this.filterConfig = config;
    }

    /**
     * Destroy the filter.
     */
    public void destroy() {
        this.filterConfig = null;
    }

    /**
     * Perform the required action on the request.  This method simply times
     * how long the request takes.
     *
     * @param request The request object
     * @param response The response object
     * @param chain The FilterChain object which is a series of
     *        filters
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
        throws IOException, ServletException {

        final long lBefore = System.currentTimeMillis();
        chain.doFilter(request, response);
        final long lAfter = System.currentTimeMillis();

        StringBuffer sbRequestURI = new StringBuffer();

        if (request instanceof HttpServletRequest) {
            sbRequestURI.append(((HttpServletRequest)request).getRequestURI());

            Enumeration e = request.getParameterNames();
            boolean bFirst = true;

            if (e != null) {

                while (e.hasMoreElements()) {
                    if (bFirst) {
                        sbRequestURI.append('?');
                        bFirst = false;
                    } else {
                        sbRequestURI.append('&');
                    }

                    String strName = (String)e.nextElement();

                    sbRequestURI.append(strName);
                    sbRequestURI.append('=');
                    sbRequestURI.append(request.getParameter(strName));
                }
            }

            StringBuffer sbLog = new StringBuffer();
            sbLog.append("ADDR:");
            sbLog.append(request.getRemoteAddr());
            sbLog.append(" URI:");
            sbLog.append(sbRequestURI);
            sbLog.append(" TIME:");
            sbLog.append((lAfter - lBefore));
            sbLog.append("msec");
            if((lAfter - lBefore) > 10000){
              log.error("******************SLOW RESPONSE*******************");
              log.error(sbLog.toString());
            }else{
            // simply log the time from start to finish
            log.info(sbLog.toString());
            }
        }
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
}
