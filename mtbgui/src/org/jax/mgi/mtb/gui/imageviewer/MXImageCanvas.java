/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/imageviewer/MXImageCanvas.java,v 1.1 2008/08/01 12:32:04 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui.imageviewer;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import javax.media.jai.PlanarImage;
import javax.swing.JComponent;

/**
 * <p>This class is based on the ImageDisplay widget, the source for
 * which comes with the JAI tutorial, authored by Daniel Rice and Dennis
 * Sigel. Some stuff has been removed, some bugs have been fixed. It is
 * effectively a straight canvas for rendering a PlanarImage, although it
 * does have an 'origin' property. This property defines where the origin
 * of the image is relative to the top-left corner of the canvas. By
 * changing the origin, you can produce a panning effect. One other effect
 * is that the image can be centered within the canvas; to enable this just
 * use <code>setImageCentered(true)</code>.</p>
 * 
 * <p>The MXImageCanvas itself subclasses javax.swing.JComponent, allowing
 * it to be used within any standard Swing GUI. One other feature is that
 * it only renders tiles that are visible, and it only retrieves the tiles
 * on demand. Unfortunately, due to the limitations of BufferedImage,
 * only TYPE_BYTE of band 1, 2, 3, 4, and TYPE_USHORT of band 1, 2, 3
 * images can be displayed using this widget. I have not managed to work
 * out a way around this yet.</p>
 * 
 * @author $Author: sbn $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/imageviewer/MXImageCanvas.java,v 1.1 2008/08/01 12:32:04 sbn Exp $
 * @date $Date: 2008/08/01 12:32:04 $
 */
public class MXImageCanvas extends JComponent {
    
    // -------------------------------------------------------------- Constants
    
    /** 
     * Property used when the source image for the canvas
     * changes.
     */
    public static final String IMAGE_PROPERTY = "imageProperty";
    
    /**
     * Property used when the image's origin changes.
     */
    public static final String ORIGIN_PROPERTY = "originProperty";

    
    // ----------------------------------------------------- Instance Variables
    
    /** 
     * The source image to render. 
     */
    private PlanarImage mSource;

    /**
     * The X coordinate of the origin for the image. This is the position of 
     * the image relative to the left edge of the canvas. Therefore a negative
     * value for this property will result in the left side of the image 
     * appearing to reside outside of the canvas. A positive value will make
     * a border appear between the edge of the canvas and the edge of the 
     * image.
     */
    private int mOriginX = 0;

    /**
     * The Y coordinate of the origin for the image, relative to the top edge 
     * of the canvas. See mOriginX for more details.
     */
    private int mOriginY = 0;

    /**
     * Determines whether the image should be centered in the canvas if the 
     * canvas is larger than the image.
     */
    private boolean mIsImageCentered = true;

    /**
     * The SampleModel used by the current image.
     */
    private SampleModel mSampleModel;

    /**
     * The ColorModel used by the current image.
     */
    private ColorModel mColorModel;

    // ----------------------------------------------------------- Constructors
    
    /**
     * Default constructor that creates a canvas 64x64 pixels in size.
     */
    public MXImageCanvas() {
        this(64, 64);
    }

    /**
     * Constructs a canvas with a given fixed size, without an image.
     *
     * @param pWidth The width of the new canvas.
     * @param pHeight The height of the new canvas.
     */
    public MXImageCanvas(int pWidth, int pHeight) {
        this(null);
        setSize(pWidth, pHeight);
        setPreferredSize(getSize());
    }

    /**
     * Constructs a canvas with an image to render on it. The size of the 
     * canvas is taken from the size of the image.
     *
     * @param pImage The image to render on the canvas.
     */
    public MXImageCanvas(PlanarImage pImage) {
        super();
        setImage(pImage);
        setOrigin(0, 0);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }


    // --------------------------------------------------------- Public Methods
    
    /** 
     * Records a new size. Called by the AWT. 
     */
    public void setBounds(int x, int y, int width, int height) {
        final Insets insets = getInsets();
        int w, h;

        if (mSource != null) {
            w = mSource.getWidth();
            h = mSource.getHeight();
            if ( width < w ) {
                width = w;
            }

            if ( height < h ) {
                height = h;
            }
        }

        w = width + insets.left + insets.right;
        h = height + insets.top  + insets.bottom;
        super.setBounds(x, y, w, h);

        // Reposition the origin if necessary
        if (mSource != null && mIsImageCentered) {
            int transX = (getWidth() - mSource.getWidth()) / 2;
            transX = Math.max(transX, 0);
            int transY = (getHeight() - mSource.getHeight()) / 2;
            transY = Math.max(transY, 0);
            setOrigin(transX, transY);
        }
    }

    /**
     * Paint the image onto a Graphics object. The painting is performed 
     * tile-by-tile, and includes a grey region covering the unused portion of 
     * image tiles as well as the general background. At this point the image 
     * must be byte data.
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2D = null;
        if (g instanceof Graphics2D) {
            g2D = (Graphics2D)g;
        } else {
            return;
        }

        // If 'mSource' is null, it's just a component
        if (mSource == null) {
            g2D.setColor(getParent().getBackground());
            g2D.fillRect(0, 0, getWidth(), getHeight());
            return;
        }

        // Get the clipping rectangle and translate it into image coordinates.
        Rectangle clipBounds = g2D.getClipBounds();

        if (clipBounds == null) {
            clipBounds = new Rectangle(0, 0, getWidth(), getHeight());
        }
        else {
            clipBounds = (Rectangle)clipBounds.createIntersection(
                                           new Rectangle(mOriginX,
                                                         mOriginY,
                                                         mSource.getWidth(),
                                                         mSource.getHeight()));
            g2D.setClip(clipBounds);
        }

        // Sneaky! This is effectively a reuse of a Rectangle instance
        // and basically moves the clipping rectangle from canvas
        // coordinates to image coordinates. Note that this does not
        // actually modify the clipping rectangle itself in any way!
        clipBounds.translate(mOriginX, mOriginY);

        // Determine the extent of the clipping region in tile coordinates.
        int txmin, txmax, tymin, tymax;
        int ti, tj;

        txmin = mSource.XToTileX(clipBounds.x);
        txmin = Math.max(txmin, mSource.getMinTileX());
        txmin = Math.min(txmin, mSource.getMaxTileX());

        txmax = mSource.XToTileX(clipBounds.x + clipBounds.width - 1);
        txmax = Math.max(txmax, mSource.getMinTileX());
        txmax = Math.min(txmax, mSource.getMaxTileX());

        tymin = mSource.YToTileY(clipBounds.y);
        tymin = Math.max(tymin, mSource.getMinTileY());
        tymin = Math.min(tymin, mSource.getMaxTileY());

        tymax = mSource.YToTileY(clipBounds.y + clipBounds.height - 1);
        tymax = Math.max(tymax, mSource.getMinTileY());
        tymax = Math.min(tymax, mSource.getMaxTileY());
        Insets insets = getInsets();

        // Loop over tiles within the clipping region
        for (tj = tymin; tj <= tymax; tj++) {
            for (ti = txmin; ti <= txmax; ti++) {
                int tx = mSource.tileXToX(ti);
                int ty = mSource.tileYToY(tj);

                Raster tile;
                try {
                    tile = mSource.getTile(ti, tj);
                }
                catch (ArrayIndexOutOfBoundsException ex) {
                    // Bug in nearest neighbour interpolation scaling
                    ex.printStackTrace();
                    tile = null;
                }

                if (tile != null) {
                    DataBuffer dataBuffer = tile.getDataBuffer();

                    WritableRaster wr = tile.createWritableRaster(mSampleModel,
                                                                  dataBuffer,
                                                                  null);

                    BufferedImage bi = new BufferedImage(mColorModel, wr,
                                         mColorModel.isAlphaPremultiplied(),
                                         null);

                    AffineTransform transform;
                    transform = AffineTransform.getTranslateInstance(
                                                 tx + insets.left + mOriginX,
                                                 ty + insets.top + mOriginY);

                    g2D.drawRenderedImage(bi, transform);
                }
            }
        }
    }

    /**
     * Returns the image currently being displayed by the canvas.
     */
    public PlanarImage getImage() {
        return mSource;
    }

    /**
     * Updates the image being displayed by the canvas.
     * <code>null</code> is a valid value and will clear the canvas.
     *
     * @param pImage The source image to display in the canvas.
     */
    public void setImage(PlanarImage pImage) {
        // Change the image, reinitialise the canvas and then redraw it
        final PlanarImage oldImage = mSource;
        mSource = pImage;
        initialize();
        revalidate();
        repaint();

        // Inform interested parties of the change
        firePropertyChange(IMAGE_PROPERTY, oldImage, pImage);
    }

    /**
     * Returns the current image origin.
     *
     * @retrun the point of origin
     */
    public Point2D getOrigin() {
        return new Point(mOriginX, mOriginY);
    }

    /**
     * Change the image origin. This allows for a panning effect.
     *
     * @param pOrigin The new origin as a Point2D object.
     */
    public void setOrigin(Point2D pOrigin) {
        setOrigin((int) pOrigin.getX(), (int) pOrigin.getY());
    }

    /**
     * Change the image origin. This allows for a panning effect.
     *
     * @param pX The new origin x coordinate
     * @param pY The new origin y coordinate
     */
    public void setOrigin(int pX, int pY) {
        final Point oldOrigin = new Point(mOriginX, mOriginY);

        // Shift to box origin
        mOriginX = pX;
        mOriginY = pY;

        // Update any interested listeners
        firePropertyChange(ORIGIN_PROPERTY, oldOrigin, new Point(pX, pY));
        repaint();
    }

    /**
     * Returns <code>true</code> if the image is centered in the canvas,
     * otherwise it returns <code>false</code>
     *
     * @return if the image is centered or not
     */
    public boolean isImageCentered() {
        return mIsImageCentered;
    }

    /**
     * Sets whether the image should be centered in the canvas
     * or not.
     *
     * @param isCentered if the image is centered or not
     */
    public void setImageCentered(boolean isCentered) {
        if (isCentered != mIsImageCentered) {
            mIsImageCentered = isCentered;
            repaint();
        }
    }


    // ------------------------------------------------------ Protected Methods
    // none
    
    // -------------------------------------------------------- Private Methods
    
    /**
     * 
     * Initializes the MXImageCanvas.
     */
    private void initialize() {
        if (mSource == null) {
            return;
        }

        setSize(mSource.getWidth(), mSource.getHeight());
        setPreferredSize(getSize());

        // Store the sample model used by the current image
        mSampleModel = mSource.getSampleModel();

        // Store the colour model used by the current image.
        // If it doesn't have one, create one from the sample
        // model
        mColorModel = mSource.getColorModel();
        if (mColorModel == null) {
            // If not, then create one.
            mColorModel = PlanarImage.createColorModel(mSampleModel);
            if (mColorModel == null) {
                throw new IllegalArgumentException("no color model");
            }
        }
    }
}