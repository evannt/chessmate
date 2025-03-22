package ui;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {

	private static final String SOUND_PATH = "resc/sounds/";
	private static final String[] SOUND_FILES = { "move-self.wav", "capture.wav", "move-check.wav", "castle.wav", "promote.wav", "notify.wav" };

	private Map<String, Clip> sounds;

	public SoundManager() {
		sounds = new HashMap<>();
		loadSounds();
	}

	public void playSound(String sound) {
		Clip clip = sounds.get(sound);
		if (clip == null) {
			return;
		}
		if (clip.isRunning()) {
			clip.stop();
		}
		clip.setFramePosition(0);
		clip.start();
	}

	public void loadSounds() {
		for (String soundFile : SOUND_FILES) {
			File file = new File(SOUND_PATH + soundFile);
			try {
				AudioInputStream audioInputStream;
				audioInputStream = AudioSystem.getAudioInputStream(file);
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				sounds.put(soundFile.substring(0, soundFile.length() - 4), clip);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}

}
