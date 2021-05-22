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

/**
 * The Class ControlBar. The upper user interface part which controls the game active state and displays the game status.
 */
public class ControlBar extends HBox implements Renderable, Restartable {

	/** The money text. */
	private Text moneyText;
	
	/** The population text. */
	private Text populationText;
	
	/** The fire ants count text. */
	private Text fireAntsCountText;
	
	/** The flash ants count text. */
	private Text flashAntsCountText;
	
	/** The brought home candy count text. */
	private Text broughtHomeCandyCountText;
	
	/** The dead count text. */
	private Text deadCountText;

	/** The money the player has. */
	private double money;
	
	/** Tells if the player has reached the amount of money that makes the candy costs nothing. */
	private boolean hasReachedMaxMoney;
	
	/** The candy cost. */
	private int candyCost;
	
	/** The base candy cost. */
	private int baseCandyCost;
	
	/** The base money. */
	private int baseMoney;

	/** The population count. */
	private int population;
	
	/** The fire ants count. */
	private int fireAntsCount;
	
	/** The flash ants count. */
	private int flashAntsCount;
	
	/** The brought home candy count. */
	private int broughtHomeCandyCount;
	
	/** The dead count. */
	private int deadCount;

	/** The start or stop button. */
	private Button startStopButton;
	
	/** The button base style. */
	private String buttonBaseStyle = "-fx-border-radius: 8px; -fx-background-color: white; -fx-border-style: solid; "
			+ "-fx-border-width: 3px; -fx-border-color: chocolate; -fx-background-radius: 8px;";

	/** The media player that plays when the simulation starts. */
	private static final MediaPlayer introPlayer = Sound.getMediaPlayer("res/intro.wav", 0.2);
	
	/** The media player that plays when the population size first reaches a certain number. */
	private static final MediaPlayer outroPlayer = Sound.getMediaPlayer("res/megalovania_intro.mp3", .2);
	
	/** The media player that plays when the amount of money first reaches a certain amout. */
	private static final MediaPlayer maxMoneyPlayer = Sound.getMediaPlayer("res/money.wav", .2);

	/**
	 * Instantiates a new control bar.
	 */
	public ControlBar() {
		super();

		this.setHasReachedMaxMoney(false);
		this.setupNumericVariables();
		this.setupLayout();

		final var fontFamily = "Consolas";
		final var textFont = new Font(fontFamily, 18);
		this.setupTexts(textFont);
		this.setupButton(fontFamily);

		this.getChildren().addAll(moneyText, populationText, fireAntsCountText, flashAntsCountText,
				broughtHomeCandyCountText, deadCountText, startStopButton);
		this.rerender();
	}

	/**
	 * Setup numeric variables.
	 */
	private void setupNumericVariables() {
		this.baseCandyCost = 100;
		this.candyCost = baseCandyCost;
		this.baseMoney = candyCost * 50;
		this.money = baseMoney;

		this.population = 0;
		this.fireAntsCount = 0;
		this.flashAntsCount = 0;
		this.broughtHomeCandyCount = 0;
		this.deadCount = 0;
	}

	/**
	 * Setup layout.
	 */
	private void setupLayout() {
		this.setPrefWidth(Global.WIDTH);
		this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		this.setAlignment(Pos.CENTER);
		this.setSpacing(40);
		this.setPadding(new Insets(10, 0, 10, 0));
	}

	/**
	 * Sets the up button.
	 *
	 * @param fontFamily the font family for the button text
	 */
	private void setupButton(String fontFamily) {
		this.startStopButton = new Button("Start");
		this.startStopButton.setFont(new Font(fontFamily, 16));
		this.startStopButton.setPrefWidth(100);
		this.startStopButton.setStyle(buttonBaseStyle);

		this.startStopButton.setOnMouseClicked(e -> {
			if (Main.isActive()) {
				Main.stopSimulation();
				return;
			}

			Main.startSimulation();
		});
		this.startStopButton.setOnMouseEntered(e -> this.startStopButton.setStyle(
				buttonBaseStyle + "-fx-background-color: darkorange;" + " -fx-color: white; -fx-text-fill: white;"));
		this.startStopButton.setOnMouseExited(e -> this.startStopButton.setStyle(buttonBaseStyle));
	}

	/**
	 * Sets the up all texts.
	 *
	 * @param textFont the font instance for all texts
	 */
	private void setupTexts(Font textFont) {
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
	}

	/**
	 * Gets the dead count.
	 *
	 * @return the dead count
	 */
	public int getDeadCount() {
		return deadCount;
	}

	/**
	 * Sets the dead count.
	 *
	 * @param deadCount the new dead count
	 */
	public void setDeadCount(int deadCount) {
		this.deadCount = deadCount;
	}

	/**
	 * Gets the base candy cost.
	 *
	 * @return the base candy cost
	 */
	public int getBaseCandyCost() {
		return baseCandyCost;
	}

	/**
	 * Renders all texts with updated values.
	 */
	@Override
	public void rerender() {
		final var maxMoney = 100_000;
		Platform.runLater(() -> {
			if (this.money > maxMoney) {
				if (!this.hasReachedMaxMoney)
					Sound.play(maxMoneyPlayer);
				this.hasReachedMaxMoney = true;
				this.moneyText.setText("Money: WHO CARES");
				candyCost = 0;
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

	/**
	 * Checks if the current population size exceeds the max population size.
	 *
	 * @return true, if population size >= max population size, else false
	 */
	public boolean hasReachedMaxPopulation() {
		return this.population >= SimulationArea.maxPopulation;
	}

	/**
	 * Gets the start or stop button.
	 *
	 * @return the start or stop button
	 */
	public Button getStartStopButton() {
		return startStopButton;
	}

	/**
	 * Gets the base money.
	 *
	 * @return the base money
	 */
	public int getBasemoney() {
		return baseMoney;
	}

	/**
	 * Gets the population size.
	 *
	 * @return the population size
	 */
	public int getPopulation() {
		return population;
	}

	/**
	 * Sets the population.
	 *
	 * @param population the new population
	 */
	public void setPopulation(int population) {
		this.population = population;
	}

	/**
	 * Gets the fire ants count.
	 *
	 * @return the fire ants count
	 */
	public int getFireAntsCount() {
		return fireAntsCount;
	}

	/**
	 * Sets the fire ants count.
	 *
	 * @param fireAntsCount the new fire ants count
	 */
	public void setFireAntsCount(int fireAntsCount) {
		this.fireAntsCount = fireAntsCount;
	}

	/**
	 * Gets the flash ants count.
	 *
	 * @return the flash ants count
	 */
	public int getFlashAntsCount() {
		return flashAntsCount;
	}

	/**
	 * Sets the flash ants count.
	 *
	 * @param flashAntsCount the new flash ants count
	 */
	public void setFlashAntsCount(int flashAntsCount) {
		this.flashAntsCount = flashAntsCount;
	}

	/**
	 * Gets the money.
	 *
	 * @return the money
	 */
	public double getMoney() {
		return money;
	}

	/**
	 * Sets the money.
	 *
	 * @param money the new money
	 */
	public void setMoney(double money) {
		this.money = money;
	}

	/**
	 * Gets the brought home candy count.
	 *
	 * @return the brought home candy count
	 */
	public int getBroughtHomeCandyCount() {
		return broughtHomeCandyCount;
	}

	/**
	 * Sets the brought home candy count.
	 *
	 * @param broughtHomeCandyCount the new brought home candy count
	 */
	public void setBroughtHomeCandyCount(int broughtHomeCandyCount) {
		this.broughtHomeCandyCount = broughtHomeCandyCount;
	}

	/**
	 * Gets the food cost.
	 *
	 * @return the food cost
	 */
	public int getFoodCost() {
		return candyCost;
	}

	/**
	 * Sets the food cost.
	 *
	 * @param foodCost the new food cost
	 */
	public void setFoodCost(int foodCost) {
		this.candyCost = foodCost;
	}

	/**
	 * Throw exception when called since there's no image for this.
	 *
	 * @throws NoImageException the no image exception
	 */
	@Override
	public ImageView getImg() throws NoImageException {
		throw new NoImageException("There is no image on ControlBar.");
	}

	/**
	 * Throw exception when called since there's no image for this.
	 *
	 * @throws NoImageException the no image exception
	 */
	@Override
	public void setImg(ImageView img) throws NoImageException {
		throw new NoImageException("There is no image on ControlBar.");
	}

	/**
	 * Plays a sound indicating the start of the simulation.
	 */
	@Override
	public void playIntroSound() {
		Sound.play(introPlayer);
	}

	/**
	 * Plays a sound when the max number of population is reached.
	 */
	@Override
	public void playOutroSound() {
		Sound.play(outroPlayer);
	}

	/**
	 * Sets the hasReachedMaxMoney field.
	 *
	 * @param hasReachedMaxMoney the new boolean value
	 */
	public void setHasReachedMaxMoney(boolean hasReachedMaxMoney) {
		this.hasReachedMaxMoney = hasReachedMaxMoney;
	}

}
