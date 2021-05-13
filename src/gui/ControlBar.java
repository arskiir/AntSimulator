package gui;

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
	private int numberOfAnts;
	private TextField numberOfAntsField;
	private Button startRestartButton;
	private String font = "Consolas";

	public ControlBar() {
		this.setPrefHeight(80);
		this.setPrefWidth(1500);
		this.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null)));
		this.setAlignment(Pos.CENTER);
		this.setSpacing(10);

		this.numberOfAnts = 50;

		this.numberOfAntsLabel = new Label("Ants:");
		this.numberOfAntsLabel.setFont(new Font(font, 36));
		
		this.numberOfAntsField = new TextField();
		this.numberOfAntsField.setFont(new Font(font, 32));
		
		this.startRestartButton = new Button("Start");
		this.startRestartButton.setFont(new Font(font, 32));
		
		this.getChildren().addAll(numberOfAntsLabel, numberOfAntsField, startRestartButton);
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
