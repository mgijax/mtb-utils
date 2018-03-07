/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXProgressGlassPane.java,v 1.1 2008/08/01 12:23:18 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import org.jax.mgi.mtb.gui.util.WizardImageCreator;

/**
 * A styled glass-pane that shows a semi-transparent pattern of gradient
 * horizontal lines with message strip when enabled.
 *
 * @author $Author: sbn $
 * @author Kirill Grouchnikov
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXProgressGlassPane.java,v 1.1 2008/08/01 12:23:18 sbn Exp $
 * @date $Date: 2008/08/01 12:23:18 $
 */
public final class MXProgressGlassPane extends JPanel {

    // -------------------------------------------------------------- Constants

    private Color darkColor = new Color(12, 12, 96);
    private Color midColor = new Color(64, 64, 255);
    private Color lightColor = new Color(96, 128, 255);


    // ----------------------------------------------------- Instance Variables

    /**
     * The animator thread.
     */
    private GlassPaneThread animator;

    /**
     * The current position of the highlight tracer. The value indicates the
     * center row of the tracer.
     */
    private int currHighLightRow;

    /**
     * The span of the highlight tracer. The tracer is twice as wide as this
     * value.
     */
    private static final int HIGHLIGHT_ROW_SPAN = 20;

    /**
     * The owner pane (to be <i>glassed</i>).
     */
    private final JRootPane owner;

    /**
     * An image of regular odd-numbered gradient line of <code>this</code>
     * glass pane.
     */
    private BufferedImage oddLine;

    /**
     * An image of regular even-numbered gradient line of <code>this</code>
     * glass pane.
     */
    private BufferedImage evenLine;

    /**
     * An image of dark odd-numbered gradient line of <code>this</code> glass
     * pane.
     */
    private BufferedImage oddDarkLine;

    /**
     * An image of dark even-numbered gradient line of <code>this</code> glass
     * pane.
     */
    private BufferedImage evenDarkLine;

    /**
     * The current message that is shown by <code>this</code> glass pane.
     */
    private String message;

    /**
     * The font of the current message.
     */
    private final Font messageFont;


    // ----------------------------------------------------------- Constructors

    /**
     * Simple constructor. Creates interceptor listeners for keyboard and mouse
     * events.
     */
    public MXProgressGlassPane(JRootPane owner) {
        this.owner = owner;
        // intercept mouse and keyboard events
        this.addMouseListener(new MouseAdapter() {
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
        });
        this.addKeyListener(new KeyAdapter() {
        });
        this.setOpaque(false);

        this.messageFont = new Font("Tahoma", Font.PLAIN, 20);
    }


    // --------------------------------------------------------- Public Methods

    /**
     * Sets the current message that will shown by <code>this</code> glass
     * pane.
     *
     * @param message
     *            The current message that will shown by <code>this</code>
     *            glass pane.
     */
    public final synchronized void setMessage(String message) {
        this.message = message;
    }

    public void paint(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.9f));

        int width = this.owner.getWidth();
        int height = this.owner.getHeight();

        if (this.oddLine == null) {
            this.oddLine = WizardImageCreator.createGradientLine(width, this.lightColor,
                    this.darkColor, 0.6);
            this.evenLine = WizardImageCreator.createGradientLine(width, this.lightColor
                    .brighter(), this.midColor, 0.6);
            this.oddDarkLine = WizardImageCreator.createGradientLine(width,
                    this.darkColor, Color.black, 0.8);
            this.evenDarkLine = WizardImageCreator.createGradientLine(width,
                    this.midColor, this.darkColor.darker(), 0.8);
        }

        // draw the lines
        for (int row = 0; row < height; row++) {
            if ((row % 2) == 0) {
                graphics.drawImage(this.evenLine, 0, row, null);
            } else {
                graphics.drawImage(this.oddLine, 0, row, null);
            }
        }

        // create highlight
        int rowStart = Math.max(0, this.currHighLightRow - HIGHLIGHT_ROW_SPAN);
        int rowEnd = Math.min(height - 1, this.currHighLightRow
                + HIGHLIGHT_ROW_SPAN);
        for (int row = rowStart; row <= rowEnd; row++) {
            if ((row % 2) == 1) {
                continue;
            }
            float opacity = (float) (0.25 * (1.0 - Math
                    .abs((float) (row - this.currHighLightRow))
                    / (float) HIGHLIGHT_ROW_SPAN));
            opacity = Math.max(0.0f, opacity);
            opacity = Math.min(1.0f, opacity);
            Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    opacity);
            graphics.setComposite(c);
            graphics.setColor(Color.white);
            graphics.drawLine(0, row, width, row);
        }

        // put the message
        if (this.message != null) {
            graphics.setFont(this.messageFont);
            FontRenderContext frc = graphics.getFontRenderContext();
            TextLayout mLayout = new TextLayout(message, this.messageFont, frc);
            int mWidth = (int) mLayout.getBounds().getWidth();
            int mHeight = (int) mLayout.getBounds().getHeight();

            rowStart = height - mHeight - 20;
            rowEnd = rowStart + mHeight + 5;
            int colStart = (width - mWidth) / 2 - 10;
            Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    (float) 1.0);
            graphics.setComposite(c);
            for (int row = rowStart; row <= rowEnd; row++) {
                if ((row % 2) == 0) {
                    graphics.drawImage(this.evenDarkLine, 0, row, null);
                } else {
                    graphics.drawImage(this.oddDarkLine, 0, row, null);
                }
            }
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setColor(this.darkColor.darker());
            graphics.drawString(this.message, colStart + 11, rowEnd - 4);
            graphics.setColor(this.lightColor.brighter());
            graphics.drawString(this.message, colStart + 10, rowEnd - 5);
        }
        super.paint(g);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.Component#setVisible(boolean)
     */
    public void setVisible(boolean isVisible) {
        if (isVisible) {
            this.currHighLightRow = -HIGHLIGHT_ROW_SPAN;
            this.animator = new GlassPaneThread(this, 30);
            this.animator.start();
        } else {
            if (this.animator != null) {
                this.animator.markStopped();
                this.animator = null;
            }
        }
        super.setVisible(isVisible);
        // this.owner.getUI().installUI(this.owner);
    }

    /**
     * Performs one iteration of the animation loop.
     */
    public void iteration() {
        if (this.owner == null) {
            return;
        }

        if (this.owner.getTopLevelAncestor() == null) {
            return;
        }

        if (!this.owner.getTopLevelAncestor().isVisible()) {
            return;
        }

        // move the highlight spot down. If necessary, loop back
        this.currHighLightRow += HIGHLIGHT_ROW_SPAN / 4;
        if (this.currHighLightRow > (this.getHeight() + HIGHLIGHT_ROW_SPAN)) {
            this.currHighLightRow = -HIGHLIGHT_ROW_SPAN;
        }

        // paint the glass pane image on it
        if (this.isVisible()) {
            this.repaint();
        }
    }

    public void setGlassPaneColors(Color darkColor, Color midColor,
            Color lightColor) {
        this.darkColor = darkColor;
        this.midColor = midColor;
        this.lightColor = lightColor;
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none

    // ---------------------------------------------------------- Inner Classes

    /**
     * Animator thread for the glass pane.
     *
     * @author Kirill Grouchnikov
     */
    private static final class GlassPaneThread extends Thread {

        /**
         * Sleep time between two successive calls to
         * {@link MXProgressGlassPane#iteration()}.
         */
        private final int sleepTime;

        /**
         * If <code>true</code>, <code>this</code> thread will quit the
         * next time it wakes.
         */
        private boolean toStop;

        /**
         * The associated glass pane.
         */
        private final MXProgressGlassPane glassPane;

        /**
         * Simple constructor.
         *
         * @param glassPane
         *            The associated glass pane.
         * @param sleepTime
         *            Sleep time between two successive calls to
         *            {@link MXProgressGlassPane#iteration()}.
         */
        public GlassPaneThread(MXProgressGlassPane glassPane, int sleepTime) {
            this.glassPane = glassPane;
            this.sleepTime = sleepTime;
        }

        /**
         * Requests that <code>this</code> thread should quit the next time it
         * wakes.
         */
        public synchronized void markStopped() {
            this.toStop = true;
        }

        /**
         * Checks whether <code>this</code> thread should quit the next time
         * it wakes.
         *
         * @return if <code>true</code>, the thread has been requested to
         *         quit the next time it wakes.
         */
        public synchronized boolean isMarkedStopped() {
            return this.toStop;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Runnable#run()
         */
        public void run() {
            this.toStop = false;
            while (!this.isMarkedStopped()) {
                this.glassPane.iteration();
                try {
                    Thread.sleep(this.sleepTime);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
