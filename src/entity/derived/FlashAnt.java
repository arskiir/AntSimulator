package entity.derived;

import entity.base.Ant;
import gui.ControlBar;
import gui.SimulationArea;
import javafx.scene.image.ImageView;
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
		super();
		this.speed *= 5;
		this.velocity = Vector.createVector2FromAngle(this.velocity.getAngle(), this.speed);
		this.visionSpan = 15; // see less than fire ant
		this.visionDepth = 40;
		this.moneyMultiplier = 1.2;
		this.img = new ImageView(ClassLoader.getSystemResource("flash-ant.png").toExternalForm());
		this.img.setFitHeight(this.antHeight);
		this.img.setPreserveRatio(true);

		this.introPlayer = Sound.getMediaPlayer("dejavu.wav", .5);
		this.outroPlayer = Sound.getMediaPlayer("oof_loud.mp3", .05);
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
