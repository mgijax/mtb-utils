/*
 * DynamicText.java
 *
 * Created on June 27, 2006, 10:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jax.mgi.mtb.webutils.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mjv
 */
public class DynamicText extends HttpServlet {
  
    private static Font baseFont;

    // font file included in the jar
    private static final String base_font_file = "fonts/tahoma.ttf";
    
    // This method is called by the servlet container just before this servlet
    // is put into service. Note that it is possible that more than one
    // instance of this servlet can be created in the same VM.
    public void init() throws ServletException {
      
      // initalize the static font
      // Font.createFont creates a file in the temp dir. If it happens for
      // each request there would be lots of files that don't go away
      // maybe fixed in java 6?
      if(baseFont == null){
        try {
              testTempDir();
              ClassLoader cl = this.getClass().getClassLoader(); 
              InputStream is = cl.getResourceAsStream(base_font_file);
              baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
              is.close();
          } catch (Exception ex) { 
              ex.printStackTrace();
          }
      }
        
    }
    
    // This method is called by the servlet container to process a GET req.
    // There may be many threads calling this method simultaneously.
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        
        // configure all of the parameters
        String text = "ABC abc XYZ xyz";
        if(req.getParameter("text") != null) {
            text = req.getParameter("text");
        }
        
        Font tempFont = null;
        String font_file = null;
        
        if(req.getParameter("font-file") != null) {
            font_file = req.getParameter("font-file");
            
            // load a custom font
            try {
              testTempDir();
              ClassLoader cl = this.getClass().getClassLoader(); 
              InputStream is = cl.getResourceAsStream(font_file);
              tempFont = Font.createFont(Font.TRUETYPE_FONT, is);
              is.close();
          } catch (Exception ex) { 
              ex.printStackTrace();
          }
        }
        
        float size = 20.0f;
        if(req.getParameter("size") != null) {
            size = Float.parseFloat(req.getParameter("size"));
        }
        
        Color background = Color.white;
        if(req.getParameter("background") != null) {
            background = new Color(Integer.parseInt(
                    req.getParameter("background"),16));
        }
        Color color = Color.black;
        if(req.getParameter("color") != null) {
            color = new Color(Integer.parseInt(
                    req.getParameter("color"),16));
        }
        
        if(tempFont == null)
        {
          tempFont = baseFont.deriveFont(size);
        }else{
          tempFont = tempFont.deriveFont(size);
        }
        
        BufferedImage buffer =
                new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = buffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        FontRenderContext fc = g2.getFontRenderContext();
        Rectangle2D bounds = tempFont.getStringBounds(text,fc);
        
        // calculate the size of the text
        int height = (int) bounds.getHeight();
        int width = (int) bounds.getWidth();
        
        // prepare some output
        buffer = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        g2 = buffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(tempFont);
        
        // actually do the drawing
        g2.setColor(background);
        g2.fillRect(0,0,width,height);
        g2.setColor(color);
        g2.drawString(text,0,(int)-bounds.getY());
        
        BufferedImage rotatedImage = rotate(buffer, 270.0, Color.white);
        
        // set the content type and get the output stream
        resp.setContentType("image/png");
        resp.setHeader("Content-Disposition", "attachment; filename=" + replace(text, " ", "") + ".png");
        
        OutputStream os = resp.getOutputStream();
        
        // output the image as png
        ImageIO.write(rotatedImage, "png", os);
        os.close();
        
    }
    
    // This method is called by the servlet container just after this servlet
    // is removed from service.
    public void destroy() {
        // Shutdown code...
    }
    
    private void testTempDir(){
      // the Font.createFont method writes the font to
      // java.io.tmpdir, if that doesn't exist it throws an exception.
      // Test to see if the dir is there and if not, make it.
      File f = new File(System.getProperty("java.io.tmpdir"));
      if(!f.canRead()){
        f.mkdir();
      }
    }
    
    /**
     * Rotates the current JpgImage object a specified number of degrees.
     * <p>
     * You should be aware of 2 things with regard to image rotation.
     * First, the more times you rotate an image, the more the image
     * degrades. So instead of rotating an image 90 degrees and then
     * rotating it again 45 degrees, you should rotate it once at a
     * 135 degree angle.
     * <p>
     * Second, a rotated image will always have a rectangular border
     * with sides that are vertical and horizontal, and all of the area
     * within this border will become part of the resulting image.
     * Therefore, if you rotate an image at an angle that's not a
     * multiple of 90 degrees, your image will appear to be placed
     * at an angle against a rectangular background of the specified Color.
     * For this reason, an image rotated 45 degrees and then another 45 degrees
     * will not be the same as an image rotated 90 degrees.
     *
     * @param  degrees    the number of degrees to rotate the image
     * @param  backgroundColor    the background color used for areas
     *                            in the resulting image that are not
     *                            covered by the image itself
     */
    public BufferedImage rotate(BufferedImage bi, double degrees, Color backgroundColor) {
        /*
         * Okay, this required some strange geometry. Before an image
         * is rotated, the origin is at the top left corner of the
         * rectangle that contains the image. After an image is rotated,
         * you want the origin to get moved to a spot that will allow
         * the entire rotated image to be framed within a rectangle.
         * Unfortunately, this does not happen automatically.
         *
         * That's where the strange geometry comes in. We essentially
         * need to rotate the image, and then determine what the width
         * and height of the new image is, and then determine where the
         * new origin should be. The width and height is easy (you can
         * also use the AffineTransform getWidth and getHeight methods),
         * but the new origin...well...not so easy. Unfortunately, my
         * trigonometry skills aren't sharp enough to be able to give you
         * a good explanation of what's going on with this method without
         * drawing everything out for you. If you want to figure it out
         * for yourself, just draw an axis on a sheet of paper, place a
         * smaller rectangular piece of paper on the axis, and start
         * rotating it along the axis to see what's going on. Then pull
         * out your old trig books and start calculating.
         *
         * BTW, if there's an easier way to do this, I'd love to know about it.
         */
        
        // adjust the angle that was passed so it's between 0 and 360 degrees
        double positiveDegrees = (degrees % 360) + ((degrees < 0) ? 360 : 0);
        double degreesMod90 = positiveDegrees % 90;
        double radians = Math.toRadians(positiveDegrees);
        double radiansMod90 = Math.toRadians(degreesMod90);
        
        // don't bother with any of the rest of this if we're not really rotating
        if (positiveDegrees == 0) {
            return bi;
        }
        
        // figure out which quadrant we're in (we'll want to know this later)
        int quadrant = 0;
        if (positiveDegrees < 90) {
            quadrant = 1;
        } else if ((positiveDegrees >= 90) && (positiveDegrees < 180)) {
            quadrant = 2;
        } else if ((positiveDegrees >= 180) && (positiveDegrees < 270)) {
            quadrant = 3;
        } else if (positiveDegrees >= 270) {
            quadrant = 4;
        }
        
        // get the height and width of the rotated image (you can also do this
        // by applying a rotational AffineTransform to the image and calling
        // getWidth and getHeight against the transform, but this should be a
        // faster calculation)
        int height = bi.getHeight();
        int width = bi.getWidth();
        double side1 = (Math.sin(radiansMod90) * height) + (Math.cos(radiansMod90) * width);
        double side2 = (Math.cos(radiansMod90) * height) + (Math.sin(radiansMod90) * width);
        
        double h = 0;
        int newWidth = 0, newHeight = 0;
        if ((quadrant == 1) || (quadrant == 3)) {
            h = (Math.sin(radiansMod90) * height);
            newWidth = (int)side1;
            newHeight = (int)side2;
        } else {
            h = (Math.sin(radiansMod90) * width);
            newWidth = (int)side2;
            newHeight = (int)side1;
        }

        // figure out how much we need to shift the image around in order to
        // get the origin where we want it
        int shiftX = (int)(Math.cos(radians) * h) - ((quadrant == 3) || (quadrant == 4) ? width : 0);
        int shiftY = (int)(Math.sin(radians) * h) + ((quadrant == 2) || (quadrant == 3) ? height : 0);

        // create a new BufferedImage of the appropriate height and width and
        // rotate the old image into it, using the shift values that we calculated
        // earlier in order to make sure the new origin is correct
        BufferedImage newbi = new BufferedImage(newWidth, newHeight, bi.getType());
        Graphics2D g2d = newbi.createGraphics();
        g2d.setBackground(backgroundColor);
        g2d.clearRect(0, 0, newWidth, newHeight);
        g2d.rotate(radians);
        g2d.drawImage(bi, shiftX, -shiftY, null);
        return newbi;

    }

  /**
     * In String string, replace all occurances of s1 with s2. The match is
     * case sensitive, but s1 and s1 need not be of the same length.
     *
     * @param string The String to perform the replace on
     * @param s1 The String to find
     * @param s3 The String to replace with
     *
     * @return The altered String
     */
    private static String replace(String string, String s1, String s2) {
        while(string.indexOf(s1) != -1) {
            String pre = string.substring(0, string.indexOf(s1));
            String suf = string.substring(string.indexOf(s1) + s1.length());

            string = pre + s2 + suf;
        }

        return string;
    }
}
