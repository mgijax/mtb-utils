package us.jawsoft.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import us.jawsoft.gui.RollupPanel.*;

public class JawRollupPanel extends JPanel implements MouseListener {
    //private JPanel pnlBody = new JPanel(new GridLayout(0, 1, 2, 2));
    private JPanel pnlBody = new JPanel();
    private JPanel pnlOuterBody = new JPanel();
    private JLabel lblTitle;
    private JLabel lblArrow;
    private TitlePanel titlePanel;
    
    private Dimension dimMenu;
    
    // title icon
    private ImageIcon iconUp = null;
    private ImageIcon iconUpRollover = null;
    private ImageIcon iconDown = null;
    private ImageIcon iconDownRollover = null;

    // title    
    private Font fontTitle = null;
    private Color colorTitleFG = null;//new Color(33, 93, 198);
    private Color colorTitleFGRollover = null;//new Color(66, 142, 255);
    private Color colorTitleBG = null;//new Color(33, 93, 198);
    private Color colorTitleBGStart = null;//new Color(33, 93, 198);
    private Color colorTitleBGFinish = null;//new Color(33, 93, 198);
    
    // menu
    private Font fontBody = null;
    private Color colorBodyFG = null;//new Color(33, 93, 198);
    private Color colorBodyBGStart = null;//new Color(33, 93, 198);
    private Color colorBodyBGFinish = null;//new Color(33, 93, 198);
    
    private final String RESOURCE_PATH = "us/jawsoft/gui/RollupPanel/resources/";
    
    private final String DEFAULT_UP_IMAGE = "us/jawsoft/gui/RollupPanel/resources/default_up.png";
    private final String DEFAULT_UP_ROLLOVER_IMAGE = "us/jawsoft/gui/RollupPanel/resources/default_up.png";
    private final String DEFAULT_DOWN_IMAGE = "us/jawsoft/gui/RollupPanel/resources/default_down.png";
    private final String DEFAULT_DOWN_ROLLOVER_IMAGE = "us/jawsoft/gui/RollupPanel/resources/default_down.png";

    final public static int MENU_SPEED_1 = 0;
    final public static int MENU_SPEED_2 = 1;
    final public static int MENU_SPEED_3 = 2;
    final public static int MENU_SPEED_4 = 3;
    final public static int MENU_SPEED_5 = 4;
    final public static int MENU_SPEED_6 = 5;
    final public static int MENU_SPEED_7 = 6;
    final public static int MENU_SPEED_8 = 7;
    final public static int MENU_SPEED_9 = 8;
    final public static int MENU_SPEED_10 = 9;
    final public static int MENU_SPEED_FAST = MENU_SPEED_10;
    final public static int MENU_SPEED_AVG = MENU_SPEED_5;
    final public static int MENU_SPEED_SLOW = MENU_SPEED_1;
    final private int MENU_SLEEP = 0;
    final private int MENU_STEP = 1;
    
    private final int[][] MENU_SPEED = 
        // sleep, step
        {{100, 1},  // slow
         {80, 3},
         {40, 5},
         {20, 8},
         {5, 10},   // avg
         {5, 13},
         {3, 15},
         {2, 18},
         {1, 20},
         {-1, -1}};  // fast
         
    
    private Color colorBorder = null;
    private int menuSpeed = MENU_SPEED_AVG;
    
    final public static int THEME_BLUE = 0;
    final public static int THEME_RED = 1;
    final public static int THEME_GREEN = 2;
    final public static int THEME_DEFAULT = 3;
    final public static int THEME_NONE = 4;
    
    private int currentTheme = THEME_NONE;
    
    public JawRollupPanel() {
        this(null, null, null);
    }
    
    public JawRollupPanel(String title) {
        this(title, null, null);
    }
    
    public JawRollupPanel(String title, ImageIcon iconUp, ImageIcon iconDown) {
        setLayout(new BorderLayout());
        
        if (iconUp != null) {
            this.iconUp = iconUp;
        } else {
            this.iconUp = new ImageIcon(loadIcon(DEFAULT_UP_IMAGE).getImage());    
        }
        
        if (iconDown != null) {
            this.iconDown = iconDown;
        } else {
            this.iconDown = new ImageIcon(loadIcon(DEFAULT_DOWN_IMAGE).getImage());
        }

        lblTitle = new JLabel(title);
        //lblTitle.setForeground(colorTitleFG);
        lblArrow = new JLabel(this.iconUp);
        
        titlePanel = new TitlePanel(new BorderLayout());
        titlePanel.setBorder(new EmptyBorder(2, 10, 2, 10));
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        titlePanel.add(lblArrow, BorderLayout.EAST);
        titlePanel.addMouseListener(this);
        titlePanel.setOpaque(true);
        
        pnlOuterBody.setLayout(new BorderLayout());
        pnlOuterBody.setDoubleBuffered(true);
        pnlOuterBody.setOpaque(true);
        //pnlOuterBody.setBorder(new LineBorder(Color.white));
        //pnlOuterBody.setBackground(new Color(214, 223, 247));
        
        pnlBody.setLayout(new JawVerticalFlowLayout(2, true, true, true));
        pnlBody.setBorder(new EmptyBorder(2, 10, 2, 10));
        //pnlBody.setBackground(new Color(214, 223, 247));
        pnlBody.setDoubleBuffered(true);
        pnlBody.setOpaque(true);
        
        add(titlePanel, BorderLayout.NORTH);
        
        pnlOuterBody.add(pnlBody, BorderLayout.CENTER);
        add(pnlOuterBody, BorderLayout.CENTER);

        dimMenu = pnlBody.getPreferredSize();

        setDoubleBuffered(true);
        //setBackground(new Color(214, 223, 247));
    }

    public Component add(Component comp) {
        Component c =  pnlBody.add(comp);
        dimMenu = pnlBody.getPreferredSize();
        debug("dimMenu.width=" + dimMenu.width);
        debug("dimMenu.height=" + dimMenu.height);
        return c;
    }
    
    public void update(Graphics g) {
        paint(g);
    }
    
    public void save() {
        if (fontTitle != null) {
            lblTitle.setFont(fontTitle);
        }
        
        if (colorTitleFG != null) {
            lblTitle.setForeground(colorTitleFG);
        }
        
        if (colorTitleBG != null) {
            titlePanel.setBackground(colorTitleBG);
        }
        
        if (colorTitleBGStart != null) {
            titlePanel.setColorStart(colorTitleBGStart);
        }
        
        if (colorTitleBGFinish != null) {
            titlePanel.setColorFinish(colorTitleBGFinish);
        }
        
        if (fontBody != null) {
            pnlBody.setFont(fontBody);
        }
        
        if (colorBodyFG != null) {
            pnlBody.setForeground(colorBodyFG);
        }
        
        if (colorBodyBGStart != null) {
            pnlBody.setBackground(colorBodyBGStart);
            pnlOuterBody.setBackground(colorBodyBGFinish);
        }

        if (colorBodyBGFinish != null) {
            pnlBody.setBackground(colorBodyBGFinish);
            pnlOuterBody.setBackground(colorBodyBGFinish);
        }
        
        if (colorBodyFG != null) {
            //pnlOuterBody.setBackground(colorBodyFG);
        }
        
        if (colorBorder != null) {
            pnlOuterBody.setBorder(new LineBorder(colorBorder));
        } else {
            pnlOuterBody.setBorder(new EmptyBorder(0,0,0,0));
        }
        
        lblArrow.setIcon(pnlBody.isVisible() ? iconUp : iconDown);
        
        
        updateUI();
    }
    
    
    
    public void mouseClicked(MouseEvent e) {
        boolean scrollingOn = true;
        
        if (menuSpeed != MENU_SPEED_FAST) {
            
            SwingWorker scroller = new SwingWorker() {
                public Object construct() {
                    
                    if (pnlBody.isVisible()) {
                        Dimension d = pnlBody.getSize();
                        int w = d.width;
                        int h = d.height;
                        
                        while (h >= MENU_SPEED[menuSpeed][MENU_STEP]) {
                            h = h-MENU_SPEED[menuSpeed][MENU_STEP];
                            try {
                                Thread.sleep(MENU_SPEED[menuSpeed][MENU_SLEEP]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pnlBody.setSize(w, h);
                            pnlBody.setPreferredSize(new Dimension(w, h));
                            pnlBody.setMaximumSize(new Dimension(w, h));
                            pnlBody.setMinimumSize(new Dimension(w, h));
                            revalidate();
                        }
                        
                        pnlBody.setSize(dimMenu);
                        pnlBody.setPreferredSize(dimMenu);
                        pnlBody.setMaximumSize(dimMenu);
                        pnlBody.setMinimumSize(dimMenu);
                        
                        
                        pnlBody.setVisible(false);
                    } else {
                        debug(pnlBody.getSize());
                        Dimension d = dimMenu;
                        int w = d.width;
                        int h = 1;
                        pnlBody.setSize(w, h);
                        pnlBody.setPreferredSize(new Dimension(w, h));
                        pnlBody.setMaximumSize(new Dimension(w, h));
                        pnlBody.setMinimumSize(new Dimension(w, h));
                        pnlBody.setVisible(true);
                        debug("pnlBody is not visible");
                        debug("d.width=" + d.width);
                        debug("d.height=" + d.height);
                        while (h <= (d.height - MENU_SPEED[menuSpeed][MENU_STEP])) {
                            debug("d.height=" + d.height);
                            debug("h=" + h);
                            
                            h = h+MENU_SPEED[menuSpeed][MENU_STEP];
                            try {
                                Thread.sleep(MENU_SPEED[menuSpeed][MENU_SLEEP]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pnlBody.setSize(w, h);
                            pnlBody.setPreferredSize(new Dimension(w, h));
                            pnlBody.setMaximumSize(new Dimension(w, h));
                            pnlBody.setMinimumSize(new Dimension(w, h));
                            revalidate();
                        }
                        
                        pnlBody.setSize(dimMenu);
                        pnlBody.setPreferredSize(dimMenu);
                        pnlBody.setMaximumSize(dimMenu);
                        pnlBody.setMinimumSize(dimMenu);
                        
                    
                    }
                    
                    
                    lblArrow.setIcon(pnlBody.isVisible() ? iconUp : iconDown);
                        
                    
                
                    
                    
                    return null;
            }

                /**
                 * Make the label look empty, like it does initially
                 */
                public void finished() {
                    debug("Done-------");
                }
            };
            scroller.start();        
            
        } else {
            pnlBody.setVisible(!pnlBody.isVisible());
            lblArrow.setIcon(pnlBody.isVisible() ? iconUp : iconDown);
        }
    }

    public void mouseEntered(MouseEvent e) {
        lblTitle.setForeground(colorTitleFGRollover);
    }

    public void mouseExited(MouseEvent e) {
        lblTitle.setForeground(colorTitleFG);
    }

    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }

    public void setFontTitle(Font f) {
        this.fontTitle = f;
    }
    
    public Font getFontTitle() {
        return this.fontTitle;
    }
    
    public void setColorTitleFG(Color c) {
        this.colorTitleFG = c;
    }
    
    public Color getColorTitleFG() {
        return this.colorTitleFG;
    }

    public void setColorTitleFGRollover(Color c) {
        this.colorTitleFGRollover = c;
    }
    
    public Color getColorTitleFGRollover() {
        return this.colorTitleFGRollover;
    }

    public void setColorTitleBG(Color c) {
        this.colorTitleBG = c;
    }
    
    public Color getColorTitleBG() {
        return this.colorTitleBG;
    }
    
    public void setColorTitleBGStart(Color c) {
        this.colorTitleBGStart = c;
    }
    
    public Color getColorTitleBGStart() {
        return this.colorTitleBGStart;
    }

    public void setColorTitleBGFinish(Color c) {
        this.colorTitleBGFinish = c;
    }
    
    public Color getColorTitleBGFinish() {
        return this.colorTitleBGFinish;
    }

    public void setFontBody(Font f) {
        this.fontBody = f;
    }
    
    public Font getFontBody() {
        return this.fontBody;
    }

    public void setColorBodyFG(Color c) {
        this.colorBodyFG = c;
    }
    
    public Color getColorBodyFG() {
        return this.colorBodyFG;
    }

    public void setColorBodyBGStart(Color c) {
        this.colorBodyBGStart = c;
    }
    
    public Color getColorBodyBGStart() {
        return this.colorBodyBGStart;
    }

    public void setColorBodyBGFinish(Color c) {
        this.colorBodyBGFinish = c;
    }
    
    public Color getColorBodyBGFinish() {
        return this.colorBodyBGFinish;
    }

    public void setColorBorder(Color c) {
        this.colorBorder = c;
    }
    
    public Color getColorBorder() {
        return this.colorBorder;
    }

    public void setMenuSpeed(int speed) {
        if (speed < MENU_SPEED_1) {
            this.menuSpeed = MENU_SPEED_1;
        } else if (speed > MENU_SPEED_10) {
            this.menuSpeed = MENU_SPEED_10;
        } else {
            this.menuSpeed = speed;
        }
    }
    
    public int getMenuSpeed() {
        return this.menuSpeed;
    }
    
    public void setIconUp(ImageIcon icon) {
        this.iconUp = icon;
    }
    
    public ImageIcon getIconUp() {
        return this.iconUp;
    }
    
    public void setIconDown(ImageIcon icon) {
        this.iconDown = icon;
    }
    
    public ImageIcon getIconDown() {
        return this.iconDown;
    }
    
    
    
    public void setTheme(int theme) {
        if (theme == THEME_BLUE) {
            this.currentTheme = theme;
            loadTheme("blue");
        } else if (theme == THEME_RED) {
            this.currentTheme = theme;
            loadTheme("red");
        } else if (theme == THEME_GREEN) {
            this.currentTheme = theme;
            loadTheme("green");
        } else if (theme == THEME_DEFAULT) {
            this.currentTheme = theme;
            loadTheme("default");
        } else {
            this.currentTheme = THEME_NONE;
        }
    }
    
    private Color stringToColor(String str) {
        Color color = null;
        
        try {
            int commaPos = str.indexOf(',');
            int r = Integer.parseInt(str.substring(0, commaPos));
            str = str.substring(commaPos + 1);
            commaPos = str.indexOf(',');
            int g = Integer.parseInt(str.substring(0, commaPos));
            int b = Integer.parseInt(str.substring(commaPos + 1));
            
            color = new Color(r, g, b);
        } catch (Exception e) {
        }
        
        return color;
    }
    
    private int stringToInt(String str) {
        try {
            int i = Integer.parseInt(str);
            return i;
        } catch (Exception e) {
        }
        
        return 0;
    }
    
    
    private void loadTheme(String theme) {
        try {
            String path = "/us/jawsoft/gui/RollupPanel/resources/" + theme + ".properties";
            InputStream in = getClass().getResourceAsStream(path);
            Properties properties = new Properties();
            properties.load(in);
            in.close();
            
            setColorTitleFG(stringToColor(properties.getProperty("COLOR_TITLE_FG")));
            setColorTitleFGRollover(stringToColor(properties.getProperty("COLOR_TITLE_FG_ROLLOVER")));
            //setColorTitleBG(stringToColor(properties.getProperty("COLOR_TITLE_BG")));
            setColorTitleBGStart(stringToColor(properties.getProperty("COLOR_TITLE_BG_START")));
            setColorTitleBGFinish(stringToColor(properties.getProperty("COLOR_TITLE_BG_FINISH")));
            setColorBodyFG(stringToColor(properties.getProperty("COLOR_BODY_FG")));
            setColorBodyBGStart(stringToColor(properties.getProperty("COLOR_BODY_BG_START")));
            setColorBodyBGFinish(stringToColor(properties.getProperty("COLOR_BODY_BG_FINISH")));
            setColorBorder(stringToColor(properties.getProperty("COLOR_BORDER")));
            setMenuSpeed(stringToInt(properties.getProperty("MENU_SPEED")));
            
            setIconUp(loadIcon(RESOURCE_PATH + theme + "_up.png"));
            setIconDown(loadIcon(RESOURCE_PATH + theme + "_down.png"));
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        e.printStackTrace();
        }
    }
    

    private ImageIcon loadIcon(String pathname) {     
        URL url = JawRollupPanel.class.getClassLoader().getResource(pathname);    
        
        if (url != null) {
            return new ImageIcon(url);		
        }
        
        System.err.println("Util.loadIcon Error: Could not find icon '" + pathname + "'");    
        
        return null;  
    }
    
        /**
         * Switches anti-aliasing on, paints, and switches it off again.
         * 
         * @param g   the Graphics object to render on
         */
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            super.paint(g2d);
        }
        
        
        private void debug(Object o) {
        //System.out.println(o.toString());
    }
}