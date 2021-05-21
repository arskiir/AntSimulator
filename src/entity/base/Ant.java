package entity.base;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import app.Main;
import gui.ControlBar;
import gui.Global;
import gui.SimulationArea;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import utils.math.Vector;

public class Ant {

	public enum AntType {
		FIRE, FLASH, MAX_TYPES
	}

	protected Vector velocity;
	protected Vector origin;
	protected Vector position;
	protected double speed;
	protected ImageView img;
	protected int steps;
	protected boolean hasFoundFood;
	protected boolean hasReachedFood;
	protected Food foundFood;
	protected int broughtHomeCount;
	protected double antHeight;
	protected double visionSpan;
	protected double visionDepth;

	protected Thread findFoodThread;

	protected int id;

	protected static int antCount = 0;

	protected static final Random random = new Random();

	public Ant(Vector home) {
		this.id = ++antCount;
		this.steps = 0;
		this.broughtHomeCount = 0;
		this.visionSpan = 90d;
		this.visionDepth = 100d;
		this.antHeight = 15;
		this.hasFoundFood = false;
		this.hasReachedFood = false;
		this.foundFood = null;
		this.origin = home; // 4th quadrant
		this.position = new Vector(home); // NOTE: home is passed in by reference D:
		this.img = new ImageView("ant.png");
		this.img.setFitHeight(this.antHeight);
		this.img.setPreserveRatio(true);
		this.speed = 1d; // for normal ants
		this.velocity = Vector.createVector2FromAngle(random.nextDouble() * 360, this.speed);
		this.findFoodThread = new Thread(() -> this.start());
	}

	protected void start() {
		while (true) {
			try {
				Thread.sleep(10);
				this.move();
				if (!this.hasFoundFood && this.foundFood == null) {
					this.foundFood = this.lookForFood();
					if (this.foundFood != null) {
						this.hasFoundFood = true;
						this.headToFood();
					}
				} else {
					if (this.isFoodReachable()) {
						this.hasReachedFood = true;
						this.foundFood.setPosition(this.position);
						this.headToHome();

						if (this.hasReachedHome()) {
							final SimulationArea simulationArea = Main.getSimulationArea();
							Platform.runLater(() -> {
								if (this.foundFood != null) {
									simulationArea.removeImage(this.foundFood.getImg());
									simulationArea.getFoods().remove(this.foundFood);
								}
								this.foundFood = null;
								this.hasFoundFood = false;
								this.hasReachedFood = false;
							});

							++this.broughtHomeCount;
							final ControlBar controlBar = Main.getControlBar();
							controlBar.setBroughtHomeCandyCount(controlBar.getBroughtHomeCandyCount() + 1);
							controlBar.rerenderTexts();
							this.reproduce(simulationArea);
						}
					}
				}

			} catch (InterruptedException e) {
				final SimulationArea simulationArea = Main.getSimulationArea();
				Platform.runLater(() -> simulationArea.removeImage(this.img));
				break;
			}
		}
	}

	protected void reproduce(SimulationArea simulationArea) {
		if (this.broughtHomeCount % 10 == 0) {
			// get some food to reproduce
			simulationArea.addAnt(AntType.FIRE);

			if (Ant.random.nextDouble() < 0.3)
				// chance to get flash ant
				simulationArea.addAnt(AntType.FLASH);
		}
	}

	private boolean hasReachedHome() {
		// return true if the ant with the food is about to reach home.
		final double range = 2;
		return this.getAntToHomeVector().modulus() <= range;
	}

	protected void headToHome() {
		final Vector antToHome = this.getAntToHomeVector();
		this.velocity = Vector.createVector2FromAngle(antToHome.getAngle(), this.speed);
	}

	protected Vector getAntToHomeVector() {
		return new Vector(this.origin.getX() - this.position.getX(), this.origin.getY() - this.position.getY(), 0d);
	}

	protected boolean isFoodReachable() {
		// return true if the food is now within a small range from the ant
		final double range = 5;
		return this.getAntToFoodVector().modulus() <= range;
	}

	protected void headToFood() {
		// make the velocity point to the food position
		final Vector antToFood = this.getAntToFoodVector();
		this.velocity = Vector.createVector2FromAngle(antToFood.getAngle(), this.speed);
	}

	protected Vector getAntToFoodVector() {
		final Vector foodPos = this.foundFood.getPosition();
		return new Vector(foodPos.getX() - this.position.getX(), foodPos.getY() - this.position.getY(), 0d);
	}

	protected Food lookForFood() {
		// return the first food in the array that is on sight
		// return null if there's no food on sight

		final Vector lower = Vector.createVector2FromAngle(this.velocity.getAngle() - this.visionSpan / 2,
				this.visionDepth);
		final Vector upper = Vector.createVector2FromAngle(lower.getAngle() + this.visionSpan, this.visionDepth);

		final CopyOnWriteArrayList<Food> foods = (CopyOnWriteArrayList<Food>) Main.getSimulationArea().getFoods();
		for (final Food food : foods) {
			if (food.isFound()) // this food currently belongs to another ant
				continue;
			final Vector foodPos = food.getPosition();
			final Vector antToFood = new Vector(foodPos.getX() - this.position.getX(),
					foodPos.getY() - this.position.getY(), 0d);

			final boolean isWithinRange = antToFood.modulus() <= upper.modulus();
			final boolean isWithinSpan = upper.getAngle() >= this.velocity.getAngle()
					&& this.velocity.getAngle() >= lower.getAngle();

			if (isWithinRange && isWithinSpan) {
				food.setFound(true); // to tell other ants not to mess with this food
				return food;
			}
		}
		return null;
	}

	protected void updateSteps() {
		// changes position
		this.position.setX(this.position.getX() + this.velocity.getX());
		this.position.setY(this.position.getY() + this.velocity.getY());
		this.steps++;
	}

	protected void randomizeDirection() {
		// random next velocity direction after some steps if hasn't found food
		if (!hasFoundFood && this.steps % (random.nextInt(200) + 20) == 0) {
			final double span = 180d;
			final double angleOffset = random.nextDouble() * span - span / 2;
			this.velocity = Vector.createVector2FromAngle(this.velocity.getAngle() + angleOffset, this.speed);
		}
	}

	protected void bounceWallIfNecessary() {
		// changes velocity direction if necessary
		if (this.position.getX() < 0) {
			this.velocity.reverseX();
			this.position.setX(0);
		} else if (this.position.getX() > Global.WIDTH) {
			this.velocity.reverseX();
			this.position.setX(Global.WIDTH);
		}
		if (this.position.getY() > 0) {
			this.velocity.reverseY();
			this.position.setY(0);
		} else if (this.position.getY() < -SimulationArea.height) {
			this.velocity.reverseY();
			this.position.setY(-SimulationArea.height);
		}
	}

	public void update() {
		this.updateSteps();
		this.randomizeDirection();
		this.bounceWallIfNecessary();
	}

	public void rerender() {
		final SimulationArea simulationArea = Main.getSimulationArea();
		Platform.runLater(() -> {
			simulationArea.removeImage(this.img);
			this.img.relocate(this.position.getX(), -this.position.getY()); // window position
			this.img.setRotate(90 - this.velocity.getAngle());
			simulationArea.addImage(this.img);
		});
	}

	protected void move() {
		update();
		rerender();
		if (this.hasReachedFood)
			this.foundFood.rerender();
	}

	public Thread getFindFoodThread() {
		return findFoodThread;
	}

}
