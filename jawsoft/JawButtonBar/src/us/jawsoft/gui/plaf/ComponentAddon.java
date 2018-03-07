/*
 * ComponentAddon.java
 *
 * Created on October 27, 2005, 3:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package us.jawsoft.gui.plaf;
/**
 * Each new component type of the library will contribute an addon to
 * the LookAndFeelAddons. A <code>ComponentAddon</code> is the
 * equivalent of a {@link javax.swing.LookAndFeel}but focused on one
 * component. <br>
 *
 * @author <a href="mailto:fred@L2FProd.com">Frederic Lavigne</a>
 */
public interface ComponentAddon {
    
    /**
     * @return the name of this addon
     */
    String getName();
    
    /**
     * Initializes this addon (i.e register UI classes, colors, fonts,
     * borders, any UIResource used by the component class). When
     * initializing, the addon can register different resources based on
     * the addon or the current look and feel.
     *
     * @param addon the current addon
     */
    void initialize(LookAndFeelAddons addon);
    
    /**
     * Uninitializes this addon.
     *
     * @param addon
     */
    void uninitialize(LookAndFeelAddons addon);
    
}