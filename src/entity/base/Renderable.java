package entity.base;

import gui.NoImageException;
import javafx.scene.image.ImageView;

public interface Renderable {
	public void rerender();
	public ImageView getImg() throws NoImageException;
	public void setImg(ImageView img) throws NoImageException;
}
