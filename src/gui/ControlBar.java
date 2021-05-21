package gui;

import app.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ControlBar extends HBox {

	private Text populationText;
	private Text fireAntsCountText;
	private Text flashAntsCountText;

	private int population;
	private int fireAntsCount;
	private int flashAntsCount;

	private Button startRestartButton;
	private String buttonBaseStyle = "-fx-border-radius: 8px; -fx-background-color: white; -fx-border-style: solid; "
			+ "-fx-border-width: 3px; -fx-border-color: chocolate; -fx-background-radius: 8px;";

	private String font = "Consolas";

	public ControlBar() {
		super();

		this.population = 0;
		this.fireAntsCount = 0;
		this.flashAntsCount = 0;

		this.setPrefWidth(Global.WIDTH);
		this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		this.setAlignment(Pos.CENTER);
		this.setSpacing(50);
		this.setPadding(new Insets(10, 0, 10, 0));

		final Font textFont = new Font(font, 18);
		this.populationText = new Text("Population: " + this.population);
		this.populationText.setFill(Color.LIGHTBLUE);
		this.populationText.setFont(textFont);
		this.fireAntsCountText = new Text("Fire Ant: " + this.fireAntsCount);
		this.fireAntsCountText.setFill(Color.ORANGERED);
		this.fireAntsCountText.setFont(textFont);
		this.flashAntsCountText = new Text("Flash Ant: " + this.flashAntsCount);
		this.getFlashAntsCountText().setFill(Color.ORANGE);
		this.flashAntsCountText.setFont(textFont);

		this.startRestartButton = new Button("Start");
		this.startRestartButton.setFont(new Font(font, 16));
		this.startRestartButton.setPrefWidth(100);
		this.startRestartButton.setStyle(buttonBaseStyle);

		this.getChildren().addAll(populationText, fireAntsCountText, flashAntsCountText, startRestartButton);

		this.startRestartButton.setOnMouseClicked(e -> {
			if (Main.isActive()) {
				Main.stopSimulation();
				return;
			}

			Main.startSimulation();
		});
		this.startRestartButton.setOnMouseEntered(e -> this.startRestartButton.setStyle(
				buttonBaseStyle + "-fx-background-color: darkorange;" + " -fx-color: white; -fx-text-fill: white;"));
		this.startRestartButton.setOnMouseExited(e -> this.startRestartButton.setStyle(buttonBaseStyle));
	}
	
	public void rerenderTexts() {
		this.populationText.setText("Population: " + this.population);
		this.fireAntsCountText.setText("Fire Ant: " + this.fireAntsCount);
		this.flashAntsCountText.setText("Flash Ant: " + this.flashAntsCount);
	}

	public Button getStartRestartButton() {
		return startRestartButton;
	}

	public void setStartRestartButton(Button startRestartButton) {
		this.startRestartButton = startRestartButton;
	}

	public Text getPopulationText() {
		return populationText;
	}

	public void setPopulationText(Text populationText) {
		this.populationText = populationText;
	}

	public Text getFireAntsCountText() {
		return fireAntsCountText;
	}

	public void setFireAntsCountText(Text fireAntsCountText) {
		this.fireAntsCountText = fireAntsCountText;
	}

	public Text getFlashAntsCountText() {
		return flashAntsCountText;
	}

	public void setFlashAntsCountText(Text flashAntsCountText) {
		this.flashAntsCountText = flashAntsCountText;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public int getFireAntsCount() {
		return fireAntsCount;
	}

	public void setFireAntsCount(int fireAntsCount) {
		this.fireAntsCount = fireAntsCount;
	}

	public int getFlashAntsCount() {
		return flashAntsCount;
	}

	public void setFlashAntsCount(int flashAntsCount) {
		this.flashAntsCount = flashAntsCount;
	}

}
