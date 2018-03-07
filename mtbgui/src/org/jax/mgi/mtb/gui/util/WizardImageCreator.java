package org.jax.mgi.mtb.gui.util;

/*
 * Copyright (c) 2005-2006 Flamingo Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Flamingo Kirill Grouchnikov nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Provides utility functions for creating <code>ImageIcon</code> s and
 * <code>BufferedImage</code> s for the GUI.
 *
 * @author Kirill Grouchnikov
 */
public final class WizardImageCreator {
    /**
     * Returns a completely transparent image of specified dimensions.
     *
     * @param width
     *            Image width.
     * @param height
     *            Image height.
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
     * @param width
     *            The width of the output image.
     * @param height
     *            The height of the output image.
     * @return The resulting image.
     */
    public static Icon getStepImage(Color main, int width, int height, String string) {
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Color text = new Color(255 - main.getRed(), 255 - main.getGreen(),
                255 - main.getBlue());
        double coef = 0.9;
        Color dark = new Color((int) (coef * main.getRed()), (int) (coef * main
                .getGreen()), (int) (coef * main.getBlue()));
        Color light = new Color(255 - (int) (coef * (255 - main.getRed())),
                255 - (int) (coef * (255 - main.getGreen())),
                255 - (int) (coef * (255 - main.getBlue())));
        graphics.drawImage(getBackgroundStripedImage(width, height, light,
                main, dark), 0, 0, null);
        // graphics.setColor(ImageCreator.mainLightColor);
        // graphics.fillRect(0, 0, width, height);
        int cellCount = 4;
        int cellSizeX = width / cellCount;
        int cellSizeY = height / cellCount;
        for (int i = 0; i < cellCount; i++) {
            for (int j = 0; j < cellCount; j++) {
                int fontSize = (int) (15 + 15 * Math.random());

                // choose random color for text
                int r = (int) (text.getRed() + 20 - 40 * Math.random());
                r = Math.max(r, 0);
                r = Math.min(r, 255);
                int g = (int) (text.getGreen() + 20 - 40 * Math.random());
                g = Math.max(g, 0);
                g = Math.min(g, 255);
                int b = (int) (text.getBlue() + 20 - 40 * Math.random());
                b = Math.max(b, 0);
                b = Math.min(b, 255);
                graphics.setColor(new Color(r, g, b));

                // set the string's center to be in the center
                // of this cell
                graphics.setFont(new Font("Arial", Font.PLAIN, fontSize));
                FontRenderContext frc = graphics.getFontRenderContext();
                TextLayout mLayout = new TextLayout(string, graphics
                        .getFont(), frc);
                int x = (int) ((i + Math.random()) * cellSizeX - mLayout
                        .getBounds().getWidth() / 2);
                int y = (int) ((j + Math.random()) * cellSizeY);
                Composite c = AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, (float) (0.2 + 0.8 * Math
                        .random()));
                graphics.setComposite(c);
                graphics.drawString(string, x, y);
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
     * @param width
     *            The width of the output image.
     * @param height
     *            The height of the output image.
     * @param title
     *            Title string to write on the output image.
     * @return The resulting image.
     */
    public static Icon getTitleImage(int width, int height, String title,
            Color foregroundColor, Color shadowColor) {
        // get gradient background image
        BufferedImage image = WizardImageCreator.getBackgroundImage(width, height,
                false);

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // Find the bounds of the entire string.
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
     * @param width
     *            The width of the output image.
     * @param height
     *            The height of the output image.
     * @param lightColor
     *            The color of the left side of the even stripes.
     * @param midColor
     *            The color of the right side of the even stripes and the left
     *            side of the odd stripes.
     * @param darkColor
     *            The color of the right side of the odd stripes.
     * @return The resulting image.
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
            GradientPaint gpLeft = (row % 2 == 0) ? gpLightLeft : gpDarkLeft;
            graphics.setPaint(gpLeft);
            graphics.drawLine(0, row, width / 2, row);
            GradientPaint gpRight = (row % 2 == 0) ? gpLightRight : gpDarkRight;
            graphics.setPaint(gpRight);
            graphics.drawLine(width / 2, row, width, row);
        }

        return image;
    }

    /**
     * Returns an image of specified dimensions that has gradient background and
     * an optional gradient separator stripe on its upper border.
     *
     * @param width
     *            The width of the output image.
     * @param height
     *            The height of the output image.
     * @param hasStripeTop
     *            if <code>true</code>, a gradient stripe few pixels high is
     *            added on the upper border of this image.
     * @return The resulting image.
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
     * @param str
     *            The input string to be shown,.
     * @param width
     *            The width of the output image.
     * @return The resulting image.
     */
    public static Icon getMultiline(String str, int width,
            Color foregroundColor, Color shadowColor) {
        // first break up the string into lines, so that each line is no
        // longer than specified width
        BufferedImage tempImage = new BufferedImage(width, 20,
                BufferedImage.TYPE_INT_ARGB);
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

        ArrayList lines = new ArrayList();
        //LinkedList<String> lines = new LinkedList<String>();
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
                //lines.addLast(currLine);
                lines.add(currLine);
                currLine = currToken;
            } else {
                currLine = newLine;
            }
        }
        // add the last one
        //lines.addLast(currLine);
        lines.add(currLine);

        // count the number of lines
        int lineCount = lines.size();
        FontMetrics fm = tempGraphics.getFontMetrics();
        int height = lineCount * (fm.getHeight() + 5);

        // create new image
        BufferedImage image = WizardImageCreator.getBackgroundImage(width, height,
                false);
        // write all strings
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        graphics.setFont(font);
        int ypos = fm.getHeight();
        //for (String line : lines) {
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
     * Creates a one-pixel high gradient image of specified width, opacity and
     * colors of the starting pixel and the ending pixel.
     *
     * @param width
     *            The width of the resulting image.
     * @param leftColor
     *            The color of the first pixel of the resulting image.
     * @param rightColor
     *            The color of the last pixel of the resulting image.
     * @param opacity
     *            The opacity of the resulting image (in 0..1 range). The
     *            smaller the value, the more transparent the resulting image
     *            is.
     * @return A one-pixel high gradient image of specified width, opacity and
     *         colors of the starting pixel and the ending pixel.
     */
    public static BufferedImage createGradientLine(int width, Color leftColor,
            Color rightColor, double opacity) {
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
}
