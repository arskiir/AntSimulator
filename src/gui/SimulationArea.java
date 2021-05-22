package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import app.Main;
import entity.base.Ant;
import entity.base.Ant.AntType;
import entity.derived.FlashAnt;
import entity.derived.PoisonousCandy;
import entity.base.Candy;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import utils.Vector;

/**
 * The Class SimulationArea. An area where all actions happen.
 */
public class SimulationArea extends Pane {

	/** The ants. */
	private ArrayList<Ant> ants = new ArrayList<>();
	
	/** The foods. */
	private CopyOnWriteArrayList<Candy> foods = new CopyOnWriteArrayList<>();
	
	/** The house image. */
	private ImageView houseImage = new ImageView("house.png");
	
	/** The food img paths. */
	private ArrayList<String> foodImgPaths = new ArrayList<>() {
		private static final long serialVersionUID = -3891700759148049065L;
		{
			add("food1.png");
			add("food2.png");
			add("food3.png");
		}
	};
	
	/** The created food count. */
	private int createdFoodCount = 0;

	/** The Constant maxPopulation. */
	public static final int maxPopulation = 100;
	
	/** The has won. */
	private boolean hasWon = false;

	/** The Constant houseImageWidth. */
	private static final int houseImageWidth = 50;
	
	/** The Constant height. */
	public static final int height = 800;
	
	/** The Constant origin. */
	public static final Vector origin = new Vector((double) Global.WIDTH / 2, (double) -height / 2, 0);

	/** The drag count. */
	private int dragCount = 0; // if this is a multiple of a number, then add the food, to slow down
	
	/** The Constant dragMultiple. */
	private static final int dragMultiple = 4;

	/** The Constant random. */
	public static final Random random = new Random();

	/**
	 * Instantiates a new simulation area.
	 */
	public SimulationArea() {
		super();
		this.setupUI();
		this.setupCreateFoodEventListeners();
	}

	/**
	 * Setup create food event listeners.
	 */
	private void setupCreateFoodEventListeners() {
		this.setOnMouseDragged(e -> {
			this.dragCount++;
			ControlBar controlBar = Main.getControlBar();
			if (this.dragCount % SimulationArea.dragMultiple == 0 && Main.isActive()
					&& controlBar.getMoney() >= controlBar.getFoodCost()) {
				this.createFood(new Vector(e.getX(), -e.getY(), 0));
				controlBar.setMoney(controlBar.getMoney() - controlBar.getFoodCost());
				controlBar.rerender();
			}
		});
		this.setOnMousePressed(e -> {
			ControlBar controlBar = Main.getControlBar();
			if (Main.isActive() && controlBar.getMoney() >= controlBar.getFoodCost()) {
				this.createFood(new Vector(e.getX(), -e.getY(), 0));
				controlBar.setMoney(controlBar.getMoney() - controlBar.getFoodCost());
				controlBar.rerender();
			}
		});
	}

	/**
	 * Setup UI.
	 */
	private void setupUI() {
		this.setMinHeight(height);
		this.setPrefWidth(Global.WIDTH);
		this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

		this.getChildren().add(houseImage);
		this.houseImage.setFitHeight(houseImageWidth);
		this.houseImage.setFitWidth(houseImageWidth);
		this.houseImage.relocate((double) Global.WIDTH / 2 - (double) houseImageWidth / 2,
				(double) height / 2 - (double) houseImageWidth / 2);

	}

	/**
	 * Checks if is checks for won.
	 *
	 * @return true, if is checks for won
	 */
	public boolean isHasWon() {
		return hasWon;
	}

	/**
	 * Sets the checks for won.
	 *
	 * @param hasWon the new checks for won
	 */
	public void setHasWon(boolean hasWon) {
		this.hasWon = hasWon;
	}

	/**
	 * Creates the food.
	 *
	 * @param position the position
	 */
	public void createFood(Vector position) {
		// create new Food object and add to container to keep track
		// also show it in the area

		this.createdFoodCount++;
		Candy food;
		if (random.nextDouble() < 0.01) {
			// poison food
			food = new PoisonousCandy("poop.png", position);
		} else {
			// normal food
			// food path changes depending on createdFoodCount
			final String foodPath = this.foodImgPaths.get(this.createdFoodCount % this.foodImgPaths.size());
			food = new Candy(foodPath, position);
		}

		final ImageView foodImage = food.getImg();
		foodImage.relocate(food.getPosition().getX(), -food.getPosition().getY());
		this.foods.add(food);
		this.addImage(foodImage);
	}

	/**
	 * Adds the ant.
	 *
	 * @param type the type
	 */
	public void addAnt(final AntType type) {
		final ControlBar controlBar = Main.getControlBar();

		Ant ant = null;
		if (type == AntType.FIRE) {
			ant = new Ant();
			controlBar.setFireAntsCount(controlBar.getFireAntsCount() + 1);
		} else {
			ant = new FlashAnt();
			ant.playIntroSound();
			controlBar.setFlashAntsCount(controlBar.getFlashAntsCount() + 1);
		}

		final int updatedPopulation = controlBar.getPopulation() + 1;
		if (updatedPopulation >= maxPopulation && !this.hasWon) { // no longer add ants after this threshold
			this.hasWon = true;
			controlBar.playOutroSound();
		}
		controlBar.setPopulation(updatedPopulation);
		controlBar.rerender();

		ants.add(ant);
		ant.getSettingOffJourneyThread().start();
	}

	/**
	 * Reset foods.
	 */
	public void resetFoods() {
		this.foods.forEach(food -> food.getImg().setVisible(false));
		this.foods.clear();
	}

	/**
	 * Reset ants.
	 */
	private void resetAnts() {
		this.ants.forEach(ant -> ant.getSettingOffJourneyThread().interrupt());
		this.ants.clear();
	}

	/**
	 * Removes the image.
	 *
	 * @param img the img
	 */
	public void removeImage(ImageView img) {
		this.getChildren().remove(img);
	}

	/**
	 * Adds the image.
	 *
	 * @param img the img
	 */
	public void addImage(ImageView img) {
		this.getChildren().add(img);
	}

	/**
	 * Reset.
	 */
	public void reset() {
		this.getChildren().clear();
		resetFoods();
		resetAnts();
		this.hasWon = false;
		setupUI();
	}

	/**
	 * Gets the ants.
	 *
	 * @return the ants
	 */
	public List<Ant> getAnts() {
		return this.ants;
	}

	/**
	 * Gets the foods.
	 *
	 * @return the foods
	 */
	public List<Candy> getFoods() {
		return foods;
	}

	/**
	 * Sets the foods.
	 *
	 * @param foods the new foods
	 */
	public void setFoods(List<Candy> foods) {
		this.foods = (CopyOnWriteArrayList<Candy>) foods;
	}

}
