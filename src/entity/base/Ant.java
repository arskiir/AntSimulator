package entity.base;

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
	
	protected int id;

	protected static int antCount = 0;

	public Ant(Vector home, double startingAngle) {
		this.id = ++antCount;
		this.origin = home; // 4th quadrant
		this.position = new Vector(home); // NOTE: home is passed in by reference D:
		this.img = new ImageView("ant.png");
		this.speed = 1d; // for normal ants
		this.velocity = Vector.createVector2FromAngle(startingAngle, this.speed);
	}

	public void findFood() {
		while (true) {
			try {
				Thread.sleep(10);
				move();
				if (this.hasFoundFood()) {
					// TODO
					System.out.println("Found food!");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
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

}
