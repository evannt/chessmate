package util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {

	public static final int TILE_SIZE = 72;
	public static final int SCALED_TILE_SIZE = TILE_SIZE * 1;

	public static BufferedImage getImage(String filename) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(filename));
			image = scaleImage(image, SCALED_TILE_SIZE, SCALED_TILE_SIZE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	public static BufferedImage getImage(String filename, int scaledWidth, int scaledHeight) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(filename));
			image = scaleImage(image, scaledWidth, scaledHeight);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	private static BufferedImage scaleImage(BufferedImage image, int width, int height) {
		BufferedImage scaledImage = new BufferedImage(width, height,
				image.getType() == 0 ? BufferedImage.TYPE_INT_RGB : image.getType());

		Graphics2D graphics2D = scaledImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		graphics2D.drawImage(image, 0, 0, width, height, null);
		graphics2D.dispose();

		return scaledImage;
	}

}
