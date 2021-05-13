package entity.base;

import java.util.Random;

import javafx.scene.image.ImageView;
import utils.Point;

public class Food {

	private ImageView img;
	private Point position;
	private static final int HEIGHT = 30;
	
	private static final Random random = new Random();

	public Food(final String imgPath, Point point) {
		this.img = new ImageView(imgPath);
		this.position = point;

		this.img.relocate(this.position.getX() - this.img.getFitWidth() / 2,
				this.position.getY() - this.img.getFitHeight() / 2);
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

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}
	
}
