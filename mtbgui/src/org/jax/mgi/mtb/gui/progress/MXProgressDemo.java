/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/progress/MXProgressDemo.java,v 1.1 2008/08/01 12:32:05 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui.progress;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * Example of how to use the <code>ProgressDialog</code> component.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/08/01 12:32:05 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/progress/MXProgressDemo.java,v 1.1 2008/08/01 12:32:05 sbn Exp $
 */
public class MXProgressDemo {

    // -------------------------------------------------------------- Constants
    // none

    // ----------------------------------------------------- Instance Variables

    static JFrame frame;

    static Action heavyAction = new AbstractAction("Database Query"){
        public void actionPerformed(ActionEvent e){
            setEnabled(false);
            new Thread(heavyRunnable).start();
        }
    };

    static Runnable heavyRunnable = new Runnable(){
        public void run(){
            MXProgressMonitor monitor =
                  MXProgressUtil.createModalProgressMonitor(1, true);
            monitor.start("Fetching 1 of 10 records from database...");
            try {
                for (int i = 0; i < 10; i+=1) {
                    fetchRecord(i);
                    monitor.setCurrent("Fetching "+(i+1)+" of 10 records",
                                       (i+1)*10);
                }
            } finally {
                // make sure the progress dlg is closed in case of an exception
                if (monitor.getCurrent()!=monitor.getTotal()) {
                    monitor.setCurrent(null, monitor.getTotal());
                }
            }
            heavyAction.setEnabled(true);
        }

        private void fetchRecord(int index){
            try{
                Thread.sleep(1000);
            } catch(InterruptedException e){
                //Utils.log(e);
            }
        }
    };

    // ----------------------------------------------------------- Constructors
    // none

    // --------------------------------------------------------- Public Methods

    /**
     * Run the demo application.
     *
     * @param args An array of command line arguments
     */
    public static void main(String args[]){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){
            //Utils.log(e);
        }
        frame = new JFrame("Progress Dialog Demo");
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JButton(heavyAction));
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
}