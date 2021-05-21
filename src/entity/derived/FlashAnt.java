package entity.derived;

import entity.base.Ant;
import gui.SimulationArea;
import javafx.scene.image.ImageView;
import utils.math.Vector;

public final class FlashAnt extends Ant {

	public FlashAnt(Vector home) {
		super(home);
		this.speed *= 5;
		this.velocity = Vector.createVector2FromAngle(this.velocity.getAngle(), this.speed);
		this.visionSpan = 15; // see less than fire ant
		this.visionDepth = 40;
		this.img = new ImageView("flash-ant.png");
		this.img.setFitHeight(this.antHeight);
		this.img.setPreserveRatio(true);
	}

	@Override
	protected void randomizeDirection() {
		// flash ant moves straight
	}
	
	@Override
	protected void reproduce(SimulationArea simulationArea) {
		// flash ant cannot reproduce
	}

}
