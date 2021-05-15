package entity.base;

import java.util.Random;

import app.Main;
import gui.Global;
import gui.SimulationArea;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import utils.math.Vector;

public class Ant {

	protected Vector velocity;
	protected Vector origin;
	protected Vector position;
	protected double speed;
	protected ImageView img;
	protected int steps;
	
	protected Thread findFoodThread;

	protected int id;

	protected static int antCount = 0;

	protected static final Random random = new Random();

	public Ant(Vector home, double startingAngle) {
		this.id = ++antCount;
		this.steps = 0;
		this.origin = home; // 4th quadrant
		this.position = new Vector(home); // NOTE: home is passed in by reference D:
		this.img = new ImageView("ant.png");
		this.img.setFitHeight(20);
		this.img.setPreserveRatio(true);
		this.speed = 1d; // for normal ants
		this.velocity = Vector.createVector2FromAngle(random.nextDouble() * 360, this.speed);
		this.findFoodThread = new Thread(() -> this.findFood());
	}

	protected void findFood() {
		while (true) {
			try {
				Thread.sleep(10);
				move();
				if (this.hasFoundFood()) {
					// TODO
					System.out.println("Found food!");
				}
			} catch (InterruptedException e) {
				final SimulationArea simulationArea = Main.getSimulationArea();
				Platform.runLater(() -> simulationArea.removeImage(this.img));
				break;
			}
		}
	}

	protected boolean hasFoundFood() {
		// TODO
		return false;
	}

	public void update() {
		// changes position
		this.position.setX(this.position.getX() + this.velocity.getX());
		this.position.setY(this.position.getY() + this.velocity.getY());
		this.steps++;

		// random next velocity direction after some steps
		if (this.steps % (random.nextInt(200) + 20) == 0) {
//			final double span = 235d;
//			final double angleOffset = random.nextDouble() * span - span / 2;
			this.velocity = Vector.createVector2FromAngle(random.nextDouble() * 360, this.speed);
//			this.velocity = Vector.createVector2FromAngle(this.velocity.getAngle(), this.speed);
		}

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
	}

	public Thread getFindFoodThread() {
		return findFoodThread;
	}

}
