package entity.derived;

import app.Main;
import entity.base.Ant;
import entity.base.Candy;
import entity.base.Poisonable;
import gui.ControlBar;
import gui.SimulationArea;
import javafx.application.Platform;
import utils.Vector;

public final class PoisonousCandy extends Candy implements Poisonable {

	public PoisonousCandy(String imgPath, Vector position) {
		super(imgPath, position);
	}

	@Override
	public void poison(Ant ant) {
		// kill the ant
		this.removeAnt(ant);
		this.disappear();
		this.updateControlBar(ant);
	}
	
	private void removeAnt(Ant ant) {
		ant.playOutroSound();
		ant.getFindFoodThread().interrupt();
		Main.getSimulationArea().getAnts().remove(ant);
	}

	private void disappear() {
		// remove this food from the area
		final SimulationArea simulationArea = Main.getSimulationArea();
		Platform.runLater(() -> simulationArea.getChildren().remove(this.img));
		simulationArea.getFoods().remove(this);
	}

	private void updateControlBar(Ant ant) {
		final ControlBar controlBar = Main.getControlBar();
		controlBar.setPopulation(controlBar.getPopulation() - 1);
		controlBar.setDeadCount(controlBar.getDeadCount() + 1);
		if (ant instanceof FlashAnt)
			controlBar.setFlashAntsCount(controlBar.getFlashAntsCount() - 1);
		else
			controlBar.setFireAntsCount(controlBar.getFireAntsCount() - 1);
		controlBar.rerender();
	}

}
