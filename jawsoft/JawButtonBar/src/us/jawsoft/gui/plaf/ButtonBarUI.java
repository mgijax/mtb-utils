/*
 * ButtonBarUI.java
 *
 * Created on October 27, 2005, 3:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package us.jawsoft.gui.plaf;

import javax.swing.AbstractButton;
import javax.swing.plaf.ComponentUI;

/**
 * Pluggable UI class for <code>ButtonBar</code>.
 */
public abstract class ButtonBarUI extends ComponentUI {

  /**
   * Called when an AbstractButton is added to a ButtonBarUI. It allows
   * pluggable UI to provide custom UI for the buttons added to the ButtonBar.
   * 
   * @param button
   */
  public void installButtonBarUI(AbstractButton button) {
  }

}
