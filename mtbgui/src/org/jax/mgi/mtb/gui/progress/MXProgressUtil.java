/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/progress/MXProgressUtil.java,v 1.1 2008/08/01 12:32:06 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui.progress;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Utility class for <code>MXProgressDialog>/code>.
 * 
 * @author $Author: sbn $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/progress/MXProgressUtil.java,v 1.1 2008/08/01 12:32:06 sbn Exp $
 * @date $Date: 2008/08/01 12:32:06 $
 */
public class MXProgressUtil {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables
    // none

    // ----------------------------------------------------------- Constructors
    // none

    // --------------------------------------------------------- Public Methods

    /**
     * Create the modal <code>MXProgressDialog</code>.
     * 
     * @param owner The dialog owner
     * @param total The total steps
     * @param indeterminate true for indeterminate, false otherwise
     * @return A <code>MXProgressMonitor</code>
     */
    public static MXProgressMonitor createModalProgressMonitor(int total, boolean indeterminate){
        MXProgressMonitor monitor = new MXProgressMonitor(total, indeterminate);
        monitor.addChangeListener(new MonitorListener(monitor));
        return monitor;
    }

    /**
     * Create the modal <code>MXProgressDialog</code>.
     * 
     * @param owner The dialog owner
     * @param total The total steps
     * @param indeterminate true for indeterminate, false otherwise
     * @param milliSecondsToWait Time to wait until completion
     * @return A <code>MXProgressMonitor</code>
     */
    public static MXProgressMonitor createModalProgressMonitor(int total, boolean indeterminate, int milliSecondsToWait){
        MXProgressMonitor monitor = new MXProgressMonitor(total, indeterminate, milliSecondsToWait);
        monitor.addChangeListener(new MonitorListener(monitor));
        return monitor;
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none

    // ---------------------------------------------------- Static Utlity Class

    static class MonitorListener implements ChangeListener, ActionListener {
        MXProgressMonitor monitor;
        Timer timer;

        public MonitorListener(MXProgressMonitor monitor){
            this.monitor = monitor;
        }

        public void stateChanged(ChangeEvent ce){
            MXProgressMonitor monitor = (MXProgressMonitor)ce.getSource();
            if (monitor.isFinished()) {
                if (timer!=null && timer.isRunning()) {
                    timer.stop();
                }
                monitor.removeChangeListener(this);
            } else {
                if (timer==null) {
                    timer = new Timer(monitor.getMilliSecondsToWait(), this);
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        }

        public void actionPerformed(ActionEvent e){
            monitor.removeChangeListener(this);
            if(!monitor.isFinished()){ // better to check again
                Window owner = MXActiveWindowTracker.findActiveWindow();
                MXProgressDialog dlg = owner instanceof Frame
                        ? new MXProgressDialog((Frame)owner, monitor)
                        : new MXProgressDialog((Dialog)owner, monitor);
                dlg.pack();
                dlg.setLocationRelativeTo(null);
                if(!monitor.isFinished()) { // better to check again
                    dlg.setVisible(true);
                }
            }
        }
    }
}