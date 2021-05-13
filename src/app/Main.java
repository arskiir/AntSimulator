package app;

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

	public static void startSimulation(final int numberOfAnts) {
		isActive = true;
		controlBar.getStartRestartButton().setText("Stop");
		controlBar.getNumberOfAntsField().setDisable(true);

	}

	public static void stopSimultaion() {
		isActive = false;
		controlBar.getStartRestartButton().setText("Start");
		controlBar.getNumberOfAntsField().setDisable(false);
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static boolean isActive() {
		return isActive;
	}

}
