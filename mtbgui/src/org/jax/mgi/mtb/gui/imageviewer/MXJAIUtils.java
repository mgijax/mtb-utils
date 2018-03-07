/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/imageviewer/MXJAIUtils.java,v 1.1 2008/08/01 12:32:04 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui.imageviewer;

import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;
import com.sun.media.jai.codec.TIFFDirectory;
import java.awt.Image;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;

/**
 * <p>A simple class which uses the JAI codecs to load images from image files.
 * It is designed to supply the {@link ImageViewer ImageViewer} with image 
 * sources for it to display.</p>
 *
 * <p>Any image that is supported by JAI codecs can be loaded through this 
 * class. And because image loading is encapsulated in this class, it can be 
 * moved over to the new ImageIO framework with relative ease at a later date.
 * </p>
 *
 * <p>All the methods are currently static.</p>
 *
 * @author $Author: sbn $
 * @date $Date: 2008/08/01 12:32:04 $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/imageviewer/MXJAIUtils.java,v 1.1 2008/08/01 12:32:04 sbn Exp $
 */
public class MXJAIUtils {
    
    // -------------------------------------------------------------- Constants
    // none
    
    // ----------------------------------------------------- Instance Variables
    // none
    
    // ----------------------------------------------------------- Constructors
    // none
    
    // --------------------------------------------------------- Public Methods
    
    /**
     * Loads an image file from a URL and returns an array of PlanarImage 
     * objects representing all the images found in the file.
     *
     * @param pURL The URL of the image file to load.
     */
    public static PlanarImage[] load(URL pURL)
                                   throws IOException {
        try {
            return load(pURL.toString());
        }
        catch (MalformedURLException ex) {
            //
            // Highly unlikely to occur. The URL argument is by
            // definition valid. Converting it to a string will
            // not suddenly make it invalid. So, unless the
            // load(String url) method is messed up, this exception
            // will never be thrown.
            //
            ex.printStackTrace();
        }
        
        return null;
    }

    /**
     * Loads an image file from a URL and returns an array of PlanarImage 
     * objects representing all the images found in the file.
     *
     * @param pURL The URL of the image file as a string.
     * @throws MalformedURLException if the 'pURL' parameter is not
     * a valid URL.
     */
    public static PlanarImage[] load(String pURL)
                                   throws MalformedURLException, IOException {
        //
        // URL.openStream() does not like spaces in the path, so
        // replace them with "%20".
        //
        URL urlObject = new URL(encodeURL(pURL));

        //
        // Now delegate to the load method which takes an InputStream
        // as an argument.
        //
        return load(urlObject.openStream());
    }

    /**
     * Loads an image file from an input stream and returns an array of 
     * PlanarImage objects representing all the images found in the stream.
     *
     * @param pImageStream the stream to read the image information from.
     */
    public static PlanarImage[] load(InputStream pImageStream)
                                   throws IOException {
        // Open the image file from a seekable stream
        ParameterBlock pb = new ParameterBlock();
        SeekableStream stream =
                SeekableStream.wrapInputStream(pImageStream, true);
        pb.add(stream);

        // Load the image from the stream
        PlanarImage source = JAI.create("stream", pb);

        // Check for a multi-page (TIFF) image. If it is one, load
        // all the pages.
        Object prop = source.getProperty("tiff_directory");
        if (prop == Image.UndefinedProperty) {
            // Just return the image that has already been loaded
            return new PlanarImage[] { source };
        } else {
            return handleTIFF(stream, source);
        }
    }

    // ------------------------------------------------------ Protected Methods
    // none
    
    // -------------------------------------------------------- Private Methods
    
    /**
     * Loads a TIFF and stores all the images and information from it. Note 
     * that it assumes that the first image has already been read from the 
     * stream.
     *
     * @param pStream the stream that contains the TIFF file data
     * @param pFirstImage the first image extracted from the stream
     */
    private static PlanarImage[] handleTIFF(SeekableStream pStream,
                                            PlanarImage pFirstImage)
                                    throws IOException {
        // Get the number of images in this file
        int numPages = TIFFDirectory.getNumDirectories(pStream);

        // Get the first IFD.
        TIFFDirectory dir = (TIFFDirectory)
                            pFirstImage.getProperty("tiff_directory");

        // Initialise a new array of images and set the first
        // item to the first image.
        PlanarImage[] images = new PlanarImage[numPages];
        images[0] = pFirstImage;

        // Now load the rest of the images and store them
        if (numPages > 1) {
            ParameterBlock pb = new ParameterBlock();
            pb.add(pStream);

            TIFFDecodeParam param = new TIFFDecodeParam();
            pb.add(param);

            long nextOffset = dir.getNextIFDOffset();
            int i = 1;
            while (nextOffset != 0) {
                // Get the next image
                param.setIFDOffset(nextOffset);
                PlanarImage image = JAI.create("tiff", pb);

                // Now store it in the array of images
                images[i] = image;

                // Now retrieve the IFD for this image so we can find
                // the next one.
                dir = (TIFFDirectory)image.getProperty("tiff_directory");
                nextOffset = dir.getNextIFDOffset();
            }
        }

        return images;
    }

    /**
     * All this method does is replace all instances of the 'space' character 
     * in the url with "%20". This is not full encoding, which would normally 
     * replace other characters like :, /, & plus others.
     *
     * @param pURL The URL to encode, in string form.
     * @return the encoded URL.
     */
    private static String encodeURL(String pURL) {
        final StringBuffer urlBuffer = new StringBuffer(pURL);

        //
        // Iterate through the URL, replacing all instances of ' '
        // with "%20".
        //
        int i = 0;
        while (i < urlBuffer.length()) {
            if (urlBuffer.charAt(i) == ' ') {
                urlBuffer.replace(i, i + 1, "%20");
                i += 3;
            }
            else {
                i++;
            }
        }

        return urlBuffer.toString();
    }
}
