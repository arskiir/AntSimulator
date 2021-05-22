package entity.derived;

import app.Main;
import entity.base.Ant;
import entity.base.Candy;
import entity.base.Poisonable;
import javafx.application.Platform;
import utils.Vector;

/**
 * The Class PoisonousCandy. A deadly type of candy that kills the ant that
 * grabs it.
 */
public final class PoisonousCandy extends Candy implements Poisonable {

	/**
	 * Instantiates a new poisonous candy.
	 *
	 * @param imgPath  the path to the actual image
	 * @param position the position vector
	 */
	public PoisonousCandy(String imgPath, Vector position) {
		super(imgPath, position);
	}

	/**
	 * Kills the ant completely, consisting of different steps including removing
	 * the ant, removing itself, and update the control bar.
	 *
	 * @param ant the ant
	 */
	@Override
	public void poison(Ant ant) {
		// kill the ant
		this.removeAnt(ant);
		this.disappear();
		this.updateControlBar(ant);
	}

	/**
	 * Removes the ant.
	 *
	 * @param ant the ant
	 */
	private void removeAnt(Ant ant) {
		ant.playOutroSound();
		ant.getSettingOffJourneyThread().interrupt();
		Main.getSimulationArea().getAnts().remove(ant);
	}

	/**
	 * Removes itself
	 */
	private void disappear() {
		// remove this food from the area
		final var simulationArea = Main.getSimulationArea();
		Platform.runLater(() -> simulationArea.getChildren().remove(this.img));
		simulationArea.getFoods().remove(this);
	}

	/**
	 * Update control bar.
	 *
	 * @param ant the poor ant
	 */
	private void updateControlBar(Ant ant) {
		final var controlBar = Main.getControlBar();
		controlBar.setPopulation(controlBar.getPopulation() - 1);
		controlBar.setDeadCount(controlBar.getDeadCount() + 1);
		if (ant instanceof FlashAnt)
			controlBar.setFlashAntsCount(controlBar.getFlashAntsCount() - 1);
		else
			controlBar.setFireAntsCount(controlBar.getFireAntsCount() - 1);
		controlBar.rerender();
	}

}
