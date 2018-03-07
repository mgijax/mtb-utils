/*
 * MXCompleterTest.java
 *
 * Created on May 23, 2006, 10:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jax.mgi.mtb.gui.completer;

/*
 * MXCompleterTest.java
 *
 * Created on October 25, 2005, 2:45 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimeZone;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Copyright Neil Cochrane 2006
 * @author neilcochrane
 */
public class MXCompleterTest
{
  
  /**
     * Creates a new instance of MXCompleterTest
     */
  public MXCompleterTest()
  {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(_createPanel(), BorderLayout.NORTH);   
    frame.setBounds(100, 100, 550, 350);
    frame.setVisible(true);
  }
  
  private JPanel _createPanel()
  {
    _tf = new MXCompleterTextField(TimeZone.getAvailableIDs(), false);

    final JCheckBox caseCheck = new JCheckBox("Case-sensitive");
    caseCheck.setSelected(_tf.isCaseSensitive());

    final JCheckBox correctCheck = new JCheckBox("Correct case");
    correctCheck.setSelected(_tf.isCorrectingCase());
    correctCheck.setEnabled(!caseCheck.isSelected());
    correctCheck.setToolTipText("Will change the case of the entered string to match the case of the matched string");

    caseCheck.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent arg0)
      {
        _tf.setCaseSensitive(caseCheck.isSelected());
        correctCheck.setEnabled(!caseCheck.isSelected());
        if (caseCheck.isSelected())
        {
          correctCheck.setSelected(false);
          _tf.setCorrectCase(false);
        }
      }
    });  
    
    correctCheck.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent arg0)
      {
        _tf.setCorrectCase(correctCheck.isSelected());
      }
    });
    
    JPanel panel = new JPanel(new GridLayout(11, 2, 5, 2));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    panel.add(new JLabel("Auto-complete text field:"));
    panel.add(_tf);

    panel.add(Box.createGlue());
    panel.add(caseCheck);

    panel.add(Box.createGlue());
    panel.add(correctCheck);

    panel.add(Box.createVerticalStrut(20));
    panel.add(Box.createVerticalStrut(20));
    
    _tfww = new MXCompleterTextField(TimeZone.getAvailableIDs(), true);
    final JCheckBox caseCheck2 = new JCheckBox("Case-sensitive");
    caseCheck2.setSelected(_tfww.isCaseSensitive());

    final JCheckBox correctCheck2 = new JCheckBox("Correct case");
    correctCheck2.setSelected(_tfww.isCorrectingCase());
    correctCheck2.setEnabled(!caseCheck2.isSelected());
    correctCheck2.setToolTipText("Will change the case of the entered string to match the case of the matched string");
    
    caseCheck2.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent arg0)
      {
        _tfww.setCaseSensitive(caseCheck2.isSelected());
        correctCheck2.setEnabled(!caseCheck2.isSelected());
        if (caseCheck2.isSelected())
        {
          correctCheck2.setSelected(false);
          _tfww.setCorrectCase(false);
        }
      }
    });  
    
    correctCheck2.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent arg0)
      {
        _tfww.setCorrectCase(correctCheck2.isSelected());
      }
    });
    
    panel.add(new JLabel("Auto-complete text field with window:"));
    panel.add(_tfww);
    
    panel.add(Box.createGlue());
    panel.add(caseCheck2);

    panel.add(Box.createGlue());
    panel.add(correctCheck2);

    panel.add(Box.createVerticalStrut(20));
    panel.add(Box.createVerticalStrut(20));
    
    _combo = new MXCompleterComboBox(TimeZone.getAvailableIDs());
    final JCheckBox caseCheck3 = new JCheckBox("Case-sensitive");
    caseCheck3.setSelected(_combo.isCaseSensitive());

    final JCheckBox correctCheck3 = new JCheckBox("Correct case");
    correctCheck3.setSelected(_combo.isCorrectingCase());
    correctCheck3.setEnabled(!caseCheck3.isSelected());
    correctCheck3.setToolTipText("Will change the case of the entered string to match the case of the matched string");
    
    caseCheck3.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent arg0)
      {
        _combo.setCaseSensitive(caseCheck3.isSelected());
        correctCheck3.setEnabled(!caseCheck3.isSelected());
        if (caseCheck3.isSelected())
        {
          correctCheck3.setSelected(false);
          _combo.setCorrectCase(false);
        }
      }
    });  
    
    correctCheck3.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent arg0)
      {
        _combo.setCorrectCase(correctCheck3.isSelected());
      }
    });
    
    panel.add(new JLabel("Auto-complete combo:"));
    panel.add(_combo);
    
    panel.add(Box.createGlue());
    panel.add(caseCheck3);

    panel.add(Box.createGlue());
    panel.add(correctCheck3);
        
    return panel;
  }
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    new MXCompleterTest();
  }
  
  private MXCompleterTextField _tf;
  private MXCompleterTextField _tfww;
  private MXCompleterComboBox  _combo;
}
