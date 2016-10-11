package Graphics;

import java.io.IOException;

import javax.sound.sampled.Clip;

public class AudioHandler implements GameEventListener {

	private AudioCache cache;
	
	private Clip musicClip;
	
	public AudioHandler(){
		cache = new AudioCache();
	}

	@Override
	public void event(String eventName) {
		playSound(eventName);
	}
	
	/**
	 * Play an audio file with the given name until
	 */
	public void playMusic(String inMusicName){
		//Stop currently running music.
		if (musicClip != null){
			musicClip.stop();
		}
		try {
			musicClip = cache.getResource(calculateSoundPath(inMusicName));
			// Loop the entire clip.
			musicClip.setLoopPoints(0, -1);
			// Start the looping!
			musicClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (IOException e) {
			//If the load fails, keep playing the pre-existing music, if there was any.
			if (musicClip != null){
				musicClip.start();
			}
		}
	}
	
	public void playSound(String soundName){
		// Load the resource and play it on a new Thread.
		new Thread(new Runnable(){

			@Override
			public void run() {
				Clip clip;
				try {
					clip = cache.getResource(calculateSoundPath(soundName));
					//Stop the clip if it is currently running.
					clip.stop();
					//Reset the clip to the beginning.
					clip.setFramePosition(0);
					// Play the audio.
					clip.start();
				} catch (IOException e) {
				}
			}
			
		}).run();
	}
	
	public String calculateSoundPath(String soundName){
		return String.format("/resources/audio/%s.aiff", soundName);
	}
	
}
