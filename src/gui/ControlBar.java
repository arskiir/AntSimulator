package gui;

import app.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ControlBar extends HBox {

	private Label numberOfAntsLabel;
	private int numberOfAnts = 50;
	private TextField numberOfAntsField;

	private Button startRestartButton;
	private String buttonBaseStyle = "-fx-border-radius: 8px; -fx-background-color: white; -fx-border-style: solid; "
			+ "-fx-border-width: 3px; -fx-border-color: chocolate; -fx-background-radius: 8px;";

	private String font = "Consolas";

	public ControlBar() {
		super();
		
		this.setPrefWidth(Global.WIDTH);
		this.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null)));
		this.setAlignment(Pos.CENTER);
		this.setSpacing(10);
		this.setPadding(new Insets(10, 0, 10, 0));

		this.numberOfAntsLabel = new Label("Ants:");
		this.numberOfAntsLabel.setFont(new Font(font, 24));

		this.numberOfAntsField = new TextField(numberOfAnts + "");
		this.numberOfAntsField.setFont(new Font(font, 24));

		this.startRestartButton = new Button("Start");
		this.startRestartButton.setFont(new Font(font, 20));
		this.startRestartButton.setPrefWidth(150);
		this.startRestartButton.setStyle(buttonBaseStyle);

		this.getChildren().addAll(numberOfAntsLabel, numberOfAntsField, startRestartButton);

		this.numberOfAntsField.setOnKeyTyped(e -> {
			try {
				final int typedInt = Integer.parseInt(this.numberOfAntsField.getText()); // may throw
				this.numberOfAnts = typedInt;
			} catch (NumberFormatException e2) { // text field is left empty
				// this.numberOfAnts is still the old value here
			}
		});
		this.startRestartButton.setOnMouseClicked(e -> {
			if (Main.isActive()) {
				Main.stopSimultaion();
				return;
			}

			Main.startSimulation(this.numberOfAnts);
		});
		this.startRestartButton.setOnMouseEntered(e -> this.startRestartButton.setStyle(
				buttonBaseStyle + "-fx-background-color: darkorange;" + " -fx-color: white; -fx-text-fill: white;"));
		this.startRestartButton.setOnMouseExited(e -> this.startRestartButton.setStyle(buttonBaseStyle));
	}

	public Label getNumberOfAntsLabel() {
		return numberOfAntsLabel;
	}

	public void setNumberOfAntsLabel(Label numberOfAntsLabel) {
		this.numberOfAntsLabel = numberOfAntsLabel;
	}

	public int getNumberOfAnts() {
		return numberOfAnts;
	}

	public void setNumberOfAnts(int numberOfAnts) {
		this.numberOfAnts = numberOfAnts;
	}

	public TextField getNumberOfAntsField() {
		return numberOfAntsField;
	}

	public void setNumberOfAntsField(TextField numberOfAntsField) {
		this.numberOfAntsField = numberOfAntsField;
	}

	public Button getStartRestartButton() {
		return startRestartButton;
	}

	public void setStartRestartButton(Button startRestartButton) {
		this.startRestartButton = startRestartButton;
	}

}
