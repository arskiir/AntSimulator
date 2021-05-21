package gui;

import app.Main;
import entity.base.Renderable;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ControlBar extends HBox implements Renderable {

	private Text moneyText;
	private Text populationText;
	private Text fireAntsCountText;
	private Text flashAntsCountText;
	private Text broughtHomeCandyCountText;

	private int money;
	private static int foodCost = 100;
	private static final int baseMoney = foodCost * 200;// able to buy 200 pieces right off start

	private int population;
	private int fireAntsCount;
	private int flashAntsCount;
	private int broughtHomeCandyCount;

	private Button startRestartButton;
	private String buttonBaseStyle = "-fx-border-radius: 8px; -fx-background-color: white; -fx-border-style: solid; "
			+ "-fx-border-width: 3px; -fx-border-color: chocolate; -fx-background-radius: 8px;";

	private String font = "Consolas";
	private boolean hasReachedMaxPopulation;

	public ControlBar() {
		super();

		this.money = foodCost * 200;
		this.hasReachedMaxPopulation = false;

		this.population = 0;
		this.fireAntsCount = 0;
		this.flashAntsCount = 0;
		this.broughtHomeCandyCount = 0;

		this.setPrefWidth(Global.WIDTH);
		this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		this.setAlignment(Pos.CENTER);
		this.setSpacing(50);
		this.setPadding(new Insets(10, 0, 10, 0));

		final Font textFont = new Font(font, 18);
		this.moneyText = new Text("Money: " + this.money);
		this.moneyText.setFill(Color.LIGHTPINK);
		this.moneyText.setFont(textFont);
		this.populationText = new Text("Population: " + this.population);
		this.populationText.setFill(Color.LIGHTBLUE);
		this.populationText.setFont(textFont);
		this.fireAntsCountText = new Text("Fire Ant: " + this.fireAntsCount);
		this.fireAntsCountText.setFill(Color.ORANGERED);
		this.fireAntsCountText.setFont(textFont);
		this.flashAntsCountText = new Text("Flash Ant: " + this.flashAntsCount);
		this.flashAntsCountText.setFill(Color.ORANGE);
		this.flashAntsCountText.setFont(textFont);
		this.broughtHomeCandyCountText = new Text("Candy: " + this.broughtHomeCandyCount);
		this.broughtHomeCandyCountText.setFont(textFont);
		this.broughtHomeCandyCountText.setFill(Color.YELLOW);

		this.startRestartButton = new Button("Start");
		this.startRestartButton.setFont(new Font(font, 16));
		this.startRestartButton.setPrefWidth(100);
		this.startRestartButton.setStyle(buttonBaseStyle);

		this.getChildren().addAll(moneyText, populationText, fireAntsCountText, flashAntsCountText,
				broughtHomeCandyCountText, startRestartButton);

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

	@Override
	public void rerender() {
		Platform.runLater(() -> {
			if (this.money > 100000) {
				this.moneyText.setText("Money: WHO CARES");
				foodCost = 0;
			} else {
				this.moneyText.setText("Money: " + this.money);
			}

			if (hasReachedMaxPopulation) {
				this.populationText.setText("Population: " + this.population + " (max)");
			} else {
				this.populationText.setText("Population: " + this.population);
			}

			this.fireAntsCountText.setText("Fire Ant: " + this.fireAntsCount);
			this.flashAntsCountText.setText("Flash Ant: " + this.flashAntsCount);
			this.broughtHomeCandyCountText.setText("Candy: " + this.broughtHomeCandyCount);
		});

	}

	public Button getStartRestartButton() {
		return startRestartButton;
	}

	public boolean isHasReachedMaxPopulation() {
		return hasReachedMaxPopulation;
	}

	public void setHasReachedMaxPopulation(boolean hasReachedMaxPopulation) {
		this.hasReachedMaxPopulation = hasReachedMaxPopulation;
	}

	public void setStartRestartButton(Button startRestartButton) {
		this.startRestartButton = startRestartButton;
	}

	public static int getBasemoney() {
		return baseMoney;
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

	public Text getBroughtHomeCandyCountText() {
		return broughtHomeCandyCountText;
	}

	public Text getMoneyText() {
		return moneyText;
	}

	public void setMoneyText(Text moneyText) {
		this.moneyText = moneyText;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public void setBroughtHomeCandyCountText(Text broughtHomeCandyCountText) {
		this.broughtHomeCandyCountText = broughtHomeCandyCountText;
	}

	public int getBroughtHomeCandyCount() {
		return broughtHomeCandyCount;
	}

	public void setBroughtHomeCandyCount(int broughtHomeCandyCount) {
		this.broughtHomeCandyCount = broughtHomeCandyCount;
	}

	public String getButtonBaseStyle() {
		return buttonBaseStyle;
	}

	public void setButtonBaseStyle(String buttonBaseStyle) {
		this.buttonBaseStyle = buttonBaseStyle;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public int getFoodCost() {
		return foodCost;
	}

	@Override
	public ImageView getImg() throws NoImageException {
		throw new NoImageException("There is no image on ControlBar. 🤣");
	}

	@Override
	public void setImg(ImageView img) throws NoImageException {
		throw new NoImageException("There is no image on ControlBar. 🤣");
	}

}
