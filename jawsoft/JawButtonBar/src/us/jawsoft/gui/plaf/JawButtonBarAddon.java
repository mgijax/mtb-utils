/*
 * JawButtonBarAddon.java
 *
 * Created on October 27, 2005, 3:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package us.jawsoft.gui.plaf;

import us.jawsoft.gui.JawButtonBar;
import us.jawsoft.gui.plaf.basic.BasicLookAndFeelAddons;
import us.jawsoft.gui.plaf.metal.MetalLookAndFeelAddons;
import us.jawsoft.gui.plaf.windows.WindowsLookAndFeelAddons;

/**
 * Addon for <code>JButtonBar</code>.<br>
 *
 */
public class JawButtonBarAddon implements ComponentAddon {
    
    public String getName() {
        return "JawButtonBar";
    }
    
    public void initialize(LookAndFeelAddons addon) {
        if (addon instanceof BasicLookAndFeelAddons) {
            addon.loadDefaults(new Object[]{
                JawButtonBar.UI_CLASS_ID,
                        "us.jawsoft.gui.plaf.basic.BasicButtonBarUI"
            });
        }
        
        if (addon instanceof MetalLookAndFeelAddons) {
            addon.loadDefaults(new Object[]{
                JawButtonBar.UI_CLASS_ID,
                        "us.jawsoft.gui.plaf.blue.BlueishButtonBarUI"
            });
        }
        
        if (addon instanceof WindowsLookAndFeelAddons) {
            addon.loadDefaults(new Object[]{
                JawButtonBar.UI_CLASS_ID,
                        "us.jawsoft.gui.plaf.blue.BlueishButtonBarUI"
            });
        }
    }
    
    public void uninitialize(LookAndFeelAddons addon) {
    }
    
}
