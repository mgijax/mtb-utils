/*
 * AbstractComponentAddon.java
 *
 * Created on October 27, 2005, 3:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package us.jawsoft.gui.plaf;

import us.jawsoft.gui.plaf.aqua.AquaLookAndFeelAddons;
import us.jawsoft.gui.plaf.metal.MetalLookAndFeelAddons;
import us.jawsoft.gui.plaf.motif.MotifLookAndFeelAddons;
import us.jawsoft.gui.plaf.windows.WindowsLookAndFeelAddons;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.UIManager;

/**
 * Ease the work of creating an addon for a component.<br>
 * 
 * @author Frederic Lavigne
 */
public abstract class AbstractComponentAddon implements ComponentAddon {

  private String name;
  
  protected AbstractComponentAddon(String name) {
    this.name = name;
  }
  
  public final String getName() {
    return name;
  }

  public void initialize(LookAndFeelAddons addon) {
    addon.loadDefaults(getDefaults(addon));
  }

  public void uninitialize(LookAndFeelAddons addon) {
    addon.unloadDefaults(getDefaults(addon));
  }

  /**
   * Adds default key/value pairs to the given list.
   * 
   * @param addon
   * @param defaults
   */
  protected void addBasicDefaults(LookAndFeelAddons addon, List defaults) {
  }

  /**
   * Default implementation calls {@link #addBasicDefaults(LookAndFeelAddons, List)}
   * 
   * @param addon
   * @param defaults
   */
  protected void addMacDefaults(LookAndFeelAddons addon, List defaults) {
    addBasicDefaults(addon, defaults);
  }

  /**
   * Default implementation calls {@link #addBasicDefaults(LookAndFeelAddons, List)}
   * 
   * @param addon
   * @param defaults
   */
  protected void addMetalDefaults(LookAndFeelAddons addon, List defaults) {
    addBasicDefaults(addon, defaults);
  }
  
  /**
   * Default implementation calls {@link #addBasicDefaults(LookAndFeelAddons, List)}
   * 
   * @param addon
   * @param defaults
   */
  protected void addMotifDefaults(LookAndFeelAddons addon, List defaults) {
    addBasicDefaults(addon, defaults);
  }

  /**
   * Default implementation calls {@link #addBasicDefaults(LookAndFeelAddons, List)}
   * 
   * @param addon
   * @param defaults
   */
  protected void addWindowsDefaults(LookAndFeelAddons addon, List defaults) {
    addBasicDefaults(addon, defaults);
  }
    
  /**
   * Gets the defaults for the given addon.
   * 
   * Based on the addon, it calls
   * {@link #addMacDefaults(LookAndFeelAddons, List)} if isMac()
   * or
   * {@link #addMetalDefaults(LookAndFeelAddons, List)} if isMetal()
   * or
   * {@link #addMotifDefaults(LookAndFeelAddons, List)} if isMotif()
   * or
   * {@link #addWindowsDefaults(LookAndFeelAddons, List)} if isWindows()
   * or
   * {@link #addBasicDefaults(LookAndFeelAddons, List)} if none of the above was called.
   * @param addon
   * @return an array of key/value pairs. For example:
   * <pre>
   * Object[] uiDefaults = {
   *   "Font", new Font("Dialog", Font.BOLD, 12),
   *   "Color", Color.red,
   *   "five", new Integer(5)
   * };
   * </pre>
   */
  private Object[] getDefaults(LookAndFeelAddons addon) {
    List defaults = new ArrayList();
    if (isWindows(addon)) {
      addWindowsDefaults(addon, defaults);
    } else if (isMetal(addon)) {
      addMetalDefaults(addon, defaults);
    } else if (isMac(addon)) {
      addMacDefaults(addon, defaults);
    } else if (isMotif(addon)) {
      addMotifDefaults(addon, defaults);
    } else {
      // at least add basic defaults
      addBasicDefaults(addon, defaults);
    }
    return defaults.toArray();
  }

  //
  // Helper methods to make ComponentAddon developer life easier
  //

  /**
   * Adds the all keys/values from the given named resource bundle to the
   * defaults
   */
  protected void addResource(List defaults, String bundleName) {
    ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
    for (Enumeration keys = bundle.getKeys(); keys.hasMoreElements(); ) {
      String key = (String)keys.nextElement();      
      defaults.add(key);
      defaults.add(bundle.getObject(key));
    }
  }
  
  /**
   * @return true if the addon is the Windows addon or its subclasses
   */
  protected boolean isWindows(LookAndFeelAddons addon) {
    return addon instanceof WindowsLookAndFeelAddons;
  }
  
  /**
   * @return true if the addon is the Metal addon or its subclasses
   */
  protected boolean isMetal(LookAndFeelAddons addon) {
    return addon instanceof MetalLookAndFeelAddons;
  }
  
  /**
   * @return true if the addon is the Aqua addon or its subclasses
   */
  protected boolean isMac(LookAndFeelAddons addon) {
    return addon instanceof AquaLookAndFeelAddons;
  }
  
  /**
   * @return true if the addon is the Motif addon or its subclasses
   */
  protected boolean isMotif(LookAndFeelAddons addon) {
    return addon instanceof MotifLookAndFeelAddons;
  }

  /**
   * @return true if the current look and feel is one of JGoodies Plastic l&fs
   */
  protected boolean isPlastic() {
    return UIManager.getLookAndFeel().getClass().getName().indexOf("Plastic") != -1;
  }

  /**
   * @return true if the current look and feel is Synth l&f
   */
  protected boolean isSynth() {
    return UIManager.getLookAndFeel().getClass().getName().indexOf("ynth") != -1;    
  }
  
}
