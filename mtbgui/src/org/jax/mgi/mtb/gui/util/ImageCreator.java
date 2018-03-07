/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/util/ImageCreator.java,v 1.1 2008/08/01 12:32:27 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui.util;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.StringTokenizer;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Provides utility functions for creating <code>ImageIcon</code> s and
 * <code>BufferedImage</code> s for the GUI.
 *
 * @author $Author: sbn $
 * @author Kirill Grouchnikov
 * @date $Date: 2008/08/01 12:32:27 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/util/ImageCreator.java,v 1.1 2008/08/01 12:32:27 sbn Exp $
 */
public final class ImageCreator {

    // -------------------------------------------------------------- Constants

    /**
     * The main light color for the backgrounds.
     */
    public static final Color mainUUltraLightColor = new Color(196, 220, 255);

    /**
     * The main light color for the backgrounds.
     */
    public static final Color mainUltraLightColor = new Color(128, 192, 255);

    /**
     * The main light color for the backgrounds.
     */
    public static final Color mainLightColor = new Color(0, 128, 255);

    /**
     * The main medium color for the backgrounds.
     */
    public static final Color mainMidColor = new Color(0, 64, 196);

    /**
     * The main dark color for the backgrounds.
     */
    public static final Color mainDarkColor = new Color(0, 32, 128);

    /**
     * The main ultra-dark color for the backgrounds.
     */
    public static final Color mainUltraDarkColor = new Color(0, 32, 64);

    /**
     * The color for icons that represent <code>Class</code> entities.
     */
    public static final Color iconClassColor = new Color(128, 255, 128);

    /**
     * The color for icons that represent <code>Annotation</code> entities.
     */
    public static final Color iconAnnotationColor = new Color(141, 112, 255);

    /**
     * The color for icons that represent <code>Field</code> or
     * <code>Method</code> entities.
     */
    public static final Color iconFieldMethodColor = new Color(32, 128, 255);

    /**
     * The color for arrows on icons.
     */
    public static final Color iconArrowColor = new Color(128, 32, 0);

    /**
     * The default dimension for icons (both width and height).
     */
    public static final int ICON_DIMENSION = 15;

    /**
     * The dimension of cube in
     * {@link #getGradientCubesImage(int, int, Color, Color, int, int)}
     */
    public static final int CUBE_DIMENSION = 5;

    public static final int CLOSE_DIMENSION = 4;

    // ----------------------------------------------------- Instance Variables
    // none

    // ----------------------------------------------------------- Constructors
    // none

    // --------------------------------------------------------- Public Methods

    /**
     * Returns a completely transparent image of specified dimensions.
     *
     * @param width Image width.
     * @param height Image height.
     * @return Completely transparent image of specified dimensions.
     */
    public static BufferedImage getBlankImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        // set completely transparent
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                image.setRGB(col, row, 0x0);
            }
        }
        // get graphics and set hints
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        return image;
    }

    /**
     * Creates an image of specified dimensions that contains a semi-random
     * distribution of JAXB buzzwords in random yellowish colors.
     *
     * @param width The width of the output image.
     * @param height The height of the output image.
     * @return The resulting image.
     */
    public static Icon getWizardImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Color main = ImageCreator.mainLightColor;
        double coef = 0.9;
        Color dark = new Color((int) (coef * main.getRed()), (int) (coef * main
                .getGreen()), (int) (coef * main.getBlue()));

        Color light = new Color(255 - (int) (coef * (255 - main.getRed())),
                255 - (int) (coef * (255 - main.getGreen())),
                255 - (int) (coef * (255 - main.getBlue())));

        graphics.drawImage(getBackgroundStripedImage(width, height, light,
                main, dark), 0, 0, null);

        String[] strings = {"JAXB 2.0", "XSD", "JAXB", "XML", "Binding",
        "<entity>", "Java", "JAXBContext", "Marshaller",
        "Unmarshaller"};
        int cellCount = 4;
        int cellSizeX = width / cellCount;
        int cellSizeY = height / cellCount;
        for (int i = 0; i < cellCount; i++) {
            for (int j = 0; j < cellCount; j++) {
                int fontSize = (int) (15 + 15 * Math.random());
                int strIndex = (int) (strings.length * Math.random());

                // choose yellowish random color
                int r = (int) (255 - 40 * Math.random());
                int g = (int) (255 - 40 * Math.random());
                int b = (int) (40 * Math.random());
                graphics.setColor(new Color(r, g, b));

                // set the string's center to be in the center
                // of this cell
                graphics.setFont(new Font("Arial", Font.PLAIN, fontSize));
                FontRenderContext frc = graphics.getFontRenderContext();
                TextLayout mLayout = new TextLayout(strings[strIndex], graphics
                        .getFont(), frc);
                int x = (int) ((i + Math.random()) * cellSizeX - mLayout
                        .getBounds().getWidth() / 2);
                int y = (int) ((j + Math.random()) * cellSizeY);
                Composite c = AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, (float) (0.2 + 0.8 * Math
                        .random()));
                graphics.setComposite(c);
                graphics.drawString(strings[strIndex], x, y);
            }
        }

        return new ImageIcon(image);
    }

    /**
     * Returns an image of specified dimensions that contains the specified
     * string in big letters. The resulting image will have gradient background
     * and semi-transparent background for the title. The title will be centered
     * in the center of the image.
     *
     * @param width The width of the output image.
     * @param height The height of the output image.
     * @param title Title string to write on the output image.
     * @return The resulting image.
     */
    public static Icon getTitleImage(int width, int height, String title,
            Color foregroundColor, Color shadowColor) {
        // get gradient background image
        BufferedImage image =
                ImageCreator.getBackgroundImage(width, height, false);

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // find the bounds of the entire string
        Font font = new Font("Arial", Font.PLAIN, height - 8);
        graphics.setFont(font);
        FontMetrics fm = graphics.getFontMetrics();
        FontRenderContext frc = graphics.getFontRenderContext();
        TextLayout mLayout = new TextLayout(title, font, frc);

        // Place the first full string, horizontally centered,
        // at the bottom of the component.
        Rectangle2D bounds = mLayout.getBounds();
        double x = (width - bounds.getWidth()) / 2;
        double y = (height - bounds.getHeight() + fm.getHeight()) / 2 + 3;

        Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                (float) 0.8);
        graphics.setComposite(c);
        graphics.setColor(shadowColor);
        graphics.drawString(title, (int) x + 1, (int) y + 1);
        c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 1.0);
        graphics.setComposite(c);
        graphics.setColor(foregroundColor);
        graphics.drawString(title, (int) x, (int) y);

        return new ImageIcon(image);
    }

    /**
     * Returns an image of specified dimensions that has gradient striped
     * background.
     *
     * @param width The width of the output image
     * @param height The height of the output image
     * @param lightColor The color of the left side of the even stripes
     * @param midColor The color of the right side of the even stripes and the
     *                 left side of the odd stripes
     * @param darkColor The color of the right side of the odd stripes
     * @return The resulting image
     */
    public static BufferedImage getBackgroundStripedImage(int width,
            int height, Color lightColor, Color midColor, Color darkColor) {
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = (Graphics2D) image.getGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // light gradients
        GradientPaint gpLightLeft = new GradientPaint(0, 0, lightColor,
                width / 2, 0, midColor);
        GradientPaint gpLightRight = new GradientPaint(width / 2, 0, midColor,
                width, 0, lightColor);
        // dark gradients
        GradientPaint gpDarkLeft = new GradientPaint(0, 0, midColor, width / 2,
                0, darkColor);
        GradientPaint gpDarkRight = new GradientPaint(width / 2, 0, darkColor,
                width, 0, midColor);

        for (int row = 0; row < height; row++) {
            GradientPaint gpLeft = (row % 2 == 0) ? gpLightLeft :
                gpDarkLeft;
            graphics.setPaint(gpLeft);
            graphics.drawLine(0, row, width / 2, row);
            GradientPaint gpRight = (row % 2 == 0) ? gpLightRight :
                gpDarkRight;
            graphics.setPaint(gpRight);
            graphics.drawLine(width / 2, row, width, row);
        }

        return image;
    }

    /**
     * Returns an image of specified dimensions that has gradient background and
     * an optional gradient separator stripe on its upper border.
     *
     * @param width The width of the output image
     * @param height The height of the output image
     * @param hasStripeTop if <code>true</code>, a gradient stripe few pixels
     *                     high is added on the upper border of this image
     * @return The resulting image
     */
    public static BufferedImage getBackgroundImage(int width, int height,
            boolean hasStripeTop) {

        BufferedImage image = getBlankImage(width, height);
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        if (hasStripeTop) {
            // create stripe on the bottom side
            for (int col = 0; col < width; col++) {
                // set color transparency - 1.0 on 0 and width, 0.0 in the
                // middle
                float transp = Math.abs((float) (col - width / 2))
                / (float) (width / 2);
                transp = Math.min((float) 1.0, (float) (transp * 1.25));
                Composite c = AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, (float) 1.0 - transp);
                graphics.setComposite(c);
                graphics.setColor(Color.black);
                graphics.drawLine(col, 0, col, 2);
            }
        }

        return image;
    }

    /**
     * Returns an image of specified width that contains (possibly) multilined
     * representation of the input string. The resulting image will have a
     * gradient background and semi-transparent background for the text. The
     * input string is broken into a sequence of lines. Each line does not
     * exceed the specified width of the resulting image. The height of the
     * resulting image is computed according to the number of lines.
     *
     * @param str The input string to be shown,.
     * @param width The width of the output image.
     * @return The resulting image.
     */
    public static Icon getMultiline(String str, int width,
            Color foregroundColor, Color shadowColor) {

        // first break up the string into lines, so that each line is no
        // longer than specified width
        BufferedImage tempImage =
                new BufferedImage(width, 20, BufferedImage.TYPE_INT_ARGB);

        Graphics2D tempGraphics = (Graphics2D) tempImage.getGraphics();

        tempGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        Font font = new Font("Arial", Font.PLAIN, 12);

        // here we could use LineBreakMeasurer, but it's not good
        // because we don't know the number of lines in advance.
        // So, in any case we need to break up the string, compute
        // the number of lines, allocate new image and then put
        // the lines on it. Might as well take the simple approach
        // and do it ourselves.
        tempGraphics.setFont(font);
        FontRenderContext frc = tempGraphics.getFontRenderContext();

        LinkedList lines = new LinkedList();
        StringTokenizer tokenizer = new StringTokenizer(str, " ", false);
        String currLine = "";
        while (tokenizer.hasMoreTokens()) {
            String currToken = tokenizer.nextToken() + " ";
            String newLine = currLine + currToken;
            TextLayout mLayout = new TextLayout(newLine, font, frc);
            // Place the first full string, horizontally centered,
            // at the bottom of the component.
            Rectangle2D bounds = mLayout.getBounds();
            if (bounds.getWidth() > (width - 20)) {
                // start new line
                lines.addLast(currLine);
                currLine = currToken;
            } else {
                currLine = newLine;
            }
        }
        // add the last one
        lines.addLast(currLine);

        // count the number of lines
        int lineCount = lines.size();
        FontMetrics fm = tempGraphics.getFontMetrics();
        int height = lineCount * (fm.getHeight() + 5);

        // create new image
        BufferedImage image =
                ImageCreator.getBackgroundImage(width, height, false);
        // write all strings
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        graphics.setFont(font);
        int ypos = fm.getHeight();
        for (int i = 0; i < lines.size(); i++) {
            String line = (String)lines.get(i);
            Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    (float) 0.8);
            graphics.setComposite(c);
            graphics.setColor(shadowColor);
            graphics.drawString(line, 6, ypos + 1);
            c = AlphaComposite
                    .getInstance(AlphaComposite.SRC_OVER, (float) 1.0);
            graphics.setComposite(c);
            graphics.setColor(foregroundColor);
            graphics.drawString(line, 5, ypos);
            ypos += (fm.getHeight() + 1);
        }

        return new ImageIcon(image);
    }

    /**
     * Returns an icon image with specified background that contains a single
     * letter in its center with optional marker sign that is shown when
     * <code>isCollection</code> parameter is <code>true</code>.
     *
     * @param letter The letter to show in the center of the icon. This letter
     *               is capitalized if it was not capitalized.
     * @param isCollection If <code>true</code>, a special marker sign is shown
     *                     in the upper-right portion of the resulting image
     *                     icon.
     * @param backgroundColor The background color for the resulting image icon
     * @return An icon image with specified background that contains a single
     *         letter in its center with optional marker sign that is shown
     *         when <code>isCollection</code> parameter is <code>true</code>
     */
    public static BufferedImage getSingleLetterImage(char letter,
            boolean isCollection,
            Color backgroundColor) {

        BufferedImage image = new BufferedImage(ICON_DIMENSION, ICON_DIMENSION,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        letter = Character.toUpperCase(letter);
        graphics.setFont(new Font("Arial", Font.BOLD, 10));

        float x = (float) ((ICON_DIMENSION - Math.ceil(graphics
                .getFontMetrics().charWidth(letter))) / 2);
        float y = ICON_DIMENSION
                - (float) 2.0
                - (float) ((ICON_DIMENSION - graphics.getFontMetrics()
                .getStringBounds("" + letter, graphics).getHeight()) /
                2);

        graphics.setColor(backgroundColor);
        graphics.fillOval(0, 0, ICON_DIMENSION - 1, ICON_DIMENSION - 1);

        // create a whitish spot in the left-top corner of the icon
        double id4 = ICON_DIMENSION / 4.0;
        double spotX = id4;
        double spotY = id4;
        for (int col = 0; col < ICON_DIMENSION; col++) {
            for (int row = 0; row < ICON_DIMENSION; row++) {
                // distance to spot
                double dx = col - spotX;
                double dy = row - spotY;
                double dist = Math.sqrt(dx * dx + dy * dy);

                // distance of 0.0 - comes 90% to Color.white
                // distance of ICON_DIMENSION - stays the same

                if (dist > ICON_DIMENSION) {
                    dist = ICON_DIMENSION;
                }

                int currColor = image.getRGB(col, row);
                int transp = (currColor >>> 24) & 0xFF;
                int oldR = (currColor >>> 16) & 0xFF;
                int oldG = (currColor >>> 8) & 0xFF;
                int oldB = (currColor >>> 0) & 0xFF;

                double coef = 0.9 - 0.9 * dist / ICON_DIMENSION;
                int dr = 255 - oldR;
                int dg = 255 - oldG;
                int db = 255 - oldB;

                int newR = (int) (oldR + coef * dr);
                int newG = (int) (oldG + coef * dg);
                int newB = (int) (oldB + coef * db);

                int newColor = (transp << 24) | (newR << 16) | (newG << 8)
                | newB;
                image.setRGB(col, row, newColor);

            }
        }

        // draw outline of the icon
        graphics.setColor(Color.black);
        graphics.drawOval(0, 0, ICON_DIMENSION - 1, ICON_DIMENSION - 1);

        // draw the letter
        graphics.drawString("" + letter, x, y);

        // if collection - draw '+' sign
        if (isCollection) {
            int xp = ICON_DIMENSION - 6;
            int yp = 3;
            graphics.setColor(new Color(255, 255, 255, 128));
            graphics.drawLine(xp - 1, yp - 2, xp + 6, yp - 2);
            graphics.drawLine(xp - 1, yp - 1, xp + 6, yp - 1);
            graphics.drawLine(xp - 1, yp, xp + 6, yp);
            graphics.drawLine(xp - 1, yp + 1, xp + 6, yp + 1);
            graphics.drawLine(xp + 1, yp - 4, xp + 1, yp + 3);
            graphics.drawLine(xp + 2, yp - 4, xp + 2, yp + 3);
            graphics.drawLine(xp + 3, yp - 4, xp + 3, yp + 3);
            graphics.drawLine(xp + 4, yp - 4, xp + 4, yp + 3);
            graphics.setColor(new Color(255, 64, 64));
            graphics.drawLine(xp, yp - 1, xp + 5, yp - 1);
            graphics.drawLine(xp, yp, xp + 5, yp);
            graphics.drawLine(xp + 2, yp - 3, xp + 2, yp + 2);
            graphics.drawLine(xp + 3, yp - 3, xp + 3, yp + 2);
        }

        return image;
    }

    /**
     * Returns an icon image with specified background that contains a single
     * letter in its center with optional marker sign that is shown when
     * <code>isCollection</code> parameter is <code>true</code>.
     *
     * @param letter The letter to show in the center of the icon. This letter
     *               is  capitalized if it was not capitalized
     * @param isCollection If <code>true</code>, a special marker sign is shown
     *                     in the upper-right portion of the resulting image
     *                     icon
     * @param backgroundColor The background color for the resulting image icon
     * @return An icon image with specified background that contains a single
     *         letter in its center with optional marker sign that is shown
     *         when <code>isCollection</code> parameter is <code>true</code>
     */
    public static Icon getSingleLetterIcon(char letter, boolean isCollection,
            Color backgroundColor) {
        return new ImageIcon(getSingleLetterImage(letter, isCollection,
                backgroundColor));
    }

    /**
     * Returns an icon image with specified background that contains a single
     * letter in its center, an arrow in the bottom portion of the icon and an
     * optional marker sign that is shown when <code>isCollection</code>
     * parameter is <code>true</code>.
     *
     * @param letter The letter to show in the center of the icon. This letter
     *               is capitalized if it was not capitalized
     * @param isCollection If <code>true</code>, a special marker sign is shown
     *                     in the upper-right portion of the resulting image
     *                     icon
     * @param backgroundColor The background color for the resulting image icon
     * @param isArrowToRight If <code>true</code>, the arrow will point to the
     *                       right, otherwise the arrow will point to the left
     * @param arrowColor The color of the arrow
     * @return An icon image with specified background that contains a single
     *         letter in its center, an arrow in the bottom portion of the icon
     *         and an optional marker sign that is shown when
     *         <code>isCollection</code> parameter is <code>true</code>
     */
    public static Icon getLetterIconWithArrow(char letter,
            boolean isCollection, Color backgroundColor,
            boolean isArrowToRight, Color arrowColor) {

        BufferedImage image = getSingleLetterImage(letter, isCollection,
                backgroundColor);

        Graphics2D graphics = (Graphics2D) image.getGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        BufferedImage arrowImage = getArrowImage(arrowColor, new Color(255,
                255, 255, 196), ICON_DIMENSION, isArrowToRight);

        graphics.drawImage(arrowImage, 0, ICON_DIMENSION
                - arrowImage.getHeight(), null);

        return new ImageIcon(image);
    }

    /**
     * Returns an image that contains a right-pointing arrow of specified width
     * and color.
     *
     * @param color Arrow color
     * @param width Arrow width
     * @return An image that contains a right-pointing arrow of specified width
     *         and color
     */
    public static BufferedImage getRightArrow(Color color, Color haloColor,
            int width) {
        int height = 6;
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = (Graphics2D) image.getGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // draw arrow
        Polygon pol = new Polygon();
        int ya = 3;
        pol.addPoint(1, ya);
        pol.addPoint(width / 2 + 3, ya);
        pol.addPoint(width / 2 + 3, ya + 2);
        pol.addPoint(width - 1, ya);
        pol.addPoint(width / 2 + 3, ya - 2);
        pol.addPoint(width / 2 + 3, ya);

        graphics.setColor(color);
        graphics.drawPolygon(pol);

        // create semi-transparent halo around arrow (to make it stand
        // out)
        BufferedImage fimage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        // set completely transparent
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                fimage.setRGB(col, row, 0x0);
            }
        }
        Graphics2D fgraphics = (Graphics2D) fimage.getGraphics();
        int haloOpacity = haloColor.getAlpha();
        for (int col = 0; col < width; col++) {
            int xs = Math.max(0, col - 1);
            int xe = Math.min(width - 1, col + 1);
            for (int row = 0; row < height; row++) {
                int ys = Math.max(0, row - 1);
                int ye = Math.min(height - 1, row + 1);
                int currColor = image.getRGB(col, row);
                int opacity = (int) (haloOpacity *
                        ((currColor >>> 24) & 0xFF) / 255.0);
                if (opacity > 0) {
                    // mark all pixels in 3*3 area
                    for (int x = xs; x <= xe; x++) {
                        for (int y = ys; y <= ye; y++) {
                            int oldOpacity =
                                    (fimage.getRGB(x, y) >>> 24) & 0xFF;
                            int newOpacity = Math.max(oldOpacity, opacity);
                            // set semi-transparent white
                            int newColor = (newOpacity << 24) | (255 << 16)
                            | (255 << 8) | 255;
                            fimage.setRGB(x, y, newColor);
                        }
                    }
                }
            }
        }

        // draw the original arrow image on top of the halo
        fgraphics.drawImage(image, 0, 0, null);

        return fimage;
    }

    /**
     * Returns an image that contains a left-pointing arrow of specified width
     * and color.
     *
     * @param arrowColor Arrow color
     * @param width Arrow width
     * @return An image that contains a left-pointing arrow of specified width
     *         and color.
     */
    public static BufferedImage getLeftArrow(Color arrowColor, Color haloColor,
            int width) {
        BufferedImage rimage = getRightArrow(arrowColor, haloColor, width);

        int height = rimage.getHeight();

        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                image.setRGB(col, row, rimage.getRGB(width - 1 - col, row));
            }
        }
        return image;
    }

    /**
     * Returns an image that contains an arrow of specified width, direction and
     * color.
     *
     * @param arrowColor Arrow color
     * @param width Arrow width
     * @param isArrowToRight If <code>true</code>, the arrow is pointing to the
     *                       right, otherwise the arrow is pointing to the left
     * @return An image that contains a left-pointing arrow of specified width
     *         and color.
     */
    public static BufferedImage getArrowImage(Color arrowColor,
            Color haloColor,
            int width,
            boolean isArrowToRight) {
        return isArrowToRight ? getRightArrow(arrowColor, haloColor, width) :
                                getLeftArrow(arrowColor, haloColor, width);
    }

    /**
     * Creates a one-pixel high gradient image of specified width, opacity and
     * colors of the starting pixel and the ending pixel.
     *
     * @param width The width of the resulting image
     * @param leftColor The color of the first pixel of the resulting image
     * @param rightColor The color of the last pixel of the resulting image
     * @param opacity The opacity of the resulting image (in 0..1 range). The
     *                smaller the value, the more transparent the resulting
     *                image is.
     * @return A one-pixel high gradient image of specified width, opacity and
     *         colors of the starting pixel and the ending pixel.
     */
    public static BufferedImage createGradientLine(int width,
            Color leftColor,
            Color rightColor,
            double opacity) {
        BufferedImage image = new BufferedImage(width, 1,
                BufferedImage.TYPE_INT_ARGB);
        int iOpacity = (int) (255 * opacity);

        for (int col = 0; col < width; col++) {
            double coef = (double) col / (double) width;
            int r = (int) (leftColor.getRed() + coef
                    * (rightColor.getRed() - leftColor.getRed()));
            int g = (int) (leftColor.getGreen() + coef
                    * (rightColor.getGreen() - leftColor.getGreen()));
            int b = (int) (leftColor.getBlue() + coef
                    * (rightColor.getBlue() - leftColor.getBlue()));

            int color = (iOpacity << 24) | (r << 16) | (g << 8) | b;
            image.setRGB(col, 0, color);
        }
        return image;
    }

    /**
     * Returns a gutter marker of specified dimensions and theme color. The
     * resulting image is a rectangle with dark border and light filling. The
     * colors of the border and the filling are created based on the input
     * color.
     *
     * @param themeColor Base color for border and filling
     * @param width Gutter marker width
     * @param height Gutter marker height
     * @return Gutter marker of specified dimensions and theme color.
     */
    public static BufferedImage getGutterMarker(Color themeColor,
            int width,
            int height) {
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = (Graphics2D) image.getGraphics();

        // darker color for border
        int rd = themeColor.getRed() / 2;
        int gd = themeColor.getGreen() / 2;
        int bd = themeColor.getBlue() / 2;
        Color darkColor = new Color(rd, gd, bd);

        // lighter color for inside
        int rl = 255 - (255 - themeColor.getRed()) / 4;
        int gl = 255 - (255 - themeColor.getGreen()) / 4;
        int bl = 255 - (255 - themeColor.getBlue()) / 4;
        Color lightColor = new Color(rl, gl, bl);

        graphics.setColor(lightColor);
        graphics.fillRect(0, 0, width - 1, height - 1);
        graphics.setColor(darkColor);
        graphics.drawRect(0, 0, width - 1, height - 1);
        return image;
    }

    /**
     * Returns a square gutter status image of specified dimensions and theme
     * color. The resulting image is a square with gradient filling and dark and
     * light borders.
     *
     * @param themeColor Base color for borders and filling
     * @param dimension Width and height of the resulting image
     * @return Square gutter status image of specified dimensions and theme
     *         color
     */
    public static BufferedImage getGutterStatusImage(Color themeColor,
            int dimension) {
        BufferedImage image = new BufferedImage(dimension, dimension,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        // ultra dark color
        int rud = themeColor.getRed() / 4;
        int gud = themeColor.getGreen() / 4;
        int bud = themeColor.getBlue() / 4;
        Color ultraDarkColor = new Color(rud, gud, bud);

        // darker color
        int rd = themeColor.getRed() / 2;
        int gd = themeColor.getGreen() / 2;
        int bd = themeColor.getBlue() / 2;
        Color darkColor = new Color(rd, gd, bd);

        // lighter color
        int rl = 255 - (255 - themeColor.getRed()) / 4;
        int gl = 255 - (255 - themeColor.getGreen()) / 4;
        int bl = 255 - (255 - themeColor.getBlue()) / 4;
        Color lightColor = new Color(rl, gl, bl);

        // create gradient
        GradientPaint gradient = new GradientPaint(0, 0,
                lightColor,
                dimension,
                dimension,
                darkColor);
        graphics.setPaint(gradient);
        Rectangle rect = new Rectangle(dimension, dimension);
        graphics.fill(rect);
        graphics.setColor(ultraDarkColor);
        graphics.drawLine(0, 0, dimension - 1, 0);
        graphics.drawLine(0, 0, 0, dimension - 1);
        graphics.setColor(lightColor);
        graphics.drawLine(0, dimension - 1, dimension - 1, dimension - 1);
        graphics.drawLine(dimension - 1, 1, dimension - 1, dimension - 1);

        return image;
    }

    /**
     * Returns an error marker of specified dimension with an <code>X</code>
     * inside. The resulting image is a red circular icon with white diagonal
     * cross with black border.
     *
     * @param dimension The diameter of the resulting marker
     * @return Error marker of specified dimension with an <code>X</code>
     *         inside
     */
    public static BufferedImage getErrorMarker(int dimension) {
        // new RGB image with transparency channel
        BufferedImage image = new BufferedImage(dimension, dimension,
                BufferedImage.TYPE_INT_ARGB);

        // create new graphics and set anti-aliasing hint
        Graphics2D graphics = (Graphics2D) image.getGraphics().create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // red background fill
        graphics.setColor(Color.red);
        graphics.fillOval(0, 0, dimension - 1, dimension - 1);

        // create spot in the upper-left corner using temporary graphics
        // with clip set to the icon outline
        GradientPaint spot =
                new GradientPaint(0, 0,
                new Color(255, 255, 255, 200),
                dimension,
                dimension,
                new Color(255, 255, 255, 0));

        Graphics2D tempGraphics = (Graphics2D) graphics.create();
        tempGraphics.setPaint(spot);
        tempGraphics.setClip(new Ellipse2D.Double(0, 0, dimension - 1,
                dimension - 1));
        tempGraphics.fillRect(0, 0, dimension, dimension);
        tempGraphics.dispose();

        // draw outline of the icon
        graphics.setColor(new Color(0, 0, 0, 128));
        graphics.drawOval(0, 0, dimension - 1, dimension - 1);

        // draw the X sign using two paths
        float dimOuter = (float) (0.5f * Math.pow(dimension, 0.75));
        float dimInner = (float) (0.28f * Math.pow(dimension, 0.75));
        float ds = 0.28f * (dimension - 1);
        float de = 0.72f * (dimension - 1);

        // create the paths
        GeneralPath gp1 = new GeneralPath();
        gp1.moveTo(ds, ds);
        gp1.lineTo(de, de);
        GeneralPath gp2 = new GeneralPath();
        gp2.moveTo(de, ds);
        gp2.lineTo(ds, de);
        graphics.setStroke(new BasicStroke(dimOuter,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        graphics.setColor(new Color(0, 0, 0, 196));
        graphics.draw(gp1);
        graphics.draw(gp2);
        graphics.setStroke(new BasicStroke(dimInner,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        graphics.setColor(Color.white);
        graphics.draw(gp1);
        graphics.draw(gp2);

        // dispose
        graphics.dispose();
        return image;
    }

    /**
     * Returns an error marker of specified dimension with an <code>X</code>
     * inside. The resulting image is a red circular icon with white diagonal
     * cross with black border.
     *
     * @param dimension The diameter of the resulting marker
     * @return Error marker of specified dimension with an <code>X</code>
     *         inside
     */
    public static Icon getErrorMarkerIcon(int dimension) {
        return new ImageIcon(getErrorMarker(dimension));
    }

    /**
     * Returns a success marker of specified dimension with an <code>V</code>
     * inside. The resulting image is a green circular icon with white V-shaped
     * mark with black border.
     *
     * @param dimension The diameter of the resulting marker
     * @return Success marker of specified dimension with a <code>V</code>
     *         inside
     */
    public static BufferedImage getSuccessMarker(int dimension) {
        // new RGB image with transparency channel
        BufferedImage image = new BufferedImage(dimension, dimension,
                BufferedImage.TYPE_INT_ARGB);

        // create new graphics and set anti-aliasing hint
        Graphics2D graphics = (Graphics2D) image.getGraphics().create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // green background fill
        graphics.setColor(new Color(0, 196, 0));
        graphics.fillOval(0, 0, dimension - 1, dimension - 1);

        // create spot in the upper-left corner using temporary graphics
        // with clip set to the icon outline
        GradientPaint spot =
                new GradientPaint(0, 0,
                new Color(255, 255, 255, 200),
                dimension,
                dimension,
                new Color(255, 255, 255, 0));

        Graphics2D tempGraphics = (Graphics2D) graphics.create();
        tempGraphics.setPaint(spot);
        tempGraphics.setClip(new Ellipse2D.Double(0, 0, dimension - 1,
                dimension - 1));
        tempGraphics.fillRect(0, 0, dimension, dimension);
        tempGraphics.dispose();

        // draw outline of the icon
        graphics.setColor(new Color(0, 0, 0, 128));
        graphics.drawOval(0, 0, dimension - 1, dimension - 1);

        // draw the V sign
        float dimOuter = (float) (0.5f * Math.pow(dimension, 0.75));
        float dimInner = (float) (0.28f * Math.pow(dimension, 0.75));
        // create the path itself
        GeneralPath gp = new GeneralPath();
        gp.moveTo(0.25f * dimension, 0.45f * dimension);
        gp.lineTo(0.45f * dimension, 0.65f * dimension);
        gp.lineTo(0.85f * dimension, 0.12f * dimension);
        // draw blackish outline
        graphics.setStroke(new BasicStroke(dimOuter,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        graphics.setColor(new Color(0, 0, 0, 196));
        graphics.draw(gp);
        // draw white inside
        graphics.setStroke(new BasicStroke(dimInner,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        graphics.setColor(Color.white);
        graphics.draw(gp);

        // dispose
        graphics.dispose();
        return image;
    }

    /**
     * Returns a success marker of specified dimension with an <code>V</code>
     * inside. The resulting image is a green circular icon with white V-shaped
     * mark with black border.
     *
     * @param dimension The diameter of the resulting marker.
     * @return Success marker of specified dimension with a <code>V</code>
     *         inside
     */
    public static BufferedImage getSuccessCurvedMarker(int dimension) {
        // new RGB image with transparency channel
        BufferedImage image = new BufferedImage(dimension, dimension,
                BufferedImage.TYPE_INT_ARGB);

        // create new graphics and set anti-aliasing hint
        Graphics2D graphics = (Graphics2D) image.getGraphics().create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // green background fill
        graphics.setColor(new Color(0, 196, 0));
        graphics.fillOval(0, 0, dimension - 1, dimension - 1);

        // create spot in the upper-left corner using temporary graphics
        // with clip set to the icon outline
        GradientPaint spot =
                new GradientPaint(0, 0,
                new Color(255, 255, 255, 200),
                dimension,
                dimension,
                new Color(255, 255, 255, 0));

        Graphics2D tempGraphics = (Graphics2D) graphics.create();
        tempGraphics.setPaint(spot);
        tempGraphics.setClip(new Ellipse2D.Double(0, 0, dimension - 1,
                dimension - 1));
        tempGraphics.fillRect(0, 0, dimension, dimension);
        tempGraphics.dispose();

        // draw outline of the icon
        graphics.setColor(new Color(0, 0, 0, 128));
        graphics.drawOval(0, 0, dimension - 1, dimension - 1);

        // draw the V sign
        float dimOuter = (float) (0.5f * Math.pow(dimension, 0.75));
        float dimInner = (float) (0.28f * Math.pow(dimension, 0.75));

        // create the path itself
        GeneralPath gp = new GeneralPath();
        gp.moveTo(0.25f * dimension, 0.45f * dimension);
        gp.quadTo(0.35f * dimension, 0.52f * dimension, 0.45f * dimension,
                0.65f * dimension);
        gp.quadTo(0.65f * dimension, 0.3f * dimension, 0.85f * dimension,
                0.12f * dimension);
        // draw blackish outline
        graphics.setStroke(new BasicStroke(dimOuter,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        graphics.setColor(new Color(0, 0, 0, 196));
        graphics.draw(gp);
        // draw white inside
        graphics.setStroke(new BasicStroke(dimInner,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        graphics.setColor(Color.white);
        graphics.draw(gp);

        // dispose
        graphics.dispose();
        return image;
    }

    /**
     * Returns a success marker of specified dimension with an <code>V</code>
     * inside. The resulting image is a green circular icon with white V-shaped
     * mark with black border.
     *
     * @param dimension The diameter of the resulting marker
     * @return Success marker of specified dimension with a <code>V</code>
     *         inside
     */
    public static Icon getSuccessMarkerIcon(int dimension) {
        return new ImageIcon(getSuccessMarker(dimension));
    }

    /**
     * Returns a success marker of specified dimension with an <code>V</code>
     * inside. The resulting image is a green circular icon with white V-shaped
     * mark with black border.
     *
     * @param dimension The diameter of the resulting marker
     * @return Success marker of specified dimension with a <code>V</code>
     *         inside
     */
    public static Icon getSuccessCurvedMarkerIcon(int dimension) {
        return new ImageIcon(getSuccessCurvedMarker(dimension));
    }

    /**
     * Returns a warning marker of specified dimension with an <code>!</code>
     * inside. The resulting image is a yellow triangular icon with black
     * exclamation mark with white border.
     *
     * @param dimension The side of the resulting marker
     * @return Warning marker of specified dimension with an <code>!</code>
     *         inside.
     */
    public static BufferedImage getWarningMarker(int dimension) {
        // new RGB image with transparency channel
        BufferedImage image = new BufferedImage(dimension, dimension,
                BufferedImage.TYPE_INT_ARGB);

        // create new graphics and set anti-aliasing hint
        Graphics2D graphics = (Graphics2D) image.getGraphics().create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // create path for the outline
        GeneralPath iconOutlinePath = new GeneralPath();
        float d = dimension - 1;
        float d32 = (float) (0.1 * d * Math.sqrt(3.0) / 2.0);
        float height = (float) (1.1 * d * Math.sqrt(3.0) / 2.0);
        iconOutlinePath.moveTo(0.45f * d, d32);
        iconOutlinePath.quadTo(0.5f * d, 0, 0.55f * d, d32);
        iconOutlinePath.lineTo(0.95f * d, height - d32);
        iconOutlinePath.quadTo(d, height, 0.9f * d, height);
        iconOutlinePath.lineTo(0.1f * d, height);
        iconOutlinePath.quadTo(0, height, 0.05f * d, height - d32);
        iconOutlinePath.lineTo(0.45f * d, d32);

        // fill inside with yellowish color
        graphics.setColor(new Color(250, 189, 5));
        graphics.fill(iconOutlinePath);

        // create spot in the upper-left corner using temporary graphics
        // with clip set to the icon outline
        GradientPaint spot =
                new GradientPaint(0, 0,
                new Color(255, 255, 255, 200),
                dimension,
                dimension,
                new Color(255, 255, 255, 0));

        Graphics2D tempGraphics = (Graphics2D) graphics.create();
        tempGraphics.setPaint(spot);
        tempGraphics.setClip(iconOutlinePath);
        tempGraphics.fillRect(0, 0, dimension, dimension);
        tempGraphics.dispose();

        // draw outline of the icon
        graphics.setColor(new Color(0, 0, 0, 128));
        graphics.draw(iconOutlinePath);

        // draw the ! sign
        float dimOuter = (float) (0.5f * Math.pow(dimension, 0.75));
        float dimInner = (float) (0.28f * Math.pow(dimension, 0.75));
        GeneralPath markerPath = new GeneralPath();
        markerPath.moveTo((float) 0.5 * d, (float) 0.3 * height);
        markerPath.lineTo((float) 0.5 * d, (float) 0.6 * height);
        markerPath.moveTo((float) 0.5 * d, (float) 0.85 * height);
        markerPath.lineTo((float) 0.5 * d, (float) 0.85 * height);
        graphics.setStroke(new BasicStroke(dimOuter,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        graphics.setColor(new Color(126, 63, 0));
        graphics.draw(markerPath);
        graphics.setStroke(new BasicStroke(dimInner,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        graphics.setColor(new Color(230, 200, 120));
        graphics.draw(markerPath);

        // dispose
        graphics.dispose();
        return image;
    }

    /**
     * Returns an info marker of specified dimension with an <code>!</code>
     * inside. The resulting image is a blue triangular icon with white
     * exclamation mark with black border.
     *
     * @param dimension The side of the resulting marker.
     * @return Info marker of specified dimension with an <code>!</code>
     *         inside.
     */
    public static BufferedImage getInfoMarker(int dimension) {
        // new RGB image with transparency channel
        BufferedImage image = new BufferedImage(dimension, dimension,
                BufferedImage.TYPE_INT_ARGB);

        // create new graphics and set anti-aliasing hint
        Graphics2D graphics = (Graphics2D) image.getGraphics().create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // create path for the outline
        GeneralPath iconOutlinePath = new GeneralPath();
        float d = dimension - 1;
        float d32 = (float) (0.1 * d * Math.sqrt(3.0) / 2.0);
        float height = (float) (1.1 * d * Math.sqrt(3.0) / 2.0);
        iconOutlinePath.moveTo(0.45f * d, d32);
        iconOutlinePath.quadTo(0.5f * d, 0, 0.55f * d, d32);
        iconOutlinePath.lineTo(0.95f * d, height - d32);
        iconOutlinePath.quadTo(d, height, 0.9f * d, height);
        iconOutlinePath.lineTo(0.1f * d, height);
        iconOutlinePath.quadTo(0, height, 0.05f * d, height - d32);
        iconOutlinePath.lineTo(0.45f * d, d32);

        // fill inside with bluish color
        graphics.setColor(new Color(0, 50, 255));
        graphics.fill(iconOutlinePath);

        // create spot in the upper-left corner using temporary graphics
        // with clip set to the icon outline
        GradientPaint spot =
                new GradientPaint(0, 0,
                new Color(255, 255, 255, 200),
                dimension,
                dimension,
                new Color(255, 255, 255, 0));
        Graphics2D tempGraphics = (Graphics2D) graphics.create();
        tempGraphics.setPaint(spot);
        tempGraphics.setClip(iconOutlinePath);
        tempGraphics.fillRect(0, 0, dimension, dimension);
        tempGraphics.dispose();

        // draw outline of the icon
        graphics.setColor(new Color(0, 0, 0, 128));
        graphics.draw(iconOutlinePath);

        // draw the ! sign
        float dimOuter = (float) (0.5f * Math.pow(dimension, 0.75));
        float dimInner = (float) (0.28f * Math.pow(dimension, 0.75));
        GeneralPath markerPath = new GeneralPath();
        markerPath.moveTo((float) 0.5 * d, (float) 0.3 * height);
        markerPath.lineTo((float) 0.5 * d, (float) 0.6 * height);
        markerPath.moveTo((float) 0.5 * d, (float) 0.85 * height);
        markerPath.lineTo((float) 0.5 * d, (float) 0.85 * height);
        graphics.setStroke(new BasicStroke(dimOuter,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        graphics.setColor(Color.black);
        graphics.draw(markerPath);
        graphics.setStroke(new BasicStroke(dimInner,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        graphics.setColor(Color.white);
        graphics.draw(markerPath);

        // dispose
        graphics.dispose();
        return image;
    }

    /**
     * Returns an info marker of specified dimension with an <code>!</code>
     * inside. The resulting image is a blue triangular icon with white
     * exclamation mark with black border.
     *
     * @param dimension The diameter of the resulting marker
     * @return Info marker of specified dimension with an <code>!</code>
     *         inside
     */
    public static Icon getInfoMarkerIcon(int dimension) {
        return new ImageIcon(getInfoMarker(dimension));
    }

    /**
     * Returns a warning marker of specified dimension with an <code>!</code>
     * inside. The resulting image is a yellow triangular icon with black
     * exclamation mark with white border.
     *
     * @param dimension The side of the resulting marker
     * @return Warning marker of specified dimension with an <code>!</code>
     *         inside.
     */
    public static Icon getWarningMarkerIcon(int dimension) {
        return new ImageIcon(getWarningMarker(dimension));
    }

    /**
     * Creates <code>close</code> image icon of specified dimension.
     *
     * @param dimension The dimension of the resulting square icon
     * @return The resulting icon.
     */
    public static Icon getCloseIcon(int dimension) {
        BufferedImage image = new BufferedImage(dimension, dimension,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // create gradient
        GradientPaint gradient =
                new GradientPaint(0, 0,
                ImageCreator.mainUltraLightColor,
                dimension,
                dimension,
                Color.white);
        graphics.setPaint(gradient);
        Rectangle rect = new Rectangle(dimension, dimension);
        graphics.fill(rect);

        graphics.setColor(Color.black);
        graphics.drawRect(0, 0, dimension - 1, dimension - 1);

        graphics.setStroke(new BasicStroke(2.0f));
        graphics.setColor(ImageCreator.mainMidColor);
        graphics.drawLine(3, 3, dimension - 4, dimension - 4);
        graphics.drawLine(3, dimension - 4, dimension - 4, 3);

        return new ImageIcon(image);
    }

    /**
     * Creates <code>close</code> image icon of specified dimension.
     *
     * @param dimension The dimension of the resulting square icon
     * @return The resulting icon.
     */
    public static Icon getCloseIcon(int dimension, char letter) {
        BufferedImage image = new BufferedImage(dimension, dimension,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // // create gradient
        GradientPaint gradient =
                new GradientPaint(dimension, 0,
                ImageCreator.mainDarkColor,
                0,
                dimension,
                ImageCreator.mainUltraDarkColor);
        graphics.setPaint(gradient);

        Font font = new Font("Arial", Font.BOLD, dimension - 2);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setFont(font);
        letter = Character.toUpperCase(letter);
        int charWidth = graphics.getFontMetrics().charWidth(letter);

        int x = dimension - charWidth - 2;
        FontRenderContext frc = graphics.getFontRenderContext();
        TextLayout mLayout = new TextLayout("" + letter, font, frc);
        Rectangle2D bounds = mLayout.getBounds();

        // graphics.setColor(ImageCreator.mainUltraDarkColor);
        graphics.drawString("" + letter, x, (int) bounds.getHeight() + 2);

        int xs = 1;
        int xe = xs + CLOSE_DIMENSION;
        int ys = ICON_DIMENSION - CLOSE_DIMENSION - 3;
        int ye = ys + CLOSE_DIMENSION;

        graphics.setStroke(new BasicStroke(3.5f));
        graphics.setColor(new Color(255, 255, 255, 196));
        graphics.drawLine(xs, ys, xe, ye);
        graphics.drawLine(xs, ye, xe, ys);
        graphics.setStroke(new BasicStroke(2.0f));
        graphics.setColor(ImageCreator.mainMidColor);
        graphics.drawLine(xs, ys, xe, ye);
        graphics.drawLine(xs, ye, xe, ys);

        return new ImageIcon(image);
    }

    /**
     * Creates <code>edit</code> image icon of specified dimension.
     *
     * @param dimension The dimension of the resulting square icon
     * @return The resulting icon.
     */
    public static Icon getEditIcon(int dimension, char letter) {
        BufferedImage image = new BufferedImage(dimension, dimension,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // // create gradient
        GradientPaint gradient =
                new GradientPaint(dimension, 0,
                ImageCreator.mainDarkColor,
                0,
                dimension,
                ImageCreator.mainUltraDarkColor);
        graphics.setPaint(gradient);

        Font font = new Font("Arial", Font.BOLD, dimension - 2);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setFont(font);
        letter = Character.toUpperCase(letter);
        int charWidth = graphics.getFontMetrics().charWidth(letter);

        int x = dimension - charWidth - 2;
        FontRenderContext frc = graphics.getFontRenderContext();
        TextLayout mLayout = new TextLayout("" + letter, font, frc);
        Rectangle2D bounds = mLayout.getBounds();

        // graphics.setColor(ImageCreator.mainUltraDarkColor);
        graphics.drawString("" + letter, x, (int) bounds.getHeight() + 2);

        int xs = 1;
        int xe = xs + CLOSE_DIMENSION;
        int xm = (xs + xe) / 2;
        int ys = ICON_DIMENSION - CLOSE_DIMENSION - 3;
        int ye = ys + CLOSE_DIMENSION;
        int ym = (ys + ye) / 2;

        graphics.setStroke(new BasicStroke(3.5f));
        graphics.setColor(new Color(255, 255, 255, 196));
        graphics.drawLine(xs, ym, xe, ym);
        graphics.drawLine(xm, ys, xm, ye);
        graphics.setStroke(new BasicStroke(2.5f));
        graphics.setColor(ImageCreator.mainMidColor);
        graphics.drawLine(xs, ym, xe, ym);
        graphics.drawLine(xm, ys, xm, ye);

        return new ImageIcon(image);
    }

    /**
     * Creates an image with transition area between two colors. The transition
     * area is specified by the starting and ending columns (in pixel units).
     * The transition area is randomly covered by semi-randomly colored squares.
     *
     * @param width The width of the resulting image
     * @param height The height of the resulting image
     * @param leftColor The color on the left side of the resulting image
     * @param rightColor The color on the right side of the resulting image
     * @param transitionStart The starting column of the transition area. All
     *                        the pixels lying to the left of this column will
     *                        be colored uniformly by the left-side color
     * @param transitionEnd The ending column of the transition area. All the
     *                      pixels lying to the right of this column will be
     *                      colored uniformly by the right-side color
     * @return The resulting image
     */
    public static BufferedImage getGradientCubesImage(int width, int height,
            Color leftColor,
            Color rightColor,
            int transitionStart,
            int transitionEnd) {
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gradient =
                new GradientPaint(transitionStart, 0,
                leftColor,
                transitionEnd,
                0,
                rightColor);
        graphics.setPaint(gradient);
        graphics.fillRect(transitionStart, 0, transitionEnd - transitionStart,
                height);

        graphics.setColor(leftColor);
        graphics.fillRect(0, 0, transitionStart, height);

        graphics.setColor(rightColor);
        graphics.fillRect(transitionEnd, 0, width - transitionEnd, height);

        int cubeCountY = height / ImageCreator.CUBE_DIMENSION;
        int cubeCountX = 1 + (transitionEnd - transitionStart)
        / ImageCreator.CUBE_DIMENSION;
        int cubeStartY = (height % ImageCreator.CUBE_DIMENSION) / 2;
        int cubeStartX = transitionStart
                - (ImageCreator.CUBE_DIMENSION -
                ((transitionEnd - transitionStart) % ImageCreator.CUBE_DIMENSION));

        for (int col = 0; col < cubeCountX; col++) {
            for (int row = 0; row < cubeCountY; row++) {
                // decide if we should put a cube
                if (Math.random() < 0.5) {
                    continue;
                }
                // Make semi-random choice of color. It should lie
                // close to the interpolated color, but still appear
                // random
                double coef = 1.0 - (((double) col /
                        (double) cubeCountX) + 0.9 * (Math.random() - 0.5));
                coef = Math.max(0.0, coef);
                coef = Math.min(1.0, coef);
                // Compute RGB components
                int r = (int) (coef * leftColor.getRed() + (1.0 - coef)
                * rightColor.getRed());
                int g = (int) (coef * leftColor.getGreen() + (1.0 - coef)
                * rightColor.getGreen());
                int b = (int) (coef * leftColor.getBlue() + (1.0 - coef)
                * rightColor.getBlue());
                // fill cube
                graphics.setColor(new Color(r, g, b));
                graphics.fillRect(cubeStartX + col
                        * ImageCreator.CUBE_DIMENSION, cubeStartY + row
                        * ImageCreator.CUBE_DIMENSION,
                        ImageCreator.CUBE_DIMENSION,
                        ImageCreator.CUBE_DIMENSION);
                // draw cube's border in slightly brighter color
                graphics.setColor(new Color(255 - (int) (0.95 * (255 - r)),
                        255 - (int) (0.9 * (255 - g)),
                        255 - (int) (0.9 * (255 - b))));
                graphics.drawRect(cubeStartX + col
                        * ImageCreator.CUBE_DIMENSION, cubeStartY + row
                        * ImageCreator.CUBE_DIMENSION,
                        ImageCreator.CUBE_DIMENSION,
                        ImageCreator.CUBE_DIMENSION);
            }
        }

        return image;
    }

    /**
     * Retrieves an icon for <code>Increase font size</code> menu item.
     * Contains a single letter with upwards arrow.
     *
     * @param font The font of the letter
     * @param letter The letter to show
     * @return Icon for <code>Increase font size</code> menu item.
     */
    public static Icon getIconFontBigger(Font font, char letter) {
        BufferedImage image = new BufferedImage(ICON_DIMENSION, ICON_DIMENSION,
                BufferedImage.TYPE_INT_ARGB);

        // get graphics and set hints
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // make letter big and derive font
        letter = Character.toUpperCase(letter);
        Font dfont = font.deriveFont(Font.BOLD, ICON_DIMENSION);
        graphics.setFont(dfont);

        // compute the bounds of the letter
        FontRenderContext frc = graphics.getFontRenderContext();
        TextLayout mLayout = new TextLayout("" + letter, dfont, frc);
        Rectangle2D bounds = mLayout.getBounds();

        int w = (int) (Math.ceil(bounds.getWidth()));
        int h = (int) (Math.ceil(bounds.getHeight()));

        int x = 0;
        int y = ICON_DIMENSION - (ICON_DIMENSION - h) / 2;

        // draw the letter
        graphics.setColor(ImageCreator.mainUltraDarkColor);
        graphics.drawString("" + letter, x, y);

        // set the gradient
        int xs = x + w - 1;
        int ys = y - h;
        GradientPaint gradient = new GradientPaint(xs, ys,
                ImageCreator.mainLightColor, xs + 4, ys + 2,
                ImageCreator.mainDarkColor);
        graphics.setPaint(gradient);

        // draw the arrow head
        graphics.drawLine(xs, ys + 2, xs + 5, ys + 2);
        graphics.drawLine(xs + 1, ys + 1, xs + 4, ys + 1);
        graphics.drawLine(xs + 2, ys, xs + 3, ys);

        return new ImageIcon(image);
    }

    /**
     * Retrieves an icon for <code>Decrease font size</code> menu item.
     * Contains a single letter with downwards arrow.
     *
     * @param font The font of the letter
     * @param letter The letter to show
     * @return Icon for <code>Decrease font size</code> menu item.
     */
    public static Icon getIconFontSmaller(Font font, char letter) {
        BufferedImage image = new BufferedImage(ICON_DIMENSION, ICON_DIMENSION,
                BufferedImage.TYPE_INT_ARGB);

        // get graphics and set hints
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // make letter big and derive font
        letter = Character.toUpperCase(letter);
        Font dfont = font.deriveFont(Font.BOLD, ICON_DIMENSION - 2);
        graphics.setFont(dfont);

        // compute the bounds of the letter
        FontRenderContext frc = graphics.getFontRenderContext();
        TextLayout mLayout = new TextLayout("" + letter, dfont, frc);
        Rectangle2D bounds = mLayout.getBounds();

        int w = (int) (Math.ceil(bounds.getWidth()));
        int h = (int) (Math.ceil(bounds.getHeight()));

        int x = 0;
        int y = ICON_DIMENSION - (ICON_DIMENSION - h) / 2;

        // draw the letter
        graphics.setColor(ImageCreator.mainUltraDarkColor);
        graphics.drawString("" + letter, x, y);

        // set the gradient
        int xs = x + w;
        int ys = y - h;
        GradientPaint gradient = new GradientPaint(xs, ys,
                ImageCreator.mainLightColor, xs + 4, ys + 2,
                ImageCreator.mainDarkColor);
        graphics.setPaint(gradient);

        // draw the arrow head
        graphics.drawLine(xs, ys, xs + 5, ys);
        graphics.drawLine(xs + 1, ys + 1, xs + 4, ys + 1);
        graphics.drawLine(xs + 2, ys + 2, xs + 3, ys + 2);

        return new ImageIcon(image);
    }

    /**
     * Retrieves an icon for <code>Paint in color</code> menu item. Contains
     * two letters, one in gray, another in colored gradient.
     *
     * @param font The font of the letters
     * @param letter The letter to show
     * @return Icon for <code>Paint in color</code> menu item
     */
    public static Icon getIconFontPaint(Font font, char letter) {
        BufferedImage image = new BufferedImage(ICON_DIMENSION, ICON_DIMENSION,
                BufferedImage.TYPE_INT_ARGB);

        // get graphics and set hints
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // make letter big and derive font
        letter = Character.toUpperCase(letter);
        Font dfont = font.deriveFont(Font.BOLD, ICON_DIMENSION);
        graphics.setFont(dfont);

        // compute the bounds of the letter
        FontRenderContext frc = graphics.getFontRenderContext();
        TextLayout mLayout = new TextLayout("" + letter, dfont, frc);
        Rectangle2D bounds = mLayout.getBounds();

        int w = (int) (Math.ceil(bounds.getWidth()));
        int h = (int) (Math.ceil(bounds.getHeight()));

        int x = 0;
        int y = ICON_DIMENSION - (ICON_DIMENSION - h) / 2;

        // draw the letters
        graphics.setColor(Color.gray);
        graphics.drawString("" + letter, x, y);

        // set the gradient
        GradientPaint gradient = new GradientPaint(0, 0,
                ImageCreator.mainLightColor, 0, ICON_DIMENSION - 1,
                ImageCreator.mainDarkColor);
        graphics.setPaint(gradient);
        graphics.drawString("" + letter, ICON_DIMENSION - w - 1, h);

        return new ImageIcon(image);
    }

    /**
     * Retrieves an icon for <code>Change font</code> menu item. Contains two
     * letters, one in gray, another in black Serif font.
     *
     * @param font1 The font of the first letter
     * @param letter The letter to show
     * @return Icon for <code>Change font</code> menu item
     */
    public static Icon getIconChangeFont(Font font1, char letter) {
        BufferedImage image =
                new BufferedImage(ICON_DIMENSION, ICON_DIMENSION,
                BufferedImage.TYPE_INT_ARGB);

        // get graphics and set hints
        Graphics2D graphics = (Graphics2D) image.getGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // make letter big and derive font
        letter = Character.toUpperCase(letter);
        Font dfont1 = font1.deriveFont(Font.BOLD, ICON_DIMENSION);
        graphics.setFont(dfont1);

        // compute the bounds of the letter
        FontRenderContext frc = graphics.getFontRenderContext();
        TextLayout mLayout = new TextLayout("" + letter, dfont1, frc);
        Rectangle2D bounds = mLayout.getBounds();

        int h = (int) (Math.ceil(bounds.getHeight()));

        int x = 0;
        int y = ICON_DIMENSION - (ICON_DIMENSION - h) / 2;

        // draw the letters
        graphics.setColor(Color.gray);
        graphics.drawString("" + letter, x, y);

        Font dfont2 = new Font("Serif", Font.BOLD, ICON_DIMENSION);
        graphics.setFont(dfont2);

        FontRenderContext frc2 = graphics.getFontRenderContext();
        TextLayout mLayout2 = new TextLayout("" + letter, dfont2, frc2);
        Rectangle2D bounds2 = mLayout2.getBounds();

        int w2 = (int) (Math.ceil(bounds2.getWidth()));

        graphics.setColor(ImageCreator.mainUltraDarkColor);
        graphics.drawString("" + letter, ICON_DIMENSION - w2, y - 1);

        return new ImageIcon(image);
    }

    /**
     * Computes rounded version of the specified rectangle.
     *
     * @param rect Rectangle
     * @return Rounded version of the specified rectangle
     */
    public static Polygon getRoundedRectangle(Rectangle rect) {
        Polygon border = new Polygon();
        int offsetX = (int) rect.getX();
        int offsetY = (int) rect.getY();
        border.addPoint(offsetX, offsetY + 1);
        border.addPoint(offsetX + 1, offsetY);
        border.addPoint(offsetX + (int) rect.getWidth() - 1, offsetY);
        border.addPoint(offsetX + (int) rect.getWidth(), offsetY + 1);
        border.addPoint(offsetX + (int) rect.getWidth(), offsetY
                + (int) rect.getHeight() - 1);
        border.addPoint(offsetX + (int) rect.getWidth() - 1, offsetY
                + (int) rect.getHeight());
        border.addPoint(offsetX + 1, offsetY + (int) rect.getHeight());
        border.addPoint(offsetX, offsetY + (int) rect.getHeight() - 1);

        return border;
    }

    /**
     * Overlay one icon on another icon.
     *
     * @param icon1 the first icon
     * @param icon2 the second icon
     * @return the resulting icon
     */
    public static Icon overlay(Icon icon1, Icon icon2) {
        BufferedImage image = getBlankImage(icon2.getIconWidth() / 2
                + icon1.getIconWidth(), icon1.getIconHeight());
        // get graphics and set hints
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        icon1.paintIcon(null, graphics, icon2.getIconWidth() / 2, 0);
        icon2.paintIcon(null, graphics, 0, icon1.getIconHeight()
        - icon2.getIconHeight());
        return new ImageIcon(image);
    }

    /**
     * Create an icona image.
     *
     * @return the image
     */
    public static Image getIconImage() {
        BufferedImage result = getBlankImage(16, 16);
        Graphics2D g2 = (Graphics2D) result.getGraphics().create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        g2.setColor(new Color(140, 72, 170));
        g2.setFont(new Font("Tahoma", Font.BOLD, 17));
        g2.drawString("J", 6.0f, 14.0f);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Tahoma", Font.BOLD, 11));
        g2.drawString("w", 2.0f, 11.0f);

        g2.dispose();
        return result;
    }


    // ------------------------------------------------------ Protected Methods
    // none

    // -------------------------------------------------------- Private Methods
    // none
}
