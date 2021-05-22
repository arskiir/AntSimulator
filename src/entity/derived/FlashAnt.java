package entity.derived;

import entity.base.Ant;
import gui.ControlBar;
import gui.SimulationArea;
import javafx.scene.image.ImageView;
import utils.Sound;
import utils.Vector;

public final class FlashAnt extends Ant {

	public FlashAnt(Vector home) {
		super(home);
		this.speed *= 5;
		this.velocity = Vector.createVector2FromAngle(this.velocity.getAngle(), this.speed);
		this.visionSpan = 15; // see less than fire ant
		this.visionDepth = 40;
		this.moneyMultiplier = 1.1;
		this.img = new ImageView("flash-ant.png");
		this.img.setFitHeight(this.antHeight);
		this.img.setPreserveRatio(true);
		
		this.introPlayer = Sound.getMediaPlayer("res/dejavu.wav", .5);
		this.outroPlayer = Sound.getMediaPlayer("res/oof_loud.mp3", .05);
	}
	
	@Override
	public void playIntroSound() {
		introPlayer.play();
	}
	
	@Override
	public void playOutroSound() {
		// more dramatic death
		outroPlayer.play();
	}

	@Override
	protected void randomizeDirection() {
		// flash ant moves straight
	}
	
	@Override
	protected void reproduce(SimulationArea simulationArea, ControlBar controlBar) {
		// flash ant cannot reproduce
	}

}
