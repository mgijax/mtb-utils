package us.jawsoft.gui.RollupPanel;

import java.awt.*;
import javax.swing.JPanel;
import java.awt.geom.RoundRectangle2D.Double;
import java.awt.geom.*;

public class TitlePanel extends JPanel {
    private Color colorStart = null;
    private Color colorFinish = null; //new Color(200, 212, 247);
    
    public TitlePanel(LayoutManager layout) {
        this(layout, null);
    }

    public TitlePanel(LayoutManager layout, Color c) {
        this(layout, c, null);
    }

    public TitlePanel(LayoutManager layout, Color cStart, Color cFinish) {
        super(layout);
        setColorStart(cStart);
        setColorFinish(cFinish);
        //setOpaque(true);
    }
    
    public void setColorStart(Color c) {
        if (c == null) {
            this.colorStart = this.getBackground();
        } else {
            this.colorStart = c;
        }
    }
    
    public Color getColorStart() {
        return this.colorStart;
    }
    
    public void setColorFinish(Color c) {
        if (c == null) {
            this.colorFinish = this.getBackground();
        } else {
            this.colorFinish = c;
        }
    }
    
    public Color getColorFinish() {
        return this.colorFinish;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        GradientPaint gradient = new GradientPaint(20, 0, colorStart, getSize().width - 10, getSize().height, colorFinish, true);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setPaint(gradient);
        //g2d.fill(new Rectangle(getSize().width, getSize().height));
        g2d.fill(new Rectangle(0, 10, getSize().width, getSize().height));
        g2d.fill(new RoundRectangle2D.Double(0, 0, getSize().width, getSize().height, 10, 10));
    }
}
