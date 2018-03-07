/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/progress/MXProgressBarCustomUI.java,v 1.1 2008/08/01 12:32:05 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui.progress;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 * CustomUI for a ProgressBar component.
 * <br>
 * <p>
 * USAGE:
 * </p>
 * <code>
 * JProgressBar myProgressBar = new JProgressBar();
 * myProgressBar.setUI(new MXProgressBarCustomUI());
 * myProgressBar.setIndeterminate(true);
 * </code>
 * 
 * @author $Author: sbn $
 * @version $Revision: 1.1 $
 * @see javax.swing.plaf.basic.BasicProgressBarUI
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/progress/MXProgressBarCustomUI.java,v 1.1 2008/08/01 12:32:05 sbn Exp $
 * @date $Date: 2008/08/01 12:32:05 $
 */
public class MXProgressBarCustomUI
       extends BasicProgressBarUI
       implements ActionListener {

    // -------------------------------------------------------------- Constants

    private final Color startColor = new Color(192, 36, 36);
    private final Color endColor = new Color(192, 192, 192);

    // ----------------------------------------------------- Instance Variables

    private int x = 0;
    private int delta = +1;
    private Timer timer = null;

    // ----------------------------------------------------------- Constructors
    // none

    // --------------------------------------------------------- Public Methods

    /**
     * Repaint the progress bar when an action is performed.
     *
     * @param ae an <code>ActionEvent</code>
     */
    public void actionPerformed(ActionEvent ae){
        if (x<=0) {
            delta = +2;
        } else if (x>=progressBar.getWidth()) {
            delta = -2;
        }
        x += delta;

        progressBar.repaint();
    }

    /**
     * Paint the progress bar.
     *
     * @param g A <code>Graphics</code> object
     * @param c A <code>JComponent</code> object
     */
    public void paintIndeterminate(Graphics g, JComponent c) {
        if (delta > 0) {
            GradientPaint redtowhite = new GradientPaint(0, 0, endColor, x, 0, startColor, true);
            ((Graphics2D)g).setPaint(redtowhite);
            ((Graphics2D)g).fill(new RoundRectangle2D.Double(0, 0, x, progressBar.getHeight() - 1, 0, 0));
        } else {
            GradientPaint redtowhite = new GradientPaint(x, 0, startColor, progressBar.getWidth(), 0, endColor, true);
            ((Graphics2D)g).setPaint(redtowhite);
            ((Graphics2D)g).fill(new RoundRectangle2D.Double(x, 0, progressBar.getWidth() - x, progressBar.getHeight() - 1, 0, 0));
        }
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Start the timer.
     */
    protected void startAnimationTimer() {
        if (timer==null) {
            timer = new Timer(10, this);
        }
        x = 0;
        delta = +1;
        timer.start();
    }

    /**
     * Stop the timer.
     */
    protected void stopAnimationTimer() {
        timer.stop();
    }

    // -------------------------------------------------------- Private Methods
    // none
}