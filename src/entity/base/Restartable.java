package entity.base;

/**
 * The Interface Restartable. Shows by playing a sound that an instance of a
 * class can exist and be removed or can start and end
 */
public interface Restartable {
	/**
	 * Plays introduction sound.
	 */
	public void playIntroSound();

	/**
	 * Plays disappearing sound.
	 */
	public void playOutroSound();
}
