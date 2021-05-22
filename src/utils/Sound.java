package utils;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * The Class Sound. Provides useful methods when working with sounds
 */
public class Sound {
	
	/**
	 * Instantiates a new sound.
	 */
	private Sound() {
	}

	/**
	 * Gets MediaPlayer instance of a media file and volume within [0.0 1.0] where
	 * 0.0 is inaudible and 1.0 is full volume, which is the default
	 *
	 * @param src the path to the file
	 * @param volume the volume
	 * @return the media player
	 */
	public static MediaPlayer getMediaPlayer(String src, double volume) {
		var sound = new Media(new File(src).toURI().toString());
		var player = new MediaPlayer(sound);
		player.setVolume(volume);
		return player;
	}
	
	/**
	 * Plays the player from the start
	 *
	 * @param player the player
	 */
	public static void play(MediaPlayer player) {
		player.seek(player.getStartTime());
		player.play();
	}
}
