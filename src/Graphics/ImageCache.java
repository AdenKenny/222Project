package Graphics;

import java.awt.Image;
import java.io.IOException;
import java.lang.ref.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageCache extends AbstractCache<Image> {

	public ImageCache(){
		super();
	}

	@Override
	protected Image loadResource(String resourceName) throws IOException {
		URL url = this.getClass().getResource(resourceName);
		if (url == null){
			throw new IOException("File not found.");
		}
		return ImageIO.read(url);
	}

}
