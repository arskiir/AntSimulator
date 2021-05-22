package app;

import entity.base.Ant.AntType;
import gui.ControlBar;
import gui.SimulationArea;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main JavaFX Application class for Ant Simulator
 */
public class Main extends Application {

	/** indicates if the simulation is running or not. */
	private static boolean isActive = false;

	/** Instance of ControlBar */
	private static ControlBar controlBar = new ControlBar();

	/** Instance of SimulationArea */
	private static SimulationArea simulationArea = new SimulationArea();

	/**
	 * Puts each user interface component on the stage and shows. This also setups a
	 * handler to interrupt all ants' threads on window close request.
	 * 
	 * @param primaryStage the primary stage
	 * @throws Exception the exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		var root = new VBox();
		root.getChildren().addAll(controlBar, simulationArea);

		var scene = new Scene(root);

		primaryStage.setTitle("Ant Simulator");
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("candy1.png")));
		primaryStage.show();
		primaryStage.setOnCloseRequest(
				e -> simulationArea.getAnts().forEach(ant -> ant.getSettingOffJourneyThread().interrupt()));
	}

	/**
	 * starts the simulation
	 */
	public static void startSimulation() {
		isActive = true;
		controlBar.getStartStopButton().setText("Stop");
		final var startingAntCount = 5;
		for (var count = 0; count < startingAntCount; ++count)
			simulationArea.addAnt(AntType.FIRE);
		controlBar.rerender();
		controlBar.playIntroSound();
	}

	/**
	 * stops the simulation
	 */
	public static void stopSimulation() {
		isActive = false;
		controlBar.getStartStopButton().setText("Start");
		controlBar.setPopulation(0);
		controlBar.setFireAntsCount(0);
		controlBar.setFlashAntsCount(0);
		controlBar.setBroughtHomeCandyCount(0);
		controlBar.setDeadCount(0);
		controlBar.setHasReachedMaxMoney(false);
		controlBar.setFoodCost(controlBar.getBaseCandyCost());
		controlBar.setMoney(controlBar.getBasemoney());
		controlBar.rerender();
		simulationArea.reset();
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Checks if the simulation is running or not.
	 * 
	 * @return true, if is isActive == true, else false
	 */
	public static boolean isActive() {
		return isActive;
	}

	/**
	 * Gets the control bar.
	 *
	 * @return the control bar
	 */
	public static ControlBar getControlBar() {
		return controlBar;
	}

	/**
	 * Gets the simulation area.
	 *
	 * @return the simulation area
	 */
	public static SimulationArea getSimulationArea() {
		return simulationArea;
	}

}
