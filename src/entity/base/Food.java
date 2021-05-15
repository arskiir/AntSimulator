package entity.base;

import java.util.Random;

import javafx.scene.image.ImageView;
import utils.math.Vector;

public class Food {

	protected ImageView img;
	protected Vector position;
	protected static final int HEIGHT = 30;
	
	protected static final Random random = new Random();

	public Food(final String imgPath, Vector position) {
		this.img = new ImageView(imgPath);
		this.position = position;

		this.img.relocate(-this.position.getX() - this.img.getFitWidth() / 2,
				-this.position.getY() - this.img.getFitHeight() / 2);
		this.img.setFitHeight(HEIGHT);
		this.img.setPreserveRatio(true);
		this.img.setRotate(random.nextDouble() * 360);
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
	
}
