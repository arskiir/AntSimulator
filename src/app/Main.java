package app;

import gui.ControlBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	
	private boolean isActive = false;
	
	private ControlBar controlBar;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.controlBar = new ControlBar();
		
		VBox root = new VBox();
		root.getChildren().addAll(this.controlBar);
		
		Scene scene = new Scene(root, 1500, 900);
		
		primaryStage.setTitle("Ant Simulator!");
		primaryStage.setResizable(false);
		primaryStage.centerOnScreen();
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
	}
	
}
