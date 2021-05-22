package entity.base;

import gui.NoImageException;
import javafx.scene.image.ImageView;

/**
 * The Interface Renderable. Any class that implements this must be able to
 * render to the pane.
 */
public interface Renderable {

	/**
	 * Renders to the pane.
	 */
	public void rerender();

	/**
	 * Gets the image.
	 *
	 * @return the image
	 * @throws NoImageException the no image exception
	 */
	public ImageView getImg() throws NoImageException;

	/**
	 * Sets the image.
	 *
	 * @param img the new image
	 * @throws NoImageException the no image exception
	 */
	public void setImg(ImageView img) throws NoImageException;
}
