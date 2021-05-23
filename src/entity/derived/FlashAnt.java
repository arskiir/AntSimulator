package entity.derived;

import entity.base.Ant;
import gui.ControlBar;
import gui.SimulationArea;
import utils.Sound;
import utils.Vector;

/**
 * The Class FlashAnt. A special type of ant that moves faster but gives less
 * money.
 */
public final class FlashAnt extends Ant {

	/**
	 * Instantiates a new flash ant.
	 */
	public FlashAnt() {
		super(5, 15, 40, 1.2, "flash-ant.png", "oof_loud.mp3", .05);
		this.introPlayer = Sound.getMediaPlayer("dejavu.wav", .5);
	}

	/**
	 * Plays introduction sound.
	 */
	@Override
	public void playIntroSound() {
		introPlayer.play();
	}

	/**
	 * Plays dying sound.
	 */
	@Override
	public void playOutroSound() {
		outroPlayer.play();
	}

	/**
	 * Does nothing since flash ants move straight.
	 */
	@Override
	protected void randomizeDirection() {
		// flash ant moves straight
	}

	/**
	 * Does nothing since flash ants cannot reproduce.
	 *
	 * @param simulationArea the simulation area
	 * @param controlBar     the control bar
	 */
	@Override
	protected void reproduce(SimulationArea simulationArea, ControlBar controlBar) {
		// flash ant cannot reproduce
	}

}
