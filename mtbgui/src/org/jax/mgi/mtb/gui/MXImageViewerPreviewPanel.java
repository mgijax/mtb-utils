/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXImageViewerPreviewPanel.java,v 1.1 2008/08/01 12:23:18 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * A preview pane for a file chooser.
 *
 * <pre><code>
 * JFileChooser chooser = new JFileChooser();
 * MXImageViewerPreviewPanel preview = new MXImageViewerPreviewPanel();
 * chooser.setAccessory(preview);
 * chooser.addPropertyChangeListener(preview);
 * </code></pre>
 *
 * @author $Author: sbn $
 * @version $Revision: 1.1 $
 * @see org.jax.mgi.mtb.gui.MXImageViewer
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXImageViewerPreviewPanel.java,v 1.1 2008/08/01 12:23:18 sbn Exp $
 * @date $Date: 2008/08/01 12:23:18 $
 */
public class MXImageViewerPreviewPanel extends JPanel
        implements PropertyChangeListener {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    private MXImageViewer iv = new MXImageViewer();


    // ----------------------------------------------------------- Constructors

    /**
     * Default constructor.
     */
    public MXImageViewerPreviewPanel() {
        setLayout(new BorderLayout(5,5));
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        add(new JLabel("Image Preview:"), BorderLayout.NORTH);
        iv.showToolBar(false);
        iv.setSize(new Dimension(200, 200));
        iv.setPreferredSize(new Dimension(200, 200));
        iv.setMaximumSize(new Dimension(200, 200));
        iv.setMinimumSize(new Dimension(200, 200));
        add(iv, BorderLayout.CENTER);
    }


    // --------------------------------------------------------- Public Methods

    /**
     * Create a new <code>MXImageViewer</code> when a file has been selected.
     *
     * @param evt the property change event
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(
                                                      evt.getPropertyName())) {

            final File newFile = (File) evt.getNewValue();

            if(newFile != null) {
                String path = newFile.getAbsolutePath();
                String temp = path.toLowerCase();

                if(temp.endsWith(".gif") || temp.endsWith(".jpg") ||
                   temp.endsWith(".jpeg")  || temp.endsWith(".png") ||
                   temp.endsWith(".bmp") || temp.endsWith(".tiff") ||
                   temp.endsWith(".tif")) {
                    try {
                        this.remove(iv);
                        final JLabel lbl = new JLabel("Loading image...");
                        SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    add(lbl, BorderLayout.CENTER);
                                    revalidate();
                                }
                            });

                        SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    remove(lbl);
                                    iv = new MXImageViewer(newFile);
                                    Dimension d = new Dimension(200, 200);
                                    iv.setSize(d);
                                    iv.setPreferredSize(d);
                                    iv.setMaximumSize(d);
                                    iv.setMinimumSize(d);
                                    iv.refresh();
                                    add(iv, BorderLayout.CENTER);
                                    revalidate();
                                }
                            });
                    } catch(Exception e) {
                        // couldn't read image.
                    }
                }
            }
        }
    }

    /**
     * A main method to test the class.
     *
     * @param a the commmand line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            //Utils.log(e);
        }

        final JFrame frame = new JFrame();
        JButton button = new JButton("Open File Chooser");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                MXImageViewerPreviewPanel preview = new MXImageViewerPreviewPanel();
                chooser.setAccessory(preview);
                chooser.addPropertyChangeListener(preview);
                chooser.showDialog(frame, "OK");
            }
        });
        frame.getContentPane().add(button);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
}
