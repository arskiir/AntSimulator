package utils;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
	private Sound() {
	}

	/**
	 * get MediaPlayer instance of a media file and volume within [0.0 1.0] where
	 * 0.0 is inaudible and 1.0 is full volume, which is the default
	 */
	public static MediaPlayer getMediaPlayer(String src, double volume) {
		Media sound = new Media(new File(src).toURI().toString());
		MediaPlayer player = new MediaPlayer(sound);
		player.setVolume(volume);
		return player;
	}
	
	/** can play sound repeatedly */
	public static void play(MediaPlayer player) {
		player.seek(player.getStartTime());
		player.play();
	}
}
