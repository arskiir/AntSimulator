package entity.base;

import java.util.Random;

import app.Main;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import utils.Vector;

/**
 * The Class Candy. Represents candies that ants will bring home to grow its
 * colony and give money.
 */
public class Candy implements Renderable {

	/** The image of the candy. */
	protected ImageView img;

	/** The position vector of the candy. */
	protected Vector position;

	/** Tells that this candy has been found by an ant or not. */
	protected boolean isFound;

	/** The height of the image. */
	protected static final int HEIGHT = 25;

	/** Used to randomize the image rotation. */
	protected static final Random random = new Random();

	/**
	 * Instantiates a new candy.
	 *
	 * @param imgPath  The path to the actual image.
	 * @param position The vector position of the candy.
	 */
	public Candy(final String imgPath, Vector position) {
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

	/**
	 * Renders the image on the pane to a new postion.
	 */
	@Override
	public void rerender() {
		final var simulationArea = Main.getSimulationArea();
		Platform.runLater(() -> {
			simulationArea.removeImage(this.img);
			this.img.relocate(this.position.getX(), -this.position.getY()); // window position
			simulationArea.addImage(this.img);
		});
	}

	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public ImageView getImg() {
		return img;
	}

	/**
	 * Sets the image.
	 *
	 * @param img the new image
	 */
	public void setImg(ImageView img) {
		this.img = img;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Vector getPosition() {
		return position;
	}

	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(Vector position) {
		this.position = position;
	}

	/**
	 * Checks if the candy has been found by an ant.
	 *
	 * @return true, if the candy has been found by an ant, else false.
	 */
	public boolean isFound() {
		return isFound;
	}

	/**
	 * Sets the isFound field.
	 *
	 * @param isFound the new found
	 */
	public void setFound(boolean isFound) {
		this.isFound = isFound;
	}
}
