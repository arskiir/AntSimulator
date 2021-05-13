package gui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class SimulationArea extends Pane {

	private ImageView houseImage = new ImageView("house.png");
	private static final int houseImageWidth = 150;
	private static final int width = 1500;
	private static final int height = 800;

	public SimulationArea() {
		super();

		this.setMinHeight(height);
		this.setPrefWidth(width);
		this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

		this.getChildren().add(houseImage);
		this.houseImage.setFitHeight(houseImageWidth);
		this.houseImage.setFitWidth(houseImageWidth);
		this.houseImage.relocate((double) width / 2 - (double) houseImageWidth / 2,
				(double) height / 2 - (double) houseImageWidth / 2);
	}

}
