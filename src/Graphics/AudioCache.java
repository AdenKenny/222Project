package Graphics;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioCache extends AbstractCache<Clip> {

	@Override
	protected Clip loadResource(String resourceName) throws IOException {
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(this.getClass().getResource(resourceName));
			Clip clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, audioStream.getFormat()));
			clip.open(audioStream);
			return clip;
		//If an exception is encountered, translate it to an expected exception.
		} catch (UnsupportedAudioFileException e) {
			throw new IOException(e);
		} catch (LineUnavailableException e) {
			throw new IOException(e);
		}
	}

	
}
