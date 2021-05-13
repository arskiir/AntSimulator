package gui;

import java.util.ArrayList;

import app.Main;
import entity.base.Food;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import utils.Point;

public class SimulationArea extends Pane {

	private ImageView houseImage = new ImageView("house.png");

	private ArrayList<Food> foods = new ArrayList<>();
	private ArrayList<String> foodPaths = new ArrayList<>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3891700759148049065L;

		{
			add("food1.png");
			add("food2.png");
			add("food3.png");
		}
	};
	private int createdFoodCount = 0;

	private static final int houseImageWidth = 80;
	private static final int width = Global.WIDTH;
	private static final int height = 850;

	private int dragCount = 0; // if this is a multiple of a number, then add the food, to slow down
	private static final int dragMultiple = 4;

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

		this.setOnMouseDragged(e -> {
			this.dragCount++;
			if (this.dragCount % SimulationArea.dragMultiple == 0 && Main.isActive())
				this.createFood(e.getX(), e.getY());
		});
	}

	public void createFood(final double x, final double y) {
		// create new Food object and add to container to keep track
		// also show it in the area

		this.createdFoodCount++;
		// food path changes depending on createdFoodCount
		final String foodPath = this.foodPaths.get(this.createdFoodCount % this.foodPaths.size());
		final Point position = new Point(x, y);
		final Food food = new Food(foodPath, position);
		this.foods.add(food);
		this.getChildren().add(food.getImg());
	}

}
