package gui;

import app.Main;
import entity.base.Renderable;
import entity.base.Restartable;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import utils.Sound;

public class ControlBar extends HBox implements Renderable, Restartable {

	private Text moneyText;
	private Text populationText;
	private Text fireAntsCountText;
	private Text flashAntsCountText;
	private Text broughtHomeCandyCountText;
	private Text deadCountText;

	private double money;
	private int foodCost;
	private int baseFoodCost;
	private int baseMoney;

	private int population;
	private int fireAntsCount;
	private int flashAntsCount;
	private int broughtHomeCandyCount;
	private int deadCount;

	private Button startRestartButton;
	private String buttonBaseStyle = "-fx-border-radius: 8px; -fx-background-color: white; -fx-border-style: solid; "
			+ "-fx-border-width: 3px; -fx-border-color: chocolate; -fx-background-radius: 8px;";

	private String fontFamily = "Consolas";
	
	private static final MediaPlayer introPlayer = Sound.getMediaPlayer("res/game-intro.wav", 0.4);
	private static final MediaPlayer outroPlayer = Sound.getMediaPlayer("res/victory.wav", 1);

	public ControlBar() {
		super();

		this.baseFoodCost = 100;
		this.foodCost = baseFoodCost;
		this.baseMoney = foodCost * 200;
		this.money = baseMoney;

		this.population = 0;
		this.fireAntsCount = 0;
		this.flashAntsCount = 0;
		this.broughtHomeCandyCount = 0;
		this.deadCount = 0;

		this.setPrefWidth(Global.WIDTH);
		this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		this.setAlignment(Pos.CENTER);
		this.setSpacing(40);
		this.setPadding(new Insets(10, 0, 10, 0));

		final Font textFont = new Font(fontFamily, 18);
		this.moneyText = new Text();
		this.moneyText.setFill(Color.LIGHTPINK);
		this.moneyText.setFont(textFont);
		this.populationText = new Text();
		this.populationText.setFill(Color.LIGHTBLUE);
		this.populationText.setFont(textFont);
		this.fireAntsCountText = new Text();
		this.fireAntsCountText.setFill(Color.ORANGERED);
		this.fireAntsCountText.setFont(textFont);
		this.flashAntsCountText = new Text();
		this.flashAntsCountText.setFill(Color.ORANGE);
		this.flashAntsCountText.setFont(textFont);
		this.broughtHomeCandyCountText = new Text();
		this.broughtHomeCandyCountText.setFont(textFont);
		this.broughtHomeCandyCountText.setFill(Color.YELLOW);
		this.deadCountText = new Text();
		this.deadCountText.setFont(textFont);
		this.deadCountText.setFill(Color.LIGHTGRAY);
		
		this.rerender();

		this.startRestartButton = new Button("Start");
		this.startRestartButton.setFont(new Font(fontFamily, 16));
		this.startRestartButton.setPrefWidth(100);
		this.startRestartButton.setStyle(buttonBaseStyle);

		this.getChildren().addAll(moneyText, populationText, fireAntsCountText, flashAntsCountText,
				broughtHomeCandyCountText, deadCountText, startRestartButton);

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

	public int getDeadCount() {
		return deadCount;
	}

	public void setDeadCount(int deadCount) {
		this.deadCount = deadCount;
	}

	public int getBaseFoodCost() {
		return baseFoodCost;
	}

	@Override
	public void rerender() {
		Platform.runLater(() -> {
			if (this.money > 100000) {
				this.moneyText.setText("Money: WHO CARES");
				foodCost = 0;
			} else {
				this.moneyText.setText("Money: " + (int) this.money);
			}

			if (this.hasReachedMaxPopulation()) {
				this.populationText.setText("Population: " + this.population + " (max)");
			} else {
				this.populationText.setText("Population: " + this.population);
			}

			this.fireAntsCountText.setText("Fire Ant: " + this.fireAntsCount);
			this.flashAntsCountText.setText("Flash Ant: " + this.flashAntsCount);
			this.broughtHomeCandyCountText.setText("Candy: " + this.broughtHomeCandyCount);
			this.deadCountText.setText("Dead: " + this.deadCount);
		});

	}

	public boolean hasReachedMaxPopulation() {
		return this.population >= SimulationArea.maxPopulation;
	}

	public Button getStartRestartButton() {
		return startRestartButton;
	}

	public int getBasemoney() {
		return baseMoney;
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

	public double getMoney() {
		return money;
	}

	public void setMoney(double d) {
		this.money = d;
	}

	public int getBroughtHomeCandyCount() {
		return broughtHomeCandyCount;
	}

	public void setBroughtHomeCandyCount(int broughtHomeCandyCount) {
		this.broughtHomeCandyCount = broughtHomeCandyCount;
	}

	public int getFoodCost() {
		return foodCost;
	}

	public void setFoodCost(int foodCost) {
		this.foodCost = foodCost;
	}

	@Override
	public ImageView getImg() throws NoImageException {
		throw new NoImageException("There is no image on ControlBar. 🤣");
	}

	@Override
	public void setImg(ImageView img) throws NoImageException {
		throw new NoImageException("There is no image on ControlBar. 🤣");
	}

	@Override
	public void playIntroSound() {
		Sound.play(introPlayer);
	}

	@Override
	public void playOutroSound() {
		// play this when the max number of population is reached.
		Sound.play(outroPlayer);
	}

}
