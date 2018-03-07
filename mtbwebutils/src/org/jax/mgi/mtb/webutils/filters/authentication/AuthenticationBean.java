/*
 * AuthenticationBean.java
 *
 * Created on March 17, 2006, 11:23 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jax.mgi.mtb.webutils.filters.authentication;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class AuthenticationBean
        implements Serializable {
    
    private String attribute;
    private String cookie;
    private String scope;
    private String redirect;
    private String except;
    private boolean doRedirect;
    private Hashtable<String, String> exceptHash;
    
    public AuthenticationBean() {
        exceptHash = new Hashtable<String, String>();
        doRedirect = false;
    }
    
    public void setAttribute(String s) {
        attribute = s;
    }
    
    public String getAttribute() {
        return attribute;
    }
    
    public void setCookie(String s) {
        cookie = s;
    }
    
    public String getCookie() {
        return cookie;
    }
    
    public void setScope(String s) {
        scope = s;
    }
    
    public String getScope() {
        return scope;
    }
    
    public String getRedirect() {
        return redirect;
    }
    
    public void setRedirect(String s) {
        redirect = s;
        doRedirect = checkRedirection(s);
    }
    
    public void setExcept(String s) {
        except = s;
        if(s != null) {
            String s1;
            for(StringTokenizer stringtokenizer = new StringTokenizer(s, ",;\t "); stringtokenizer.hasMoreTokens(); exceptHash.put(s1, s1)) {
                s1 = stringtokenizer.nextToken();
            }
            
        }
    }
    
    public String getExcept() {
        return except;
    }
    
    public boolean isRedirect() {
        return doRedirect;
    }
    
    public boolean exceptURI(String s) {
        return exceptHash.get(s) != null;
    }
    
    private boolean checkRedirection(String s) {
        String s1 = s.toUpperCase();
        return s1.startsWith("HTTP://") || s1.startsWith("HTTPS://");
    }
}
