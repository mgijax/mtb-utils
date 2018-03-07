/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXImageViewer.java,v 1.1 2008/08/01 12:23:18 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.media.jai.PlanarImage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jax.mgi.mtb.gui.imageviewer.MXImagePanel;
import org.jax.mgi.mtb.gui.imageviewer.MXJAIUtils;


/**
 * An <code>MXImageViewer</code> is a custom component used to diaply all types
 * of images (jpeg, gif, tiff, etc.).
 *
 * @author $Author: sbn $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXImageViewer.java,v 1.1 2008/08/01 12:23:18 sbn Exp $
 * @date $Date: 2008/08/01 12:23:18 $
 */
public class MXImageViewer extends JPanel implements MouseListener {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    private boolean showToolBar = true;
    private File file = null;
    private URL url = null;
    private MXImagePanel imagePanel = null;


    // ----------------------------------------------------------- Constructors

    /**
     * Construct an MXImageViewer without a toolbar and without an image.
     */
    public MXImageViewer() {
        initComponents();
    }

    /**
     * Construct an MXImageViewer without a toolbar.
     *
     *
     * @param f The file to view
     */
    public MXImageViewer(File f) {
        this(f, false);
    }

    /**
     * Construct an MXImageViewer without a toolbar.
     *
     *
     * @param u The url of the file to view
     */
    public MXImageViewer(URL u) {
        this(u, false);
    }

    /**
     *
     * Construct an MXImageViewer with optional toolbar.
     *
     * @param f The file to view
     * @param toolBar <code>true</code> to show the toolbar, <code>false</code>
     *        otherwise
     */
    public MXImageViewer(File f, boolean toolBar) {
        initComponents();
        this.file = f;
        showToolBar(toolBar);

        try {
            this.url = f.toURL();
        } catch (MalformedURLException e) {
            //Utils.log(e);
        }

        loadImage();
        getImageInformation();
        displayImage();
    }

    /**
     * Construct an MXImageViewer with optional toolbar.
     *
     * @param u The url of the file to view
     * @param toolBar <code>true</code> to show the toolbar, <code>false</code>
     *        otherwise
     */
    public MXImageViewer(URL u, boolean toolBar) {
        initComponents();
        this.url = u;
        showToolBar(toolBar);

        // Convert the URL to a file object
        file = new File(url.getFile());

        loadImage();
        getImageInformation();
        displayImage();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Set the image to display.
     *
     * @param f The file to view
     */
    public void setImage(File f) {
        this.file = f;
        loadImage();
        getImageInformation();
        displayImage();
    }

    /**
     * Set the image to display.
     *
     * @param u The url of the file to view
     */
    public void setImage(URL u) {
        file = new File(u.getFile());
        this.url = u;
        loadImage();
        getImageInformation();
        displayImage();
    }


    public void setShowToolbar(boolean bShow) {
        showToolBar(bShow);
    }

    public boolean getShowToolbar() {
        return this.showToolBar;
    }

    /**
     * Show or don't show the toolbar.
     *
     * @param show <code>true</code> to show the toolbar, <code>false</code>
     *        otherwise
     */
    public void showToolBar(boolean show) {
        if (this.showToolBar == show) {
            return;
        }

        this.showToolBar = show;

        if (show) {
            this.add(pnlToolBar, BorderLayout.NORTH);
        } else {
            this.remove(pnlToolBar);
        }

        this.revalidate();
    }

    /**
     * Refersh this component.
     */
    public void refresh() {
        try {
            Dimension thisSize = this.getSize();
            thisSize.width -= 10;
            thisSize.height -= 10;
            PlanarImage[] i = imagePanel.getImages();
            float h = (float)i[0].getHeight() + 10;
            float w = (float)i[0].getWidth() + 10;
            float scaleh = 0.0f;
            float scalew = 0.0f;

            if (thisSize.height < h) {
                scaleh = (thisSize.height/h);
            }

            if (thisSize.width < w) {
                scalew = (thisSize.width/w);
            }

            if ((scaleh == 0.0f) && (scalew == 0.0f)) {
                imagePanel.refresh(1.0f);
            } else {
                imagePanel.refresh(Math.min(scalew, scaleh));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Process a mouse click event.
     *
     * @param e The <code>MouseEvent</code>
     */
    public void mouseClicked(MouseEvent e) {
        Point pt = e.getPoint();

        //Utils.log("pt="+pt.toString());

        /*
        if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
            //processLeft(evt.getPoint());
        }
        if ((e.getModifiers() & InputEvent.BUTTON2_MASK) != 0) {
            //processMiddle(evt.getPoint());
        }
        if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
            //processRight(evt.getPoint());
        }
        */
    }

    /**
     * Process a mouse exited event (do nothing in this case).
     *
     * @param e The <code>MouseEvent</code>
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Process a mouse entered event (do nothing in this case).
     *
     * @param e The <code>MouseEvent</code>
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Process a mouse pressed event (do nothing in this case).
     *
     * @param e The <code>MouseEvent</code>
     */
    public void mousePressed(MouseEvent e) {
    }

    /**
     * Process a mouse released event (do nothing in this case).
     *
     * @param e The <code>MouseEvent</code>
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Demoable main method class.
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {

        JFrame f = new JFrame("ImageViewer Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(300, 300);
        URL url = null;
        String u = "http://tumor.informatics.jax.org/img/mtb_logo_side.png";
        try {
            url = new URL(u);
        } catch (MalformedURLException e) {
            //Utils.log(e);
        }
        MXImageViewer iv = new MXImageViewer(url, false);
        Dimension d = new Dimension(200, 200);
        iv.setSize(d);
        iv.setPreferredSize(d);
        iv.setMinimumSize(d);
        iv.setMaximumSize(d);
        iv.refresh();
        f.getContentPane().setLayout(new FlowLayout());
        f.getContentPane().add(iv);
        f.setVisible(true);
    }


    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods

    /**
     * Loads an image in a separate worker thread. When the image
     * has finished loading, it is displayed in a window. To ensure
     * correct behaviour, the displaying is executed on the AWT event
     * thread.
     *
     * @param pURL The URL of the image to load.
     */
    private void loadImage() {
        imagePanel = new MXImagePanel(200, 200);
        try {
            //EIGlobals.getInstance().log("Loading: " + url.toString());
            imagePanel.setImages(MXJAIUtils.load(url));
            imagePanel.fitWindow();
        } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                        "Unable to open file at URL:\n"
                        + file.toString() + "\n\n" + ex.getMessage());
        }


        imagePanel.fitWindow();
        imagePanel.revalidate();

    }

    /**
     * Get the image information.
     */
    private void getImageInformation() {
        /*
        try {
            String dimensions = imagePanel.getCurrentImage().getWidth() +
                                "px X " +
                                imagePanel.getCurrentImage().getHeight() +
                                "px";
            //lblImageDimensionsValue.setText(dimensions);

        } catch (Exception e) {
        }
        */
    }

    /**
     * Display the image.
     */
    private void displayImage() {
        pnlImage.add(imagePanel, BorderLayout.CENTER);
        pnlImage.revalidate();
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------ NetBeans Generated Code
    // ------------------------------------------------------------------------
    // TAKE EXTREME CARE MODIFYING CODE BELOW THIS LINE

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        pnlImage = new javax.swing.JPanel();
        pnlToolBar = new javax.swing.JPanel();
        btnZoomIn = new javax.swing.JButton();
        btnZoomOut = new javax.swing.JButton();
        btnRotateLeft = new javax.swing.JButton();
        btnRotateRight = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        pnlImage.setLayout(new java.awt.BorderLayout());

        add(pnlImage, java.awt.BorderLayout.CENTER);

        pnlToolBar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5));

        btnZoomIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jax/mgi/mtb/gui/resources/img/ZoomIn16.png")));
        btnZoomIn.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomInActionPerformed(evt);
            }
        });

        pnlToolBar.add(btnZoomIn);

        btnZoomOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jax/mgi/mtb/gui/resources/img/ZoomOut16.png")));
        btnZoomOut.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomOutActionPerformed(evt);
            }
        });

        pnlToolBar.add(btnZoomOut);

        btnRotateLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jax/mgi/mtb/gui/resources/img/RotateLeft16.png")));
        btnRotateLeft.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnRotateLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRotateLeftActionPerformed(evt);
            }
        });

        pnlToolBar.add(btnRotateLeft);

        btnRotateRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jax/mgi/mtb/gui/resources/img/RotateRight16.png")));
        btnRotateRight.setMargin(new java.awt.Insets(1, 1, 1, 1));
        btnRotateRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRotateRightActionPerformed(evt);
            }
        });

        pnlToolBar.add(btnRotateRight);

        add(pnlToolBar, java.awt.BorderLayout.NORTH);

    }// </editor-fold>//GEN-END:initComponents

    private void btnRotateRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRotateRightActionPerformed
        imagePanel.rotate(-1 * (Math.PI / 2));
    }//GEN-LAST:event_btnRotateRightActionPerformed

    private void btnRotateLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRotateLeftActionPerformed
        imagePanel.rotate(Math.PI / 2);
    }//GEN-LAST:event_btnRotateLeftActionPerformed

    private void btnZoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomOutActionPerformed
        imagePanel.zoomOut(1.5);
    }//GEN-LAST:event_btnZoomOutActionPerformed

    private void btnZoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomInActionPerformed
        imagePanel.zoomIn(1.5);
    }//GEN-LAST:event_btnZoomInActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRotateLeft;
    private javax.swing.JButton btnRotateRight;
    private javax.swing.JButton btnZoomIn;
    private javax.swing.JButton btnZoomOut;
    private javax.swing.JPanel pnlImage;
    private javax.swing.JPanel pnlToolBar;
    // End of variables declaration//GEN-END:variables

}
