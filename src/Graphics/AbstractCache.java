package Graphics;

import java.awt.Image;
import java.io.IOException;
import java.lang.ref.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public abstract class AbstractCache<T> {

	
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
	private List<Bundle<T>> hardCache ;
	private Map<String, WeakReference<T>> softCache;

	
	public AbstractCache(){
		hardCache = new ArrayList<>(finalIndex + 1);
		// Fill the cache with nulls.
		for (int i = 0; i <= finalIndex; ++i){
			hardCache.add(null);
		}
		softCache = new HashMap<>();
	}
	
	public T getResource(String resourceName) throws IOException{
		for (int i = 0; i < hardCache.size(); ++i){
			Bundle<T> current = hardCache.get(i);
			if (current !=  null && current.resourceName.equals(resourceName)){
				// Bubble i to front of queue.
				bubbleIndex(i);
				return current.content;
			}
		}
		// Image not in hard cache, so check soft cache.
		WeakReference<T> weakResource = softCache.get(resourceName);
		if (weakResource != null){
			T resource = weakResource.get();
			
			if (resource != null){
				return resource;
			}
		}
		// Everything has fallen through, must load from disk.
		T newResource = loadResource(resourceName);
		addResourceToHardCache(resourceName, newResource);
		return newResource;
		
	}
	
	// Move the item at the given location of the hardCache to the front of the hardCache.
	private void bubbleIndex(int index){
		while(index > 0){
			swap(index, index - 1);
			--index;
		}
	}
	
	private void swap(int a, int b){
		Bundle<T> save = hardCache.get(a);
		hardCache.set(a, hardCache.get(b));
		hardCache.set(b, save);
	}
	
	/*
	 * Add the Image to the hardCache. If hardCache is full, add the displaced Image to the softCache.
	 */
	private void addResourceToHardCache(String resourceName, T image){
		Bundle<T> bundle = new Bundle<T>(resourceName, image);
		// Add displaced image to softCache.
		if (hardCache.get(finalIndex) != null){
			addToSoftCache(hardCache.get(finalIndex).resourceName, hardCache.get(finalIndex).content);
		}
		// Insert new Image.
		hardCache.set(finalIndex, bundle);
		// Refresh the new Image's priority.
		bubbleIndex(finalIndex);
	}
	
	private void addToSoftCache(String resourceName, T image){
		softCache.put(resourceName, new WeakReference<T>(image));
	}
	
	protected abstract T loadResource(String resourceName) throws IOException;
	
	private class Bundle<K>{
		final String resourceName;
		final K content;
		
		public Bundle(String inResourceName, K inResource){
			resourceName = inResourceName;
			content = inResource;
		}
	}
	
}