/**
 * Header: /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/Timer.java,v 1.1 2007/04/30 15:52:19 mjv Exp
 * Author: mjv
 */
package org.jax.mgi.mtb.utils;

/**
 * Implements a simple Timer.
 *
 * @author mjv
 * @date 2007/04/30 15:52:19
 * @version 1.1
 * @cvsheader /mgi/cvsroot/mgi/mtb/mtbutils/src/org/jax/mgi/mtb/utils/Timer.java,v 1.1 2007/04/30 15:52:19 mjv Exp
 **/
public class Timer {

    // -------------------------------------------------------------- Constants
    // none
    
    // ----------------------------------------------------- Instance Variables
    private boolean running;
    private long timeStart;
    private long timeFinish;
    private long timeLength;

    // ----------------------------------------------------------- Constructors

    /**
     *  Initializes Timer to 0 msec
     **/
    public Timer() {
        reset();
    }

    // --------------------------------------------------------- Public Methods
    
    /**
     *  Starts the timer.  Accumulates time across multiple calls to start.
     **/
    public void start() {
        running = true;
        timeStart = System.currentTimeMillis();
        timeFinish = timeStart;
    }

    /**
     * Stops the timer. returns the time elapsed since the last matching call
     * to start(), or zero if no such matching call was made.
     *
     * @return The elapsed time
     */
    public long stop() {
        long diff = 0;
        
        timeFinish = System.currentTimeMillis();
        
        if (running) {
            running = false;

            diff = timeFinish - timeStart;

            timeLength += diff;
        }
        
        return diff;
    }

    /**
     * if RUNNING() ==> returns the time since last call to start()
     * if !RUNNING() ==> returns total elapsed time
     *
     * @return The elasped time in milliseconds
     */
    public long getElapsed() {
        if (running) {
            return System.currentTimeMillis() - timeStart;
        }

        return timeLength;
    }
    
    /**
     * if RUNNING() ==> returns the time since last call to start()
     * if !RUNNING() ==> returns total elapsed time
     *
     * @return The elasped time in seconds
     */
    public double getElapsedSeconds() {
        long time = getElapsed();
        
        if (time <= 0) {
            return 0.0;
        }
        
        return ((double)time) / 1000;
    }

    /**
     *  Stops timing, if currently RUNNING(); resets accumulated time to 0.
     */
    public void reset() {
        running = false;
        timeStart = 0;
        timeFinish = 0;
        timeLength = 0;
    }
    
    /**
     * Restart the timer.
     */
    public void restart() {
        reset();
        start();
    }
    
    /**
     * Returns a String representation of the timer.
     *
     * @return The time elapsed
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if(getElapsedSeconds() > 35){
          sb.append("*****************SLOW!***********");
        }
        sb.append(getElapsedSeconds());
        sb.append(" seconds (");
        sb.append(getElapsed());
        sb.append(" milliseconds)");
        
        return sb.toString();
    }
    
    // ------------------------------------------------------ Protected Methods
    // none
    
    // -------------------------------------------------------- Private Methods
    // none

}
