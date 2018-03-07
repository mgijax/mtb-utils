/**
 * Header: $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/imageviewer/MXImagePanel.java,v 1.1 2008/08/01 12:32:04 sbn Exp $
 * Author: $Author: sbn $
 */
package org.jax.mgi.mtb.gui.imageviewer;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * <p>The <code>MXImageCanvas</code> class will display a PlanarImage
 * and that's about it. <code>MXImagePanel</code> is effectively a
 * wrapper for it, providing rotation, scaling and printing capabilities.
 * It can also view multiple pages of an image.</p>
 * 
 * <p>Original concept taken from Peter Ledbrook.</p>
 * 
 * @author $Author: sbn $
 * @version $Revision: 1.1 $
 * @cvsheader $Header: /mgi/cvsroot/mgi/mtb/mtbgui/src/org/jax/mgi/mtb/gui/imageviewer/MXImagePanel.java,v 1.1 2008/08/01 12:32:04 sbn Exp $
 * @date $Date: 2008/08/01 12:32:04 $
 */
public class MXImagePanel extends JPanel {

    // -------------------------------------------------------------- Constants
    
    /** 
     * The zoom level property 
     */
    public static final String ZOOM_PROPERTY = "zoomProperty";

    /** 
     * The angle of rotation property 
     */
    public static final String ROTATION_PROPERTY = "rotationProperty";

    /** 
     * The property for the index of the image being display 
     */
    public static final String INDEX_PROPERTY = "imageIndexProperty";

    
    // ----------------------------------------------------- Instance Variables
    
    /** 
     * Stores the current scaling regime 
     */
    private ScalingType mScalingType = ScalingType.NORMAL;

    /**
     * The current zoom factor. A value of 1 is full scale, a value of 2
     * doubles the visible size of the image (zoom in) and a value of 0.5
     * halves the size of the image (zoom out).
     */
    private double mCurrZoomLevel = 1.0;

    /**
     * The current angle of rotation on the image, in radians.
     */
    private double mRotation = 0.0;

    /**
     * Determines whether sub-sampling-to-gray should be used
     * when scaling black and white images. By default sub-sampling
     * will be used.
     */
    private boolean mUseSubSampling = true;

    /**
     * The canvas used to actually display the image.
     */
    private MXImageCanvas mCanvas;

    /**
     * An array of source images to display. These are used as
     * the source for the rotation and scaling.
     */
    private PlanarImage[] mSourceImages;

    /**
     * The index of the image being displayed, starting at 0
     * for the first image in the array.
     */
    private int mImageIndex = 0;


    // ----------------------------------------------------------- Constructors
    
    /**
     * Default constructor.
     */
    public MXImagePanel() {
        super();

        // Create the canvas for the image and install it in the center
        mCanvas = new MXImageCanvas();
        setLayout(new BorderLayout());
        add(new JScrollPane(mCanvas), BorderLayout.CENTER);
    }

    /**
     * Constructor with a specified width and height.
     *
     * @param w The width
     * @param h The height
     */
    public MXImagePanel(int w, int h) {
        super();

        // Create the canvas for the image and install it in the center
        mCanvas = new MXImageCanvas(w, h);
        setLayout(new BorderLayout());
        add(new JScrollPane(mCanvas), BorderLayout.CENTER);
    }
    
    
    // --------------------------------------------------------- Public Methods
    
    /**
     * Refresh the image based upon a scale factor.
     *
     * @param scaleFactor the scaling factor
     */
    public void refresh(float scaleFactor) {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(mSourceImages[0]);
        pb.add(scaleFactor);
        pb.add(scaleFactor);
        pb.add(0.0F);
        pb.add(0.0F);
        pb.add(new InterpolationNearest());
        
        try {
            // Creates a new, scaled image and uses it on the DisplayJAI component
            PlanarImage scaledImage = JAI.create("scale", pb);
            setImage(scaledImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the current source image being displayed. Note that this is the
     * <em>source</em> image, not the (probably) transformed one on the canvas.
     *
     * @return the current source PlanarImage
     */
    public PlanarImage getCurrentImage() {
        return mSourceImages[mImageIndex];
    }

    /**
     * Returns the index of the currently display image in the source image 
     * array.
     *
     * @return the current image index
     */
    public int getImageIndex() {
        return mImageIndex;
    }

    /**
     * Set which image index to display in the canvas. The first image has an 
     * index of 0.
     *
     * @param pIndex the index of the image to show.
     * @throws IndexOutOfBoundsException if the index is invalid
     * for the array of source images.
     */
    public void setImageIndex(int pIndex) {
        if (pIndex >= mSourceImages.length) {
            throw new IndexOutOfBoundsException();
        }

        if (pIndex != mImageIndex) {
            final int oldIndex = mImageIndex;
            mImageIndex = pIndex;
            firePropertyChange(INDEX_PROPERTY, oldIndex, pIndex);
        }
    }

    /**
     * Returns the array of source images currently attached to this viewer.
     *
     * @return an array of PlanarImage objects
     */
    public PlanarImage[] getImages() {
        return (PlanarImage[]) mSourceImages.clone();
    }

    /**
     * Sets the source image for this viewer. This replaces the existing array 
     * of source images in this viewer.
     *
     * @param pImage the new source image.
     */
    public void setImage(PlanarImage pImage) {
        setImages(new PlanarImage[] { pImage }, 0);
    }

    /**
     * Replaces the existing array of source images with a new one and displays
     * the first image in the array.
     *
     * @param pImages the array of images to use as sources.
     */
    public void setImages(PlanarImage[] pImages) {
        setImages(pImages, 0);
    }

    /**
     * Replaces the existing array of source images with a new one and displays
     * the image at the given index.
     *
     * @param images the array of images to use as sources
     * @param index the index of the image to display
     * @throws IndexOutOfBoundsException
     */
    public void setImages(PlanarImage[] pImages, int pIndex)
                                       throws IndexOutOfBoundsException {
        if (pIndex >= pImages.length) {
            throw new IndexOutOfBoundsException();
        }

        // Store the array of images and the index of the image
        // to display
        mSourceImages = (PlanarImage[]) pImages.clone();
        mImageIndex = pIndex;

        // Since we're changing the image source, reset rotation and
        // scaling factors. This also causes the whole thing to be
        // rendered, which will thus update the image canvas with the
        // new image.
        reset();
        validate();
        repaint();
    }

    /**
     * Returns the current angle of rotation on the image, in radians.
     *
     * @return the current angle in radians
     */
    public double getRotation() {
        return mRotation;
    }

    /**
     * Sets the current angle of rotation on the displayed image and fires a 
     * property change event.
     *
     * @param pRotation the angle of rotation to apply to the image.
     */
    public void setRotation(double pRotation) {
        if (pRotation != mRotation) {
            final Double oldRotation = new Double(mRotation);
            mRotation = pRotation;
            render();
            firePropertyChange(ROTATION_PROPERTY, oldRotation,
                                                  new Double(pRotation));
        }
    }

    /**
     * Returns the current zoom level (or scaling factor).
     *
     * @return the zoome level
     */
    public double getZoom() {
        return mCurrZoomLevel;
    }

    /**
     * Sets the current zoom level (or scaling factor).
     *
     * @param pZoom the new zoom level.
     */
    public void setZoom(double pZoom) {
        if (pZoom != mCurrZoomLevel) {
            final Double oldZoom = new Double(mCurrZoomLevel);
            mCurrZoomLevel = pZoom;
            render();
            firePropertyChange(ZOOM_PROPERTY, oldZoom, new Double(pZoom));
        }
    }

    /**
     * Returns <code>true</code> if the sub-sampling to gray is used when 
     * scaling black and white images. Otherwise it returns <code>false</code>.
     *
     * @return whether or not subsampling is used
     */
    public boolean isSubSamplingUsed() {
        return mUseSubSampling;
    }

    /**
     * Sets whether sub-sampling to gray is used when scaling black and white 
     * images.
     *
     * @param pValue <code>true</code> is sub-sampling should be used, 
     *        otherwise <code>false</code>.
     */
    public void setSubSamplingUsed(boolean pValue) {
        mUseSubSampling = pValue;
    }

    /**
     * Rotate the image through the angle provided.
     *
     * @param pAngle the angle to rotate the image by in radians.
     */
    public void rotate(double pAngle) {
        if (pAngle != 0.0) {
            double rotation = mRotation + pAngle;
            rotation = Math.IEEEremainder(rotation, 2 * Math.PI);
            setRotation(rotation);
        }
    }

    /**
     * Zooms into the image by a given factor.
     *
     * @param pLevel the zoom factor to use.
     */
    public void zoomIn(double pLevel) {
        setZoom(mCurrZoomLevel * pLevel);
    }

    /**
     * Zooms out of the image by a given factor.
     *
     * @param pLevel the zoom factor to use.
     */
    public void zoomOut(double pLevel) {
        setZoom(mCurrZoomLevel / pLevel);
    }

    /**
     * Resets the zoom level to 1.0, i.e. full scale image. Also resets the 
     * scaling type back to normal.
     */
    public void fullScale() {
        setScalingType(ScalingType.NORMAL);
        setZoom(1.0);
    }

    /**
     * Fit to the width of the component.
     */
    public void fitWidth() {
        setScalingType(ScalingType.FIT_WIDTH);
    }

    /**
     * Fit to the height of the component.
     */
    public void fitHeight() {
        setScalingType(ScalingType.FIT_HEIGHT);
    }

    /**
     * Fit to the width and height of the component.
     */
    public void fitWindow() {
        setScalingType(ScalingType.FIT_ALL);
    }
    
    /**
     * Reset the scaling, zoom level, and rotation to the original values.
     */
    public void reset() {
        mScalingType = ScalingType.NORMAL;
        mCurrZoomLevel = 1.0;
        mRotation = 0.0;
        render();
    }

    /**
     * Records a new size. Called by the AWT.
     *
     * @param pX the x boundary
     * @param pY the y boundary
     * @param pWidth the width
     * @param pHeight the height
     * 
     */
    public void setBounds(int pX, int pY, int pWidth, int pHeight) {
        super.setBounds(pX, pY, pWidth, pHeight);
        updateZoomLevel();
    }


    // ------------------------------------------------------ Protected Methods
    // none
    
    // -------------------------------------------------------- Private Methods
    
    /**
     * Creates a new PlanarImage by applying rotation and scaling operations to
     * the source image and then setting the new image in the canvas.
     */
    private void render() {
        if (mSourceImages == null) {
            setImage(null);
            return;
        }

        PlanarImage currImage = mSourceImages[mImageIndex];
        ParameterBlock pb;

        // Apply any rotation
        if (mRotation != 0.0) {
            pb = new ParameterBlock();
            pb.addSource(currImage);
            pb.add(null).add(null).add(new Float(mRotation)).add(null);
            currImage = JAI.create("rotate", pb);
        }

        // Scale the image based on the zoom level. If the image
        // is black and white, this method uses the subsample to
        // gray operation. This operation particularly improves the
        // readability of text at zoom levels < 1.0.
        //
        // @todo Scaling should be done in increments of no less
        // than 0.5, otherwise the results can be poor.
        if (mCurrZoomLevel > 0.0) {
            if (mUseSubSampling &&
                currImage.getSampleModel() instanceof MultiPixelPackedSampleModel &&
                currImage.getColorModel().getPixelSize() == 1 &&
                mCurrZoomLevel <= 1.0) {
                
                // Try to convert to gray.
                pb = new ParameterBlock();
                pb.addSource(currImage);
                pb.add(new Float(mCurrZoomLevel));
                pb.add(new Float(mCurrZoomLevel));
                currImage = JAI.create("SubsampleBinaryToGray", pb);
            }
            else {
                // Straight scaling of the image
                pb = new ParameterBlock();
                pb.addSource(currImage);
                pb.add(new Float(mCurrZoomLevel));
                pb.add(new Float(mCurrZoomLevel));
                pb.add(null).add(null).add(null);
                currImage = JAI.create("scale", pb);
            }
        }

        // Rotation tends to shift the rendered image around, so
        // make sure the image is positioned correctly in the panel.
        // We just return the origin of the image back to 0,0.
        pb = new ParameterBlock();
        pb.addSource(currImage);
        pb.add(new Float(-currImage.getMinX()));
        pb.add(new Float(-currImage.getMinY()));
        pb.add(null);
        currImage = (PlanarImage)JAI.create("translate", pb);

        // Update the rendered image
        mCanvas.setImage(currImage);
    }

    /**
     * Changes the current scaling regime and updates the zoom level if 
     * necessary.
     *
     * @param pType the new scaling regime.
     */
    private void setScalingType(ScalingType pType) {
        // If the scaling type isn't actually changing, there's nothing
        // to do.
        if (mScalingType == pType) {
            return;
        }

        // Update the scaling type and the zoom level
        mScalingType = pType;
        updateZoomLevel();
    }

    /**
     * Updates the current zoom level based on the active scaling regime. This 
     * takes into account the size of the displayed image and the size of the 
     * visible area of the viewer to set the requisite zoom level. If the 
     * scaling regime is ScalingType.NORMAL, this method does nothing.
     */
    private void updateZoomLevel() {
        if (mSourceImages == null) {
            return;
        }

        final PlanarImage currImage = mSourceImages[mImageIndex];
        final Insets insets = getInsets();
        final int width = getWidth() - insets.left - insets.right;
        final int height = getHeight() - insets.top - insets.bottom;
        
        //EIGlobals.getInstance().log("w="+width);
        //EIGlobals.getInstance().log("h="+height);

        if (mScalingType == ScalingType.FIT_WIDTH) {
            setZoom(width / (double)currImage.getWidth());
        } else if (mScalingType == ScalingType.FIT_HEIGHT) {
            setZoom(height / (double)currImage.getHeight());
        } else if (mScalingType == ScalingType.FIT_ALL) {
            double zoom = width / (double)currImage.getWidth();
            zoom = Math.min(height / (double)currImage.getHeight(), zoom);
            setZoom(zoom);
        }
    }

    // ------------------------------------------------------------ Inner Class
    
    /**
     * <p>Enumeration type for the scaling types used by MXImagePanel. These
     * are:</p>
     * <table border="none">
     *   <tr><td>Normal</td><td>Zoom level on image is manually set</td></tr>
     *   <tr><td>Fit width</td><td>Image is automatically resized to fit into 
     *           the width of the viewer</td></tr>
     *   <tr><td>Fit height</td><td>Image is automatically resized to fit into
     *           the height of the viewer</td></tr>
     *   <tr><td>Fit all</td><td>Image is automatically resized to fit into the
     *           viewer.</td></tr>
     * </table>
     * <p>Note that all these types maintain the image's aspect ratio.<p>
     */
    private static class ScalingType {
        public static final ScalingType NORMAL     = new ScalingType();
        public static final ScalingType FIT_WIDTH  = new ScalingType();
        public static final ScalingType FIT_HEIGHT = new ScalingType();
        public static final ScalingType FIT_ALL    = new ScalingType();

        /**
         * Instances of this class cannot be created by clients. They
         * can only be created within the class itself.
         */
        private ScalingType() {
        }
    }
}
