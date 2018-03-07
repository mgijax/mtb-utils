/*
 * MetalLookAndFeelAddons.java
 *
 * Created on October 27, 2005, 3:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package us.jawsoft.gui.plaf.metal;

import us.jawsoft.gui.plaf.basic.BasicLookAndFeelAddons;

/**
 * MetalLookAndFeelAddons.<br>
 *
 */
public class MetalLookAndFeelAddons extends BasicLookAndFeelAddons {
    
    public void initialize() {
        super.initialize();
        //MJV loadDefaults(getDefaults());
    }
    
    public void uninitialize() {
        super.uninitialize();
        //MJV unloadDefaults(getDefaults());
    }
    
    private Object[] getDefaults() {
        Object[] defaults =
                new Object[] {
            "DirectoryChooserUI",
                    "us.jawsoft.gui.plaf.windows.WindowsDirectoryChooserUI",
        };
        return defaults;
    }
}
