package Graphics;

import java.awt.Image;
import java.io.IOException;
import java.lang.ref.*;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageCache extends AbstractCache<Image> {
	
	public ImageCache(){
		super();
	}

	@Override
	protected Image loadResource(String resourceName) throws IOException {
		System.out.println("Loading visual resource: " + resourceName);
		return ImageIO.read(this.getClass().getResource(resourceName));
	}
	
}
