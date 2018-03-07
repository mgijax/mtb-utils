/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbwebutils/src/org/jax/mgi/mtb/webutils/filters/AuthenticationFilter.java,v 1.1 2008/07/17 17:03:28 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.webutils.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jax.mgi.mtb.webutils.filters.authentication.AuthenticationBean;

public class AuthenticationFilter implements Filter  {
    
    // -------------------------------------------------------------- Constants
    
    public static final String ATTRIBUTE = "attribute";
    public static final String DEBUG = "debug";
    public static final String COOKIE = "cookie";
    public static final String SCOPE = "scope";
    public static final String REDIRECT = "redirect";
    public static final String EXCEPT = "except";
    public static final String SESSION_SCOPE = "session";
    public static final String REQUEST_SCOPE = "request";
    public static final String APPLICATION_SCOPE = "application";
    public static final String ACCESSFILTER = "mtbwebutilsaccessfilter";
    
    // ----------------------------------------------------- Instance Variables
    
    private FilterConfig filterConfig;
    private static boolean bDebug = false;
    
    // ----------------------------------------------------------- Constructors
    // none
    
    // --------------------------------------------------------- Public Methods
    
    /**
     * Initialize the filter.
     * 
     * @param config A FilterConfig</code object
     * @throws javax.servlet.ServletException 
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        
        ServletContext servletContext = filterConfig.getServletContext();
        String strRedirect = filterConfig.getInitParameter(REDIRECT);
        String strAttribute = filterConfig.getInitParameter(ATTRIBUTE);
        String strCookie = filterConfig.getInitParameter(COOKIE);
        String strExcept = filterConfig.getInitParameter(EXCEPT);
        String strScope = filterConfig.getInitParameter(SCOPE);
        String strDebug = filterConfig.getInitParameter(DEBUG);
        
        if(strRedirect == null) {
            out("Could not get an initial parameter redirect");
            return;
        }
        
        if(strAttribute == null && strCookie == null) {
            out("Could not get an initial parameter attribute or cookie");
            return;
        }
        
        AuthenticationBean authBean = new AuthenticationBean();
        authBean.setRedirect(strRedirect);
        
        if(strAttribute != null) {
            authBean.setAttribute(strAttribute);
        }
        if(strCookie != null) {
            authBean.setCookie(strCookie);
        }
        if(strExcept != null) {
            authBean.setExcept(strExcept);
        }
        if(strScope != null) {
            authBean.setScope(strScope);
        }
        if(strDebug != null) {
            if (strDebug.equalsIgnoreCase("true") || 
                strDebug.equalsIgnoreCase("on") ||
                strDebug.equalsIgnoreCase("1")) {
                bDebug = true;
            } else {
                bDebug = false;
            }
        }
        
        
        log("REDIRECT="+strRedirect);
        log("ATTRIBUTE="+strAttribute);
        log("COOKIE="+strCookie);
        log("EXCEPT="+strExcept);
        log("SCOPE="+strScope);
        log("DEBUG="+strDebug);
        log("Context set with key" + ACCESSFILTER);
        
        servletContext.setAttribute(ACCESSFILTER, authBean);
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
    public void doFilter(ServletRequest request, 
                         ServletResponse response, 
                         FilterChain chain)
    throws IOException, ServletException {
        log("Filter called...");
        
        String strRequestURI = ((HttpServletRequest)request).getRequestURI();
        ServletContext servletContext = filterConfig.getServletContext();
        AuthenticationBean authBean = (AuthenticationBean)servletContext.getAttribute(ACCESSFILTER);
        
        log("REQUEST URI = [" + strRequestURI + "]");
        
        if((authBean == null) || (request.getAttribute("AUTHENTICATED_USER") != null)) {
            log("authbean is null or request.getAttribute(AUTHENTICATED_USER) is NOT null");
            log("*** DOING FILTER ***");
            chain.doFilter(request, response);
        } else {
            log("authbean is NOT null AND request.getAttribute(ACCESSFILTER) is null");
            log("checking excepturi");
            if(authBean.exceptURI(strRequestURI) || (authBean.getRedirect() == null)) {
                log("*** DOING FILTER ***");
                chain.doFilter(request, response);
            } else {
                log("SETTING REQUEST AUTHENTICATED_USER = yes");

                request.setAttribute("AUTHENTICATED_USER", "yes");
                
                String strAttribute = authBean.getAttribute();
                String strCookie = authBean.getCookie();
                if(strAttribute != null) {
                    String strScope = authBean.getScope();
                    if(isAttribute(strAttribute, strScope, request)) {
                        log("*** DOING FILTER ***");
                        chain.doFilter(request, response);
                    } else {
                        log(" PASSTHRU");
                        passThrough(authBean, servletContext, request, response);
                    }
                } else {
                    if(strCookie != null) {
                        if(isCookie(strCookie, request)) {
                            log("*** DOING FILTER ***");
                            chain.doFilter(request, response);
                        } else {
                            log("PASSTHRU");
                            passThrough(authBean, servletContext, request, response);
                        }
                    } else {
                        chain.doFilter(request, response);
                    }
                }
            }
        }
    }
    
    /**
     * Set filter config.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Return filter config.
     */
    public FilterConfig getFilterConfig() {
        return this.filterConfig;
    }
    
    
    // ------------------------------------------------------ Protected Methods
    // none
    
    // -------------------------------------------------------- Private Methods

    /**
     * Perform a pass through.
     */
    private void passThrough(AuthenticationBean authBean, 
                             ServletContext context, 
                             ServletRequest request, 
                             ServletResponse response)
    throws IOException, ServletException {
        if(authBean.isRedirect()) {
            HttpServletResponse httpResponse = (HttpServletResponse)response;
            log("*** SENDING REDIRECT ***");

            httpResponse.sendRedirect(authBean.getRedirect());
        } else {
            RequestDispatcher dispatcher = 
                    context.getRequestDispatcher(authBean.getRedirect());
            log("***  FORWARDING to " + authBean.getRedirect() + " ***");

            dispatcher.forward(request, response);
        }
    }
    
    /**
     * Find an attribute.
     */
    private boolean isAttribute(String s, String s1, ServletRequest request) {
        if(s1 == null) {
            s1 = "session";
        }
        if(s1.equals("session")) {
            HttpServletRequest httpservletrequest = (HttpServletRequest)request;
            HttpSession httpsession = httpservletrequest.getSession();
            if(httpsession == null) {
                return false;
            } else {
                return httpsession.getAttribute(s) != null;
            }
        }
        if(s1.equals("request")) {
            return request.getAttribute(s) != null;
        }
        if(s1.equals("application")) {
            return filterConfig.getServletContext().getAttribute(s) != null;
        } else {
            return false;
        }
    }
    
    private boolean isCookie(String s, ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        Cookie cookie[] = httpRequest.getCookies();
        if(cookie == null) {
            return false;
        }
        for(int i = 0; i < cookie.length; i++) {
            if(cookie[i].getName().equals(s)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Output a message.
     *
     * @param strMessage the message
     */
    private void out(String strMessage) {
        if (strMessage != null) {
            StringBuffer sb = new StringBuffer("Authentication Filter: ");
            sb.append(strMessage);
            System.out.println(strMessage);
            sb = null;
        }
    }
    
    /**
     * Log a message.
     *
     * @param strMessage the message
     */
    private void log(String strMessage) {
        if (bDebug && (strMessage != null)) {
            StringBuffer sb = new StringBuffer("Authentication Filter: ");
            sb.append(strMessage);
            this.filterConfig.getServletContext().log(sb.toString());
            sb = null;
        }
    }
    
}

