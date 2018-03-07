/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/progress/MXProgressDialog.java,v 1.1 2008/08/01 12:32:05 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui.progress;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.RenderingHints;
import java.lang.reflect.InvocationTargetException;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * An extended class of <code>JDialog</code> which displays an a progress
 * dialog component.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/08/01 12:32:05 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/progress/MXProgressDialog.java,v 1.1 2008/08/01 12:32:05 sbn Exp $
 * @see javax.swing.JDialog
 */
public class MXProgressDialog extends JDialog
       implements ChangeListener {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    private JLabel lblStatus = new JLabel();
    private JProgressBar progressBar;
    private MXProgressMonitor monitor;
    private boolean bDebugMode = false;

    // ----------------------------------------------------------- Constructors

    /**
     * Create a new MXProgressDialog object
     * 
     * @param owner The owner of the dialog
     * @oaram monitor The monitoring object
     * @throws HeadlessException if this is on a headless box
     */
    public MXProgressDialog(Frame owner, MXProgressMonitor monitor)
        throws HeadlessException {

        super(owner, "Progress", false);
        init(monitor);
    }

    /**
     * Create a new MXProgressDialog object
     * 
     * @param owner The owner of the dialog
     * @oaram monitor The monitoring object
     * @throws HeadlessException if this is on a headless box
     */
    public MXProgressDialog(Dialog owner, MXProgressMonitor monitor)
        throws HeadlessException {

        super(owner);
        init(monitor);
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Handle the a change of stae.
     *
     * @param ce A <code>ChangeEvent</code> object
     */
    public void stateChanged(final ChangeEvent ce){
        //Utils.log("Monitor Current: " + monitor.getCurrent());
        //Utils.log("Monitor Total: " + monitor.getTotal());
        //Utils.log("Monitor Status: " + monitor.getStatus());

        // to ensure EDT thread
        if (!SwingUtilities.isEventDispatchThread()){
            try{
                SwingUtilities.invokeAndWait(new Runnable(){
                    public void run(){
                        stateChanged(ce);
                    }
                });
            } catch(InterruptedException e){
                e.printStackTrace();
            } catch(InvocationTargetException e){
                // should never happen
                e.printStackTrace();
            }
            return;
        }

        if (monitor.isFinished()){
            dispose();
        } else {
            lblStatus.setText(monitor.getStatus());
            if(!monitor.isIndeterminate()) {
                progressBar.setValue(monitor.getCurrent());
            }
        }
    }

    public void dispose() {
        super.dispose();
    }
    
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        super.paint(g2d);
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods

    /**
     * Initialize the component.
     *
     * @param monitor the monitor
     */
    private void init(MXProgressMonitor monitor) {
        this.monitor = monitor;

        progressBar = new JProgressBar(0, monitor.getTotal());
        progressBar.setUI(new MXProgressBarCustomUI());

        if(monitor.isIndeterminate()) {
            progressBar.setIndeterminate(true);
        } else {
            progressBar.setValue(monitor.getCurrent());
        }

        lblStatus.setText(monitor.getStatus());

        JPanel contents = (JPanel)getContentPane();
        contents.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contents.add(lblStatus, BorderLayout.NORTH);
        contents.add(progressBar);

        /*
        JButton btnDebug = new JButton(" ");
        btnDebug.setBorder(new EmptyBorder(1,1,1,1));
        btnDebug.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               bDebugMode = !bDebugMode;
               debug();
            }
        });
        contents.add(btnDebug, BorderLayout.SOUTH);
         */

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        monitor.addChangeListener(this);
    }

    /**
     * Display some debug diagnostic information.
     */
    private void debug() {
       //Utils.log("Monitor Current: " + monitor.getCurrent());
       //Utils.log("Monitor Total: " + monitor.getTotal());
       //Utils.log("Monitor Status: " + monitor.getStatus());
    }
}