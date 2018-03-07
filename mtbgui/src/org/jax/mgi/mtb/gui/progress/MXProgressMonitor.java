/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/progress/MXProgressMonitor.java,v 1.1 2008/08/01 12:32:05 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui.progress;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A <code>MXProgressMonitor</code> class does the bulf of the work for keeping
 * tack of the current state (progress).
 * 
 * @author $Author: sbn $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/progress/MXProgressMonitor.java,v 1.1 2008/08/01 12:32:05 sbn Exp $
 * @date $Date: 2008/08/01 12:32:05 $
 */
public class MXProgressMonitor {

    // -------------------------------------------------------------- Constants

    private static final int defaultMilliseconds = 500;  // half second

    // ----------------------------------------------------- Instance Variables

    private int total;
    private int current = -1;
    private boolean indeterminate;
    private int milliSecondsToWait;
    private String status;
    private List listeners = new ArrayList();
    private ChangeEvent ce = new ChangeEvent(this);

    // ----------------------------------------------------------- Constructors

    /**
     * Construct the <code>MXProgressMonitor</code>.
     * 
     * @param total the number of steps
     * @param indeterminate true if end is unknown, false otherwise
     */
    public MXProgressMonitor(int total, boolean indeterminate){
        this(total, indeterminate, defaultMilliseconds);
    }

    /**
     * Construct the <code>MXProgressMonitor</code>.
     * 
     * @param total the number of steps
     * @param indeterminate true if end is unknown, false otherwise
     * @millis the time to wait in milliseconds
     */
    public MXProgressMonitor(int total, boolean indeterminate, int millis){
        this.total = total;
        this.indeterminate = indeterminate;
        this.milliSecondsToWait = millis;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Get the total number of steps.
     *
     * @return The total number of steps
     */
    public int getTotal(){
        return this.total;
    }

    /**
     * Start the monitor.
     *
     * @param status The message to display
     */
    public void start(String status){
        if(current != -1) {
            throw new IllegalStateException("not started yet");
        }

        this.status = status;
        current = 0;
        fireChangeEvent();
    }

    /**
     * Get the number of milliseconds to wait.
     *
     * @return The number of milliseconds
     */
    public int getMilliSecondsToWait(){
        return this.milliSecondsToWait;
    }

    /**
     * Get the current step
     *
     * @return the current step
     */
    public int getCurrent(){
        return this.current;
    }

    /**
     * Get the status message.
     *
     * @return the status message
     */
    public String getStatus(){
        return this.status;
    }

    /**
     * Returns true if this is an indeterminate progress dialog, false
     * otherwise.
     *
     * @return true if this is an indeterminate progress dialog, false
     * otherwise.
     */
    public boolean isIndeterminate(){
        return this.indeterminate;
    }

    /**
     * Set the current step and status message.
     *
     * @param status the status message
     * @param current the current step
     */
    public void setCurrent(String status, int current){
        if(current == -1) {
            throw new IllegalStateException("not started yet");
        }

        this.current = current;

        if(status != null) {
            this.status = status;
        }
        fireChangeEvent();
    }

    /**
     * Add a <code>ChangeListener</code>.
     *
     * @param listener the <code>ChangeListener</code>
     */
    public void addChangeListener(ChangeListener listener){
        listeners.add(listener);
    }

    /**
     * Remove a <code>ChangeListener</code>.
     *
     * @param listener the <code>ChangeListener</code>
     */
    public void removeChangeListener(ChangeListener listener){
        listeners.remove(listener);
    }

    public boolean isFinished() {
        return current==total;
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods

    /**
     * For listener support.  Calls the <code>stateChanged</code> method for
     * each registered listener.
     */
    private void fireChangeEvent() {
        int s = listeners.size();

        ChangeListener listener[] =
                (ChangeListener[])listeners.toArray(new ChangeListener[s]);

        for (int i = 0; i < listener.length; i++) {
            listener[i].stateChanged(ce);
        }
    }
}