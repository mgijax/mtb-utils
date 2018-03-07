/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXTypeWriterPanel.java,v 1.1 2008/08/01 12:23:18 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JComponent;

/**
 * An animated typewriter component.
 *
 * @author $Author: sbn $
 * @date $Date: 2008/08/01 12:23:18 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/MXTypeWriterPanel.java,v 1.1 2008/08/01 12:23:18 sbn Exp $
 */
public class MXTypeWriterPanel extends JComponent implements Runnable  {

    // -------------------------------------------------------------- Constants

    private final String ERROR_THREAD_SLEEP =
            "Error - The thread failed to sleep";

    // ----------------------------------------------------- Instance Variables

    private boolean openConnection;
    private boolean play;
    private boolean antialias = false;
    private boolean running;
    private boolean indexOf;
    private int j;
    private int k;
    private int i;
    private int cWidth;
    private int cHeight;
    private int getSize;
    private int getsound;
    private int hasMoreElements;
    private int hasMoreTokens;
    private int hexconvert;
    private int connect[];
    private int drawRect[];
    private int drawString[];
    private int equalsIgnoreCase;
    private int borderWidth;
    private int speed;
    private int pause;
    private int alignment;
    private String startupText;
    private String nextElement;
    private String append[][];
    private String urlData;
    private String characterSet;
    private Color lastIndexOf;
    private Color colorArray[];
    private Color colorBG;
    private Color colorBorder;
    private Color colorFG;
    private ArrayList addMouseListener;
    private Font fontArray[];
    private Font font;
    private AudioClip soundTyping;
    private AudioClip soundEOL;
    private AudioClip soundScroll;
    private Thread getMessage;
    private FontMetrics parseInt;

    // ----------------------------------------------------------- Constructors

    /**
     * Constructor with width and height specified.
     *
     * @param width the width
     * @param height the height
     */
    public MXTypeWriterPanel(int width, int height) {
        running = false;
        openConnection = false;
        play = false;
        cWidth = width;
        cHeight = height;
        this.setSize(width, height);
        this.setPreferredSize(new Dimension(width,height));
        this.setMaximumSize(new Dimension(width,height));
        this.setMinimumSize(new Dimension(width,height));

        init();
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Get the value of the paramater.
     *
     * @param str the name of the parameter
     * @return the value of the parameter
     */
    public String getParameter(String str) {
        return "";
    }

    /**
     * Get the startup text.
     *
     * @return the startup text
     */
    public final String getStartupText() {
        return this.startupText;
    }

    /**
     * Set the startup text.
     *
     * @param text the startup text
     */
    public final void setStartupText(final String text) {
        this.startupText = text;
    }

    /**
     * Get the character set.
     *
     * @return the character set
     */
    public final String getCharacterSet() {
        return this.characterSet;
    }

    /**
     * Set the character set.
     *
     * @param set the character set
     */
    public final void setCharacterSet(final String set) {
        this.characterSet = set;
    }

    /**
     * Get the background color.
     *
     * @return the background color
     */
    public final Color getColorBG() {
        return this.colorBG;
    }

    /**
     * Set the background color.
     *
     * @param color the background color
     */
    public final void setColorBG(Color color) {
        this.colorBG = color;
    }

    /**
     * Get the foreground color.
     *
     * @return the foreground color
     */
    public final Color getColorFG() {
        return this.colorFG;
    }

    /**
     * Set the foreground color.
     *
     * @param color the foreground color
     */
    public final void setColorFG(Color color) {
        this.colorFG = color;
    }

    /**
     * Get the border color.
     *
     * @return the border color
     */
    public final Color getColorBorder() {
        return this.colorBorder;
    }

    /**
     * Set the border color.
     *
     * @param color the border color
     */
    public final void setColorBorder(Color color) {
        this.colorBorder = color;
    }

    /**
     * Get the speed of the menu.
     *
     * @return the speed of the menu
     */
    public final int getSpeed() {
        return this.speed;
    }

    /**
     * Set the speed of the menu.
     *
     * @param speed the speed of the menu
     */
    public final void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Get the amount of time to pause.
     *
     * @return the amount of time to pause
     */
    public final int getPause() {
        return this.pause;
    }

    /**
     * Set the amount of time to pause.
     *
     * @param pause the amount of time to pause
     */
    public final void setPause(int pause) {
        this.pause = pause;
    }

    /**
     * Initialize the local variables.
     */
    public void init() {
        openConnection = false;
        cWidth = getSize().width;
        cHeight = getSize().height;
        characterSet = verifystr(getParameter("charset"), "8859_1");
        startupText = verifystr(getParameter("startupmessage"),
                "Loading data, please wait....");
        colorBG = verifyclr(getParameter("BGcolor"), "33,72,152", ",");
        colorFG = verifyclr(getParameter("TextColor"), "0,0,0", ",");
        colorBorder = verifyclr(getParameter("BorderColor"), "33,72,152", ",");
        borderWidth = verifyint(getParameter("BorderWidth"));
        font = verifyfnt(getParameter("Font"), "Verdana,N,10", ",");
        alignment = verifyalign(getParameter("align"), "left");
        soundTyping = getsound(verifystr(getParameter("typeSound"), " "));
        soundEOL = getsound(verifystr(getParameter("eolSound"), " "));
        soundScroll = getsound(verifystr(getParameter("scrollSound"), " "));
        speed = 20;
        pause = (int)Math.round(verifydbl(getParameter("Pause"))) * 1000;
        if(speed < 5) {
            speed = 5;
        }
        if(speed > 105) {
            speed = 105;
        }
        urlData = verifystr(getParameter("dataURL"), " ");
        parse(urlData);
        openConnection = true;
    }

    /**
     * Start the animation.
     */
    public void startAnimation() {
        getMessage = new Thread(this);
        running = true;
        getMessage.start();
    }

    /**
     * Stop the animation.
     */
    public final void stopAnimation() {
        running = false;
    }

    /**
     * Run the animation.
     */
    public final void run() {
        getSize = 0;
        getsound = 0;
        hasMoreElements = 0;
        hasMoreTokens = 0;
        indexOf = false;
        int j1 = colorBG.getBlue();
        int k1 = colorBG.getRed();
        int l1 = colorBG.getGreen();
        while(running) {
            for(getSize = 0; getSize < equalsIgnoreCase; getSize++) {
                lastIndexOf = colorArray[getSize];
                parseInt = getFontMetrics(fontArray[getSize]);
                hexconvert = parseInt.getHeight();

                for(getsound = 0; getsound < connect[getSize]; getsound++) {
                    for(hasMoreElements = 0;
                        hasMoreElements < append[getSize][getsound].length();
                        hasMoreElements++) {

                        indexOf = !indexOf;
                        repaint();
                        try {
                            Thread.sleep(2 * speed);
                        } catch(InterruptedException ie) {
                            //Utils.log(ERROR_THREAD_SLEEP);
                        }
                    }

                    if(soundEOL != null) {
                        soundEOL.play();
                    }
                    try {
                        Thread.sleep(10 * speed);
                    } catch(InterruptedException ie) {
                        //Utils.log(ERROR_THREAD_SLEEP);
                    }
                    if(soundScroll != null) {
                        soundScroll.loop();
                    }
                    indexOf = false;

                    for(hasMoreTokens = 0;
                        hasMoreTokens < hexconvert;
                        hasMoreTokens++) {

                        repaint();
                        if(soundScroll != null) {
                            soundScroll.play();
                        }
                        try {
                            Thread.sleep(speed / 5);
                        } catch(InterruptedException ie) {
                            //Utils.log(ERROR_THREAD_SLEEP);
                        }
                    }

                    if(soundScroll != null) {
                        soundScroll.stop();
                    }
                    hasMoreTokens = 0;
                }

                hasMoreElements = append[getSize][getsound].length();
                repaint();
                try {
                    Thread.sleep(drawRect[getSize]);
                } catch(InterruptedException ie) {
                    //Utils.log(ERROR_THREAD_SLEEP);
                }
                for(int i1 = 0; i1 < 100; i1++) {
                    int j2 = colorArray[getSize].getBlue();
                    int l2 = colorArray[getSize].getRed();
                    int j3 = colorArray[getSize].getGreen();
                    lastIndexOf = new Color(l2 + (i1 * (k1 - l2)) / 100,
                                            j3 + (i1 * (l1 - j3)) / 100,
                                            j2 + (i1 * (j1 - j2)) / 100);
                    repaint();
                    try {
                        Thread.sleep(speed / 5);
                    } catch(InterruptedException ie) {
                        //Utils.log(ERROR_THREAD_SLEEP);
                    }
                }
            }
        }
    }

    /**
     * Paint the appropriate text to the screen.
     *
     * @param g the graphics context
     */
    public final void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        // Enable antialiasing for text
        if (antialias) {
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                 RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        if(openConnection && !play) {
            g2d.setColor(colorBG);
            g2d.fillRect(0, 0, cWidth, cHeight);
            g2d.setFont(fontArray[getSize]);
            parseInt = getFontMetrics(fontArray[getSize]);
            g2d.setColor(lastIndexOf);

            int i1 = borderWidth + 2;
            int j1 = cHeight - borderWidth - 8 -
                    (getsound + 1) * hexconvert - hasMoreTokens;

            for(i = 0; i < getsound + 1; i++) {
                j1 += hexconvert;
                if(i == getsound) {
                    nextElement =
                            append[getSize][i].substring(0, hasMoreElements);

                    if(nextElement == null) {
                        nextElement = " ";
                    }

                    if(drawString[getSize] == 1) {
                        i1 = cWidth / 2 -
                             parseInt.stringWidth(nextElement) / 2;
                    }

                    if(drawString[getSize] == 2) {
                        i1 = cWidth - parseInt.stringWidth(nextElement) -
                             borderWidth - 2;
                    }

                    g2d.drawString(nextElement, i1, j1);

                    if(indexOf) {
                        i1 += parseInt.stringWidth(nextElement);
                        g2d.fillRect(i1, j1 - hexconvert / 2,
                                     hexconvert / 2, hexconvert / 2);

                        if(soundTyping != null) {
                            soundTyping.play();
                        }
                    }
                } else {
                    if(drawString[getSize] == 1) {
                        i1 = cWidth / 2 -
                             parseInt.stringWidth(append[getSize][i]) / 2;
                    }
                    if(drawString[getSize] == 2) {
                        i1 = cWidth -
                             parseInt.stringWidth(append[getSize][i]) -
                                                  borderWidth - 2;
                    }
                    g2d.drawString(append[getSize][i], i1, j1);
                }
            }

            g2d.setColor(colorBorder);

            for(i = 0; i < borderWidth; i++) {
                g2d.drawRect(i, i, cWidth - 2 * i - 1, cHeight - 2 * i - 1);
            }

        } else {
            if(play) {
                g2d.setColor(new Color(255, 255, 255));
                g2d.fillRect(20, 15, 300, 50);
                g2d.setColor(new Color(200, 30, 30));
                g2d.setFont(new Font("Helvetica", 1, 12));
                g2d.drawString("Error Loading Data File:", 30, 30);
                g2d.drawString(urlData, 30, 45);
                g2d.drawString("Check parameter 'dataURL'", 30, 60);
            } else {
                // startup text rendering
                g2d.setColor(colorBG);
                g2d.fillRect(0, 0, getSize().width, getSize().height);
                g2d.setColor(colorFG);
                g2d.setFont(new Font("Helvetica", 0, 10));
                g2d.drawString(startupText, 30, 30);
            }
        }
    }

    /**
     * Parse the data in the text file.
     *
     * @param s the location of the text file
     */
    private void parse(String s) {
        URL url = null;
        addMouseListener = new ArrayList();
        try {
            url = getClass().getResource("/data.txt");
        } catch(Exception e) {
            //Utils.log("Bad URL for File Location : " + s);
        }
        try {
            URLConnection urlc = url.openConnection();
            urlc.connect();
            BufferedReader bufferedreader =
                    new BufferedReader(
                            new InputStreamReader(urlc.getInputStream(),
                                                  characterSet));

            while((nextElement = bufferedreader.readLine()) != null) {
                addMouseListener.add(nextElement);
            }

            equalsIgnoreCase = 0;
            int i1 = 0;
            int j1 = 0;
            for(int i = 0; i < addMouseListener.size(); i++) {
                nextElement = (String)addMouseListener.get(i);
                if(nextElement.indexOf("<MESSAGE>") > -1) {
                    equalsIgnoreCase++;
                    if(j1 > i1) {
                        i1 = j1;
                    }
                    j1 = 0;
                }
                if(nextElement.indexOf("<MESSAGE>") == -1 &&
                   nextElement.indexOf("<FONT") == -1 &&
                   nextElement.indexOf("<URL") == -1 &&
                   nextElement.indexOf("<COLOR") == -1 &&
                   nextElement.indexOf("<PAUSE") == -1 &&
                   nextElement.indexOf("<ALIGN") == -1 &&
                   nextElement.indexOf("</MESSAGE") == -1) {
                    j1++;
                }
            }

            if(j1 > i1) {
                i1 = j1;
            }
            i1++;
            append = new String[equalsIgnoreCase][i1];
            connect = new int[equalsIgnoreCase];
            fontArray = new Font[equalsIgnoreCase];
            colorArray = new Color[equalsIgnoreCase];
            drawRect = new int[equalsIgnoreCase];
            drawString = new int[equalsIgnoreCase];
            for(i = 0; i < equalsIgnoreCase; i++) {
                for(j = 0; j < i1; j++) {
                    append[i][j] = " ";
                }

                connect[i] = 0;
                fontArray[i] = font;
                colorArray[i] = colorFG;
                drawRect[i] = pause;
                drawString[i] = alignment;
            }

            equalsIgnoreCase = -1;
            boolean flag = false;
            for (int i = 0; i < addMouseListener.size(); i++) {
                nextElement = (String)addMouseListener.get(i);
                if(nextElement.indexOf("<MESSAGE>") > -1) {
                    equalsIgnoreCase++;
                    j1 = 0;
                    flag = true;
                }
                if(nextElement.indexOf("</MESSAGE>") > -1) {
                    connect[equalsIgnoreCase] = j1;
                    flag = false;
                }
                if(nextElement.indexOf("<FONT") > -1) {
                    j = nextElement.indexOf("<FONT");
                    k = nextElement.indexOf('>');
                    fontArray[equalsIgnoreCase] =
                            verifyfnt(nextElement.substring(j + 6, k),
                                      "Courier,N,12", ",");
                }
                if(nextElement.indexOf("<URL") > -1) {
                    j = nextElement.indexOf("<URL");
                    k = nextElement.indexOf('>');
                    //nextToken = nextElement.substring(j + 5, k);
                }
                if(nextElement.indexOf("<COLOR") > -1) {
                    j = nextElement.indexOf("<COLOR");
                    k = nextElement.indexOf('>');
                    colorArray[equalsIgnoreCase] =
                            verifyclr(nextElement.substring(j + 7, k),
                                      "#000000", ",");
                }
                if(nextElement.indexOf("<PAUSE") > -1) {
                    j = nextElement.indexOf("<PAUSE");
                    k = nextElement.indexOf('>');
                    drawRect[equalsIgnoreCase] =
                            (int)Math.round(verifydbl(
                                nextElement.substring(j + 7, k))) * 1000;
                }

                if(nextElement.indexOf("<ALIGN") > -1) {
                    j = nextElement.indexOf("<ALIGN");
                    k = nextElement.indexOf('>');
                    drawString[equalsIgnoreCase] =
                            verifyalign(nextElement.substring(j + 7, k),
                                        "LEFT");
                }

                if(nextElement.indexOf("<MESSAGE>") == -1 &&
                   nextElement.indexOf("<FONT") == -1 &&
                   nextElement.indexOf("<URL") == -1 &&
                   nextElement.indexOf("<COLOR") == -1 &&
                   nextElement.indexOf("<PAUSE") == -1 &&
                   nextElement.indexOf("<ALIGN") == -1 &&
                   nextElement.indexOf("</MESSAGE") == -1 &&
                   flag) {
                    append[equalsIgnoreCase][j1] = nextElement;
                    j1++;
                }
            }

            equalsIgnoreCase++;
        } catch(IOException ioexception) {
            //Utils.log("IO Error:" + ioexception.getMessage());
            play = true;
            repaint();
        }
    }

    /**
     * Return the value of the alignment.
     *
     * @param s the value
     * @param s1 the default value
     * @return 0 for left, 1 for center, 2 for right
     */
    public static final int verifyalign(String s, String s1) {
        byte byte0 = 0;
        String s2 = " ";

        if(s == null || s.equals("")) {
            s = s1;
        }

        try {
            s2 = s.trim();
        } catch(Exception exception) {
            s2 = "LEFT";
        }

        if(s2.equalsIgnoreCase("left")) {
            byte0 = 0;
        } else if(s2.equalsIgnoreCase("center")) {
            byte0 = 1;
        } else if (s2.equalsIgnoreCase("right")) {
            byte0 = 2;
        }

        return byte0;
    }

    /**
     * A method like nvl in SQL.
     *
     * @param s the value
     * @param s1 the default value
     * @return s1 if s is null, or " " if both are null
     */
    public static final String verifystr(String s, String s1) {
        String s2 = " ";
        if(s == null || s.equals("")) {
            s2 = s1;
        } else {
            s2 = s;
        }
        return s2;
    }

    /**
     * Verify the value of the font.
     *
     * @param s the value in the format of FONT|WEIGHT|SIZE
     * @param s1 the default value in the format of FONT|WEIGHT|SIZE
     * @param s2 the separator
     * @return the font specifed by the string
     */
    public static final Font verifyfnt(String s, String s1, String s2) {
        String s3 = "Courier";
        String s4 = "N";
        int i1 = 12;
        String s5;
        try {
            s5 = s.trim();
        } catch(Exception exception) {
            s5 = "Courier|N|10";
        }
        String s6 = verifystr(s5, s1);
        StringTokenizer stringtokenizer = new StringTokenizer(s6, s2);
        Font font;
        try {
            if(stringtokenizer.hasMoreTokens()) {
                s3 = stringtokenizer.nextToken();
            }
            if(stringtokenizer.hasMoreTokens()) {
                s4 = stringtokenizer.nextToken().toUpperCase();
            }
            if(stringtokenizer.hasMoreTokens()) {
                i1 = Integer.parseInt(stringtokenizer.nextToken());
            }
            if("B".equals(s4)) {
                font = new Font(s3, 1, i1);
            } else
                if("I".equals(s4)) {
                font = new Font(s3, 2, i1);
                } else
                    if("BI".equals(s4) || "IB".equals(s4)) {
                font = new Font(s3, 3, i1);
                    } else {
                font = new Font(s3, 0, i1);
                    }
        } catch(Exception exception1) {
            font = new Font("Courier", 0, 12);
        }
        return font;
    }

    /**
     * Verify the color.
     *
     * @param s the value
     * @param s1 the default value
     * @param s2 the separator
     * @return the color
     */
    public static final Color verifyclr(String s, String s1, String s2) {
        int i1 = 0;
        int k1 = 0;
        int i2 = 0;
        if(s == null || s.equals("")) {
            s = s1;
        }
        String s3;
        try {
            s3 = s.trim();
        } catch(Exception exception) {
            s3 = "0" + s2 + "0" + s2 + "0";
        }
        String s4 = verifystr(s3, s1);
        Color color = new Color(0, 0, 0);
        StringTokenizer stringtokenizer = new StringTokenizer(s4, s2);
        try {
            if(stringtokenizer.hasMoreTokens()) {
                i1 = Math.abs(Integer.parseInt(stringtokenizer.nextToken()));
            }
            if(stringtokenizer.hasMoreTokens()) {
                k1 = Math.abs(Integer.parseInt(stringtokenizer.nextToken()));
            }
            if(stringtokenizer.hasMoreTokens()) {
                i2 = Math.abs(Integer.parseInt(stringtokenizer.nextToken()));
            }
            color = new Color(i1, k1, i2);
        } catch(Exception exception1) {
            color = new Color(0, 0, 0);
        }
        if(s4.charAt(0) == '#') {
            int j1 = hexconvert(s4.substring(1, 3));
            int l1 = hexconvert(s4.substring(3, 5));
            int j2 = hexconvert(s4.substring(5, 7));
            color = new Color(j1, l1, j2);
        }
        if(s4.equalsIgnoreCase("red")) {
            color = new Color(255, 0, 0);
        }
        if(s4.equalsIgnoreCase("light red")) {
            color = new Color(255, 75, 75);
        }
        if(s4.equalsIgnoreCase("dark red")) {
            color = new Color(100, 0, 0);
        }
        if(s4.equalsIgnoreCase("green")) {
            color = new Color(0, 255, 0);
        }
        if(s4.equalsIgnoreCase("light green")) {
            color = new Color(75, 255, 75);
        }
        if(s4.equalsIgnoreCase("dark green")) {
            color = new Color(0, 100, 0);
        }
        if(s4.equalsIgnoreCase("blue")) {
            color = new Color(0, 0, 255);
        }
        if(s4.equalsIgnoreCase("light blue")) {
            color = new Color(75, 75, 255);
        }
        if(s4.equalsIgnoreCase("dark blue")) {
            color = new Color(0, 0, 100);
        }
        if(s4.equalsIgnoreCase("orange")) {
            color = new Color(255, 128, 0);
        }
        if(s4.equalsIgnoreCase("light orange")) {
            color = new Color(255, 175, 100);
        }
        if(s4.equalsIgnoreCase("dark orange")) {
            color = new Color(208, 104, 0);
        }
        if(s4.equalsIgnoreCase("yellow")) {
            color = new Color(255, 255, 0);
        }
        if(s4.equalsIgnoreCase("light yellow")) {
            color = new Color(255, 255, 100);
        }
        if(s4.equalsIgnoreCase("dark yellow")) {
            color = new Color(200, 200, 0);
        }
        if(s4.equalsIgnoreCase("pink")) {
            color = new Color(255, 128, 128);
        }
        if(s4.equalsIgnoreCase("light pink")) {
            color = new Color(255, 172, 172);
        }
        if(s4.equalsIgnoreCase("dark pink")) {
            color = new Color(255, 77, 77);
        }
        if(s4.equalsIgnoreCase("purple")) {
            color = new Color(255, 0, 255);
        }
        if(s4.equalsIgnoreCase("light purple")) {
            color = new Color(255, 100, 255);
        }
        if(s4.equalsIgnoreCase("dark purple")) {
            color = new Color(140, 0, 140);
        }
        if(s4.equalsIgnoreCase("grey")) {
            color = new Color(127, 127, 127);
        }
        if(s4.equalsIgnoreCase("light grey")) {
            color = new Color(200, 200, 200);
        }
        if(s4.equalsIgnoreCase("dark grey")) {
            color = new Color(50, 50, 50);
        }
        if(s4.equalsIgnoreCase("black")) {
            color = new Color(0, 0, 0);
        }
        if(s4.equalsIgnoreCase("white")) {
            color = new Color(255, 255, 255);
        }
        return color;
    }

    /**
     * Convert a hexidecimal string value to an integer.
     *
     * @param s the hexidecimal value
     * @return the integer equivalent
     */
    public static final int hexconvert(String s) {
        String s1 = "00";
        try {
            s1 = s.trim();
        } catch(Exception exception) {
            s1 = "00";
        }
        if(s1.length() < 2) {
            s1 = "00";
        }
        byte byte0 = 0;
        byte byte1 = 0;
        if(s1.substring(0, 1).equalsIgnoreCase("0")) {
            byte0 = 0;
        }
        if(s1.substring(0, 1).equalsIgnoreCase("1")) {
            byte0 = 1;
        }
        if(s1.substring(0, 1).equalsIgnoreCase("2")) {
            byte0 = 2;
        }
        if(s1.substring(0, 1).equalsIgnoreCase("3")) {
            byte0 = 3;
        }
        if(s1.substring(0, 1).equalsIgnoreCase("4")) {
            byte0 = 4;
        }
        if(s1.substring(0, 1).equalsIgnoreCase("5")) {
            byte0 = 5;
        }
        if(s1.substring(0, 1).equalsIgnoreCase("6")) {
            byte0 = 6;
        }
        if(s1.substring(0, 1).equalsIgnoreCase("7")) {
            byte0 = 7;
        }
        if(s1.substring(0, 1).equalsIgnoreCase("8")) {
            byte0 = 8;
        }
        if(s1.substring(0, 1).equalsIgnoreCase("9")) {
            byte0 = 9;
        }
        if(s1.substring(0, 1).equalsIgnoreCase("A")) {
            byte0 = 10;
        }
        if(s1.substring(0, 1).equalsIgnoreCase("B")) {
            byte0 = 11;
        }
        if(s1.substring(0, 1).equalsIgnoreCase("C")) {
            byte0 = 12;
        }
        if(s1.substring(0, 1).equalsIgnoreCase("D")) {
            byte0 = 13;
        }
        if(s1.substring(0, 1).equalsIgnoreCase("E")) {
            byte0 = 14;
        }
        if(s1.substring(0, 1).equalsIgnoreCase("F")) {
            byte0 = 15;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("0")) {
            byte1 = 0;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("1")) {
            byte1 = 1;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("2")) {
            byte1 = 2;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("3")) {
            byte1 = 3;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("4")) {
            byte1 = 4;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("5")) {
            byte1 = 5;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("6")) {
            byte1 = 6;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("7")) {
            byte1 = 7;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("8")) {
            byte1 = 8;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("9")) {
            byte1 = 9;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("A")) {
            byte1 = 10;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("B")) {
            byte1 = 11;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("C")) {
            byte1 = 12;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("D")) {
            byte1 = 13;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("E")) {
            byte1 = 14;
        }
        if(s1.substring(1, 2).equalsIgnoreCase("F")) {
            byte1 = 15;
        }
        return 16 * byte0 + byte1;
    }

    /**
     * Verify the integer value.
     *
     * @param s the value to verify
     * @return the value of s as an integer, or 0 if it cannot be determined
     */
    public static final int verifyint(String s) {
        String s1 = "0";
        try {
            s1 = s.trim();
        } catch(Exception exception) {
            s1 = "0";
        }
        int i1;
        try {
            i1 = Integer.parseInt(s1);
        } catch(Exception exception1) {
            i1 = 0;
        }
        return i1;
    }

    /**
     * Verify the double value.
     *
     * @param s the value to verify
     * @return the value of s as a douoble, or 0.0 if it cannot be determined
     */
    public static final double verifydbl(String s) {
        String s1 = "0.0";
        try {
            s1 = s.trim();
        } catch(Exception exception) {
            s1 = "0.0";
        }
        double d;
        try {
            d = Double.valueOf(s1).floatValue();
        } catch(Exception exception1) {
            d = 0.0D;
        }
        return d;
    }

    /**
     * Get the soundclip.  Currently does nothing.
     *
     * @param s the path to the soundclip
     * @return the AudioClip
     */
    public final AudioClip getsound(String s) {
        AudioClip audioclip = null;
        /*
        try {
            audioclip = getAudioClip(getDocumentBase(), s);
        } catch(Exception exception) {
            audioclip = null;
        }
         */
        return audioclip;
    }

    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
}
