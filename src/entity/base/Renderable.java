package entity.base;

import javafx.scene.image.ImageView;

public interface Renderable {
	public void rerender();
	public ImageView getImg();
	public void setImg(ImageView img);
}
