package wade;

import java.awt.Rectangle;
import java.awt.image.*;
import java.util.Vector;

/**
 * Class implementing the {@link RenderedImage} interface in a way that allows
 * <code>RenderedImage</code> objects to be created from
 * <code>BufferedImage</code> objects
 * 
 * @author Wade Harkins (vdtdev@gmail.com)
 * 
 */
public class RenderedBufferedImage implements RenderedImage {

	private BufferedImage baseImage;

	/**
	 * Public constructor creating a new <code>RenderedBufferedImage</code> from an
	 * existing {@link BufferedImage}
	 * 
	 * @param img
	 */
	public RenderedBufferedImage(BufferedImage img) {
		this.baseImage = img;
	}

	

	@Override
	public WritableRaster copyData(WritableRaster raster) {
		// TODO Auto-generated method stub
		return baseImage.copyData(raster);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.image.RenderedImage#getColorModel()
	 */
	@Override
	public ColorModel getColorModel() {
		return baseImage.getColorModel();
	}

	@Override
	public Raster getData() {
		// TODO Auto-generated method stub
		return baseImage.getData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.image.RenderedImage#getData(java.awt.Rectangle)
	 */
	@Override
	public Raster getData(Rectangle rect) {
		return baseImage.getData(rect);
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return baseImage.getHeight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.image.RenderedImage#getMinTileX()
	 */
	@Override
	public int getMinTileX() {
		return baseImage.getMinTileX();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.image.RenderedImage#getMinTileY()
	 */
	@Override
	public int getMinTileY() {
		return baseImage.getMinTileY();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.image.RenderedImage#getMinX()
	 */
	@Override
	public int getMinX() {
		return baseImage.getMinX();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.image.RenderedImage#getMinY()
	 */
	@Override
	public int getMinY() {
		return baseImage.getMinTileY();
	}

	@Override
	public int getNumXTiles() {
		return baseImage.getNumXTiles();
	}

	@Override
	public int getNumYTiles() {
		return baseImage.getNumYTiles();
	}

	@Override
	public Object getProperty(String name) {
		return baseImage.getProperty(name);
	}

	@Override
	public String[] getPropertyNames() {
		return baseImage.getPropertyNames();
	}

	@Override
	public SampleModel getSampleModel() {
		return baseImage.getSampleModel();
	}

	@Override
	public Vector<RenderedImage> getSources() {
		return baseImage.getSources();
	}

	@Override
	public Raster getTile(int tileX, int tileY) {
		return baseImage.getTile(tileX, tileY);
	}

	@Override
	public int getTileGridXOffset() {
		return baseImage.getTileGridXOffset();
	}

	@Override
	public int getTileGridYOffset() {
		return baseImage.getTileGridYOffset();
	}

	@Override
	public int getTileHeight() {
		return baseImage.getTileHeight();
	}

	@Override
	public int getTileWidth() {
		return baseImage.getTileWidth();
	}

	@Override
	public int getWidth() {
		return baseImage.getWidth();
	}

}
