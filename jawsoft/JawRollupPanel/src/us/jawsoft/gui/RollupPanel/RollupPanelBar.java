package us.jawsoft.gui.RollupPanel;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import us.jawsoft.gui.JawRollupPanel;

 public class RollupPanelBar extends JPanel {
        private int vgap = 10;

        public RollupPanelBar() {
            //setLayout(new VerticalLayout(vgap));
            setLayout(new JawVerticalFlowLayout(10, true, false, false));
            setBorder(new EmptyBorder(10, 5, 10, 5));
            //setBackground(new Color(117, 151, 226)); // blue
            //setBackground(new Color(201,215,170)); // green
            //setBackground(new Color(217,123,162)); // red
            //setBackground(new Color(190,194,208)); // default
            setDoubleBuffered(true);
            
        }
        
        public void setBackground(Color bg) {
            super.setBackground(bg);
            
            for (int i = 0; i < this.getComponentCount(); i++) {
                Component j = this.getComponent(i);
                
                if (j instanceof JawRollupPanel) {
                    JawRollupPanel p = (JawRollupPanel)j;
                    p.setColorTitleBG(bg);
                    p.save();
                }
            }
            
        }

        public void add(JawRollupPanel panel) {
            super.add(panel);
        }
    }
    
