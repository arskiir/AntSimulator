package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import app.Main;
import entity.base.Ant;
import entity.base.Ant.AntType;
import entity.derived.FlashAnt;
import entity.base.Food;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import utils.math.Vector;

public class SimulationArea extends Pane {

	private ImageView houseImage = new ImageView("house.png");
	private ArrayList<Ant> ants = new ArrayList<>();
	private CopyOnWriteArrayList<Food> foods = new CopyOnWriteArrayList<>();
	private ArrayList<String> foodImgPaths = new ArrayList<>() {
		private static final long serialVersionUID = -3891700759148049065L;
		{
			add("food1.png");
			add("food2.png");
			add("food3.png");
		}
	};
	private int createdFoodCount = 0;

	private static final int houseImageWidth = 50;
	private static final int width = Global.WIDTH;
	public static final int height = 800;
	private static final Vector origin = new Vector((double) width / 2, (double) -height / 2, 0); // in conventional x-y
																									// plane

	private int dragCount = 0; // if this is a multiple of a number, then add the food, to slow down
	private static final int dragMultiple = 4;

	public static final Random random = new Random();

	public SimulationArea() {
		super();
		setup();
	}

	private void setup() {
		this.setMinHeight(height);
		this.setPrefWidth(width);
		this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

		this.getChildren().add(houseImage);
		this.houseImage.setFitHeight(houseImageWidth);
		this.houseImage.setFitWidth(houseImageWidth);
		this.houseImage.relocate((double) width / 2 - (double) houseImageWidth / 2,
				(double) height / 2 - (double) houseImageWidth / 2);

		// click or drag to generate food
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
		this.setOnMouseClicked(e -> {
			ControlBar controlBar = Main.getControlBar();
			if (Main.isActive() && controlBar.getMoney() >= controlBar.getFoodCost()) {
				this.createFood(new Vector(e.getX(), -e.getY(), 0));
				controlBar.setMoney(controlBar.getMoney() - controlBar.getFoodCost());
				controlBar.rerender();
			}
		});
	}

	public void createFood(Vector position) {
		// create new Food object and add to container to keep track
		// also show it in the area

		this.createdFoodCount++;
		// food path changes depending on createdFoodCount
		final String foodPath = this.foodImgPaths.get(this.createdFoodCount % this.foodImgPaths.size());
		final Food food = new Food(foodPath, position);
		final ImageView foodImage = food.getImg();
		foodImage.relocate(food.getPosition().getX(), -food.getPosition().getY());
		this.foods.add(food);
		this.addImage(foodImage);
	}

	public void addAnt(final AntType type) {
		final ControlBar controlBar = Main.getControlBar();

		Ant ant = null;
		if (type == AntType.FIRE) {
			ant = new Ant(origin);
			controlBar.setFireAntsCount(controlBar.getFireAntsCount() + 1);
		} else {
			ant = new FlashAnt(origin);
			controlBar.setFlashAntsCount(controlBar.getFlashAntsCount() + 1);
		}

		final int updatedPopulation = controlBar.getPopulation() + 1;
		final int maxPopulation = 100;
		if (updatedPopulation >= maxPopulation) // no longer add ants after this threshold
			controlBar.setHasReachedMaxPopulation(true);
		controlBar.setPopulation(updatedPopulation);
		controlBar.rerender();

		ants.add(ant);
		ant.getFindFoodThread().start();
	}

	public void resetFoods() {
		this.foods.clear();
	}

	private void resetAnts() {
		this.ants.forEach(ant -> ant.getFindFoodThread().interrupt());
		this.ants.clear();
	}

	public void removeImage(ImageView img) {
		this.getChildren().remove(img);
	}

	public void addImage(ImageView img) {
		this.getChildren().add(img);
	}

	public void reset() {
		resetFoods();
		resetAnts();
		this.getChildren().clear();
		setup();
	}

	public Iterable<Ant> getAnts() {
		return this.ants;
	}

	public List<Food> getFoods() {
		return foods;
	}

	public void setFoods(List<Food> foods) {
		this.foods = (CopyOnWriteArrayList<Food>) foods;
	}

}
