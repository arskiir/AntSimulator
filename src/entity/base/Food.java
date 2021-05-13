package entity.base;

import javafx.scene.image.ImageView;
import utils.Point;

public class Food {

	private ImageView img;
	private Point position;

	public Food(final String imgPath, Point point) {
		this.img = new ImageView(imgPath);
		this.position = point;

		this.img.relocate(this.position.getX() - this.img.getFitWidth() / 2,
				this.position.getY() - this.img.getFitHeight() / 2);
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
