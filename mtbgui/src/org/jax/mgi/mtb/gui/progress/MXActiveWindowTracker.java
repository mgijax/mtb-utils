/*
 * MXActiveWindowTracker.java
 *
 * Created on October 12, 2006, 3:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jax.mgi.mtb.gui.progress;


import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Stack;
import javax.swing.JDialog;

/**
 * Utility class to get Current/Last Active Window.
 * Usage:
 * <pre>
 *    MXActiveWindowTracker.findActiveWindow();
 * </pre>
 * this is always guaranteed to return non-null window
 * <br>
 * NOTE:
 * <br>
 * Ensure that MXActiveWindowTracker class is loaded
 * before any window is shown, to get accurate results
 * 
 * @author Santhosh Kumar T
 * @email santhosh@in.fiorano.com
 */
public class MXActiveWindowTracker{
    static Stack showingWindows = new Stack();

    private static WindowListener windowListener = new WindowAdapter(){
        public void windowDeactivated(WindowEvent we){
            if(!we.getWindow().isShowing())
                windowHiddenOrClosed(we);
        }

        public void windowClosed(WindowEvent we){
            windowHiddenOrClosed(we);
        }

        private void windowHiddenOrClosed(WindowEvent we){
            we.getWindow().removeWindowListener(windowListener);
            showingWindows.remove(we.getWindow());
        }
    };

    private static PropertyChangeListener propListener = new PropertyChangeListener(){
        public void propertyChange(PropertyChangeEvent evt){
            if(evt.getNewValue()!=null){
                Window window = (Window)evt.getNewValue();
                if(!showingWindows.contains(window)){
                    window.addWindowListener(windowListener);
                    showingWindows.remove(window);
                }
                showingWindows.push(window);
            }
        }
    };

    static{
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addPropertyChangeListener("activeWindow", propListener);
    }

    public static Window findActiveWindow(){
        if(showingWindows.isEmpty()) {
            // Trick to get the shared frame instance.
            JDialog dlg = new JDialog();
            Window owner = dlg.getOwner();
            dlg.dispose();
            return owner;
        } else {
            return (Window)showingWindows.peek();
        }
    }
}