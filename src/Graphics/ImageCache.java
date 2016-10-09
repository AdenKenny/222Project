package Graphics;

import java.awt.Image;
import java.io.IOException;
import java.lang.ref.*;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageCache {

	
	// The capacity - 1, because index 0.
	private static final int finalIndex = 19;
	
	/* 
	 * Guaranteed live references, stored in order of priority.
	 * When a new Image is loaded from disk, the lowest priority
	 * Image is moved to the softCache, and will be garbage collected next cycle.
	 * When an Image is referenced, it is given top priority,
	 * as it is likely to be used again, and should remain available to minimise delay
	 * from disk access.
	 */
	private Bundle[] hardCache;
	private Map<String, WeakReference<Image>> softCache;

	
	public ImageCache(){
		hardCache = new Bundle[finalIndex + 1];
		softCache = new HashMap<>();
	}
	
	public Image getImage(String resourceName) throws IOException{
		for (int i = 0; i < hardCache.length; ++i){
			Bundle current = hardCache[i];
			if (current !=  null && current.resourceName.equals(resourceName)){
				// Bubble i to front of queue.
				bubbleIndex(i);
				return current.image;
			}
		}
		// Image not in hard cache, so check soft cache.
		WeakReference<Image> refBundle = softCache.get(resourceName);
		if (refBundle != null){
			Image weakImage = refBundle.get();
			
			if (weakImage != null){
				return weakImage;
			}
		}
		// Everything has fallen through, must load from disk.
		Image newImage;
		newImage = ImageIO.read(this.getClass().getResource(resourceName));
		addImageToHardCache(resourceName, newImage);
		return newImage;
		
	}
	
	// Move the item at the given location of the hardCache to the front of the hardCache.
	private void bubbleIndex(int index){
		while(index > 0){
			swap(index, index - 1);
			--index;
		}
	}
	
	private void swap(int a, int b){
		Bundle save = hardCache[a];
		hardCache[a] = hardCache[b];
		hardCache[b] = save;
	}
	
	/*
	 * Add the Image to the hardCache. If hardCache is full, add the displaced Image to the softCache.
	 */
	private void addImageToHardCache(String resourceName, Image image){
		Bundle bundle = new Bundle(resourceName, image);
		// Add displaced image to softCache.
		if (hardCache[finalIndex] != null){
			addToSoftCache(hardCache[finalIndex].resourceName, hardCache[finalIndex].image);
		}
		// Insert new Image.
		hardCache[finalIndex] = bundle;
		// Refresh the new Image's priority.
		bubbleIndex(finalIndex);
	}
	
	private void addToSoftCache(String resourceName, Image image){
		softCache.put(resourceName, new WeakReference<Image>(image));
	}
	
	private class Bundle{
		final String resourceName;
		final Image image;
		
		public Bundle(String inResourceName, Image inImage){
			resourceName = inResourceName;
			image = inImage;
			
		}
	}
	
}
