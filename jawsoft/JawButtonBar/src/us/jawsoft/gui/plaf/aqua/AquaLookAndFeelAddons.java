/*
 * AquaLookAndFellAddon.java
 *
 * Created on October 27, 2005, 3:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package us.jawsoft.gui.plaf.aqua;

import us.jawsoft.gui.plaf.basic.BasicLookAndFeelAddons;

public class AquaLookAndFeelAddons extends BasicLookAndFeelAddons {
    
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
            "TaskPaneGroupUI",
                    "us.jawsoft.gui.plaf.misc.GlossyTaskPaneGroupUI",
        };
        return defaults;
    }
    
}
