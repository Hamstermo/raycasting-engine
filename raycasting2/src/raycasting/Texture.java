package raycasting;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;

public class Texture {
	
	private BufferedImage image;

	public Texture(String loc) {
		try {
			image = ImageIO.read(new File(loc));
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public int[] getPixel(int x, int y) {
		int pixel = image.getRGB(x, y);
		
	    int red = (pixel >> 16) & 0xff;
	    int green = (pixel >> 8) & 0xff;
	    int blue = (pixel) & 0xff;
	    
	    int[] rgb = {red, green, blue};
	    
	    return rgb;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
}
