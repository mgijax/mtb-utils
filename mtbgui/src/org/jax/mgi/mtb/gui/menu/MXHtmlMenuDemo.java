/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/menu/MXHtmlMenuDemo.java,v 1.1 2008/08/01 12:32:04 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

/**
 * Example of how to use the <code>HtmlMenu</code> component.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/08/01 12:32:04 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/menu/MXHtmlMenuDemo.java,v 1.1 2008/08/01 12:32:04 sbn Exp $
 */
public class MXHtmlMenuDemo extends JFrame implements ActionListener {
    
    // -------------------------------------------------------------- Constants
    // none
    
    // ----------------------------------------------------- Instance Variables
    
    final private JPopupMenu menu = new JPopupMenu();

    
    // ----------------------------------------------------------- Constructors
    
    /**
     * 
     * Creates a new instance of MXHtmlMenuDemo.
     */
    public MXHtmlMenuDemo() {
        //HtmlMenu hmenu = new HtmlMenu("This is a menu");
        
        // Create and add a menu item
        MXHeaderMenuItem header = new MXHeaderMenuItem("Header Menu Item");
        menu.add(header);

        MXHtmlMenuItem item = new MXHtmlMenuItem("Item Label - Any Text Here");
        item.addActionListener(this);
        menu.add(item);
        
        // Set the component to show the popup menu
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    menu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
            public void mouseReleased(MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    menu.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        });        
    }
    
    // --------------------------------------------------------- Public Methods
    
    /**
     * Handles events.
     *
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e) {
        //EIGlobals.getInstance().log("action performed");
    }
    
    /**
     * Run the demo application.
     *
     * @param args An array of command line arguments
     */
    public static void main(String args[]){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){
            e.printStackTrace();
        }
        MXHtmlMenuDemo t = new MXHtmlMenuDemo();
        t.setSize(300, 200);
        t.setLocationRelativeTo(null);
        t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        t.setVisible(true);
    }
    
    // ------------------------------------------------------ Protected Methods
    // none
    
    // -------------------------------------------------------- Private Methods
    // none
}
