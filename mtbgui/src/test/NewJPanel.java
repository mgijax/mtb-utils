/*
 * NewJPanel.java
 *
 * Created on October 26, 2006, 12:41 PM
 */

package test;

import javax.swing.JFrame;

/**
 *
 * @author  mjv
 */
public class NewJPanel extends javax.swing.JPanel {
    
    /** Creates new form NewJPanel */
    public NewJPanel() {
        initComponents();
    }
    
    public static void main (String[] args) {
        JFrame f = new JFrame("TESTER");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(640,480);
        f.setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        mXHeaderPanel1 = new org.jax.mgi.mtb.gui.MXHeaderPanel();
        mXHyperlink1 = new org.jax.mgi.mtb.gui.MXHyperlink();
        mXTitledSeparator1 = new org.jax.mgi.mtb.gui.MXTitledSeparator();
        mXTitledSeparator2 = new org.jax.mgi.mtb.gui.MXTitledSeparator();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        mXHeaderPanel1.setDrawSeparatorUnderneath(true);
        mXHeaderPanel1.setStartColor(new java.awt.Color(102, 255, 102));

        mXHyperlink1.setText("mXHyperlink1");
        mXHyperlink1.setLinkBehavior(1);
        mXHyperlink1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mXHyperlink1ActionPerformed(evt);
            }
        });

        mXTitledSeparator1.setTitle("Abercrombie");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mXHeaderPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(mXTitledSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                .addContainerGap())
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(mXHyperlink1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(288, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(mXTitledSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(mXHeaderPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mXTitledSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mXHyperlink1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mXTitledSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(208, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void mXHyperlink1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mXHyperlink1ActionPerformed
        System.out.println("hi");
    }//GEN-LAST:event_mXHyperlink1ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jax.mgi.mtb.gui.MXHeaderPanel mXHeaderPanel1;
    private org.jax.mgi.mtb.gui.MXHyperlink mXHyperlink1;
    private org.jax.mgi.mtb.gui.MXTitledSeparator mXTitledSeparator1;
    private org.jax.mgi.mtb.gui.MXTitledSeparator mXTitledSeparator2;
    // End of variables declaration//GEN-END:variables
    
}
