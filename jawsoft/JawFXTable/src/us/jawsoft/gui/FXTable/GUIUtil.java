package us.jawsoft.gui;


import java.awt.*;
import javax.swing.*;
import java.net.*;


public class GUIUtil{    
    /**     
     * Loads an <code>ImageIcon</code> object from this archive.    
     *<pre>     * Note: This method is compatible under Java Web Start.  
     *</pre>     * @param pathnamethe name of the icon file   
     * @return the icon if loaded, <code>null</code> otherwise   
     */    public static ImageIcon loadIcon(String pathname) {     
         URL url = GUIUtil.class.getClassLoader().getResource(pathname);        
         if (url != null)          
             return new ImageIcon(url);		
         System.err.println("Util.loadIcon Error: Could not find icon '" +    
             pathname + "'");        
         return null;  
     }
}