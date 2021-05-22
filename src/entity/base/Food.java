package entity.base;

import java.util.Random;

import app.Main;
import gui.SimulationArea;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import utils.Vector;

public class Food implements Renderable {

	protected ImageView img;
	protected Vector position;
	protected boolean isFound;

	protected static final int HEIGHT = 25;
	protected static final Random random = new Random();

	public Food(final String imgPath, Vector position) {
		this.img = new ImageView(imgPath);
		this.position = position;
		this.isFound = false;
		this.img.setFitHeight(HEIGHT);
		this.img.setPreserveRatio(true);
		this.img.setRotate(random.nextDouble() * 360);

		// to centralize the image to the click event location
		this.img.relocate(-this.position.getX() - this.img.getFitWidth() / 2,
				-this.position.getY() - this.img.getFitHeight() / 2);
	}

	@Override
	public void rerender() {
		final SimulationArea simulationArea = Main.getSimulationArea();
		Platform.runLater(() -> {
			simulationArea.removeImage(this.img);
			this.img.relocate(this.position.getX(), -this.position.getY()); // window position
			simulationArea.addImage(this.img);
		});
	}

	public ImageView getImg() {
		return img;
	}

	public void setImg(ImageView img) {
		this.img = img;
	}

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	public boolean isFound() {
		return isFound;
	}

	public void setFound(boolean isFound) {
		this.isFound = isFound;
	}

}
