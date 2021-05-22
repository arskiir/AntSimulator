package entity.derived;

import app.Main;
import entity.base.Ant;
import entity.base.Food;
import entity.base.Poisonable;
import gui.ControlBar;
import gui.SimulationArea;
import javafx.application.Platform;
import utils.Vector;

public final class PoisonFood extends Food implements Poisonable {

	public PoisonFood(String imgPath, Vector position) {
		super(imgPath, position);
	}

	@Override
	public void poison(Ant ant) {
		// kill the ant
		ant.playOutroSound();
		ant.getFindFoodThread().interrupt();
		// remove this food from the area
		final SimulationArea simulationArea = Main.getSimulationArea();
		Platform.runLater(() -> simulationArea.getChildren().remove(this.img));
		simulationArea.getFoods().remove(this);
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
