package app;

import entity.base.Ant.AntType;
import gui.ControlBar;
import gui.SimulationArea;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

	private static boolean isActive = false;

	private static ControlBar controlBar = new ControlBar();
	private static SimulationArea simulationArea = new SimulationArea();

	@Override
	public void start(Stage primaryStage) throws Exception {

		VBox root = new VBox();
		root.getChildren().addAll(controlBar, simulationArea);

		Scene scene = new Scene(root);

		primaryStage.setTitle("Ant Simulator!");
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void startSimulation() {
		isActive = true;
		controlBar.getStartRestartButton().setText("Stop");
		final int startingAntCount = 3;
		for (int count = 0; count < startingAntCount; ++count)
			simulationArea.addAnt(AntType.FIRE);
		controlBar.rerender();
	}

	public static void stopSimulation() {
		isActive = false;
		controlBar.getStartRestartButton().setText("Start");
		controlBar.setPopulation(0);
		controlBar.setFireAntsCount(0);
		controlBar.setFlashAntsCount(0);
		controlBar.setBroughtHomeCandyCount(0);
		controlBar.setFoodCost(controlBar.getBaseFoodCost());
		controlBar.setMoney(controlBar.getBasemoney());
		controlBar.rerender();
		simulationArea.reset();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static boolean isActive() {
		return isActive;
	}

	public static ControlBar getControlBar() {
		return controlBar;
	}

	public static void setControlBar(ControlBar controlBar) {
		Main.controlBar = controlBar;
	}

	public static SimulationArea getSimulationArea() {
		return simulationArea;
	}

	public static void setSimulationArea(SimulationArea simulationArea) {
		Main.simulationArea = simulationArea;
	}

	public static void setActive(boolean isActive) {
		Main.isActive = isActive;
	}

}
