package wade;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class which encapsulates a {@link BufferedImage} while providing publicly
 * accessible {@link Graphics} and {@link Graphics2D} objects, and the
 * functionality to write it's contents to a JPEG or PNG file.
 * 
 * 
 * @author Wade Harkins (vdtdev@gmail.com)
 */
public class SavableGraphics {

	/**
	 * Constant representing the JPEG Image format<br>
	 * JPEG images are smaller than other file formats, but have lossy
	 * compression.
	 */
	public final String SGF_TYPE_JPEG = "jpeg";
	/**
	 * Constant representing the PNG (Portable Network Graphic) Image format<br>
	 * PNG images are moderate in size, and use lossless compression.
	 */
	public final String SGF_TYPE_PNG = "png";

	private BufferedImage baseImage = null; // buffered image
	private Graphics2D g; // reference to graphics content

	/**
	 * Public constructor for SavableGraphics
	 * 
	 * @param width
	 *            Width of the image as <code>int</code>
	 * @param height
	 *            Height of the image as <code>int</code>
	 */
	public SavableGraphics(int width, int height) {
		baseImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = baseImage.createGraphics();
	}

	/**
	 * Returns a {@link Graphics} object that can be used to write directly into
	 * the class's internal {@link BufferedImage}
	 * 
	 * @return <code>Graphics</code> object that draws directly into the class's
	 *         <code>BufferedImage</code>
	 */
	public Graphics getGraphics() {
		return (Graphics) g;
	}

	/**
	 * Returns a {@link Graphics2D} object that can be used to write directly
	 * into the class's internal {@link BufferedImage}
	 * 
	 * @return <code>Graphics2D</code> object that draws directly into the
	 *         class's <code>BufferedImage</code>.
	 */
	public Graphics2D getGraphics2D() {
		return g;
	}

	/**
	 * Writes the content of the class's internal BufferedImage to the specified
	 * filename in jpeg format
	 * 
	 * @param filename
	 *            File to write the image to
	 * @param type
	 *            The image format to use for the file. Must be either
	 *            {@link SGF_TYPE_JPEG} or {@link SGF_TYPE_PNG}.
	 * 
	 * @throws IOException
	 */
	public boolean WriteImage(String filename, String type) throws IOException {

		RenderedBufferedImage i = new RenderedBufferedImage(this.baseImage);
		boolean result = false;
		try {
			result = ImageIO.write(i, type, new File(filename));
		} catch (IOException ex) {
			throw new IOException(ex.getMessage());
		}
		return result;

	}

}
