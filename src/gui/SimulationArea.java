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

	/** The container for all living ants. */
	private ArrayList<Ant> ants = new ArrayList<>();
	
	/** The container for all candies. */
	private CopyOnWriteArrayList<Candy> candies = new CopyOnWriteArrayList<>();
	
	/** The house image. */
	private ImageView houseImage = new ImageView("house.png");
	
	/** The paths for candy images. */
	private ArrayList<String> candyImgPaths = new ArrayList<>() {
		private static final long serialVersionUID = -3891700759148049065L;
		{
			add("candy1.png");
			add("candy2.png");
			add("candy3.png");
		}
	};
	
	/** The created candy count. */
	private int createdCandyCount = 0;

	/** The maximum number of population. */
	public static final int maxPopulation = 100;
	
	/** Tells if the current population size has reached the maxPopulation or not. */
	private boolean hasReachedMaxPopulation = false;

	/** The width of the house image. Note: the height is the same for this specific design */
	private static final int houseImageWidth = 50;
	
	/** The height of the area, i.e. the pane */
	public static final int height = 800;
	
	/** The vector to the center of the area. */
	public static final Vector origin = new Vector((double) Global.WIDTH / 2, (double) -height / 2, 0);

	/** Gets incremented on MouseDragged event */
	private int dragCount = 0;
	
	/** Helps slow down the generation rate of candies. */
	private static final int dragMultiple = 4;

	/** Used to random numbers */
	public static final Random random = new Random();

	/**
	 * Instantiates a new simulation area.
	 */
	public SimulationArea() {
		super();
		this.setupUI();
		this.setupCreateCandyEventListeners();
	}

	/**
	 * Setups create candy event listeners.
	 */
	private void setupCreateCandyEventListeners() {
		this.setOnMouseDragged(e -> {
			this.dragCount++;
			var controlBar = Main.getControlBar();
			if (this.dragCount % SimulationArea.dragMultiple == 0 && Main.isActive()
					&& controlBar.getMoney() >= controlBar.getFoodCost()) {
				this.createCandy(new Vector(e.getX(), -e.getY(), 0));
				controlBar.setMoney(controlBar.getMoney() - controlBar.getFoodCost());
				controlBar.rerender();
			}
		});
		this.setOnMousePressed(e -> {
			var controlBar = Main.getControlBar();
			if (Main.isActive() && controlBar.getMoney() >= controlBar.getFoodCost()) {
				this.createCandy(new Vector(e.getX(), -e.getY(), 0));
				controlBar.setMoney(controlBar.getMoney() - controlBar.getFoodCost());
				controlBar.rerender();
			}
		});
	}

	/**
	 * Setups UI.
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
	 * Checks if hasReachedMaxPopulation
	 *
	 * @return true, if hasReachedMaxPopulation == true, else false
	 */
	public boolean getHasReachedMaxPopulation() {
		return hasReachedMaxPopulation;
	}

	/**
	 * Sets hasReachedMaxPopulation.
	 *
	 * @param hasReachedMaxPopulation the new boolean value
	 */
	public void setHasReachedMaxPopulation(boolean hasReachedMaxPopulation) {
		this.hasReachedMaxPopulation = hasReachedMaxPopulation;
	}

	/**
	 * Creates a piece of candy, adds to the container, and shows it on the area.
	 *
	 * @param position the position vector
	 */
	public void createCandy(Vector position) {
		// create new Food object and add to container to keep track
		// also show it in the area

		this.createdCandyCount++;
		Candy food;
		if (random.nextDouble() < 0.01) {
			// poison food
			food = new PoisonousCandy("poop.png", position);
		} else {
			// normal food
			// food path changes depending on createdFoodCount
			final String foodPath = this.candyImgPaths.get(this.createdCandyCount % this.candyImgPaths.size());
			food = new Candy(foodPath, position);
		}

		final ImageView foodImage = food.getImg();
		foodImage.relocate(food.getPosition().getX(), -food.getPosition().getY());
		this.candies.add(food);
		this.addImage(foodImage);
	}

	/**
	 * Creates a new ant, adds to the container, and starts it journey thread.
	 *
	 * @param type the ant type
	 */
	public void addAnt(final AntType type) {
		final var controlBar = Main.getControlBar();

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
		if (updatedPopulation >= maxPopulation && !this.hasReachedMaxPopulation) { // no longer add ants after this threshold
			this.hasReachedMaxPopulation = true;
			controlBar.playOutroSound();
		}
		controlBar.setPopulation(updatedPopulation);
		controlBar.rerender();

		ants.add(ant);
		ant.getSettingOffJourneyThread().start();
	}

	/**
	 * Hides and deletes all candies from the area.
	 */
	public void resetCandies() {
		this.candies.forEach(food -> food.getImg().setVisible(false));
		this.candies.clear();
	}

	/**
	 * Ends all ants' journeys and delete them.
	 */
	private void resetAnts() {
		this.ants.forEach(ant -> ant.getSettingOffJourneyThread().interrupt());
		this.ants.clear();
	}

	/**
	 * Removes an image from the area.
	 *
	 * @param img the image to be removed
	 */
	public void removeImage(ImageView img) {
		this.getChildren().remove(img);
	}

	/**
	 * Shows the image on the area.
	 *
	 * @param img the image to be shown
	 */
	public void addImage(ImageView img) {
		this.getChildren().add(img);
	}

	/**
	 * Resets every field to its default values.
	 */
	public void reset() {
		this.getChildren().clear();
		resetCandies();
		resetAnts();
		this.hasReachedMaxPopulation = false;
		setupUI();
	}

	/**
	 * Gets the ants.
	 *
	 * @return the list of ants
	 */
	public List<Ant> getAnts() {
		return this.ants;
	}

	/**
	 * Gets the candies.
	 *
	 * @return the candies
	 */
	public List<Candy> getCandies() {
		return candies;
	}

	/**
	 * Sets the candies.
	 *
	 * @param candies the new list of candies
	 */
	public void setCandies(List<Candy> candies) {
		this.candies = (CopyOnWriteArrayList<Candy>) candies;
	}

}
