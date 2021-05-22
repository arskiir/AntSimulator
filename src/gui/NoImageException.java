package gui;

/**
 * The Class NoImageException. Used to throw when a method relating to
 * Renderable interface is called inappropriately
 */
public class NoImageException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7511655467882506948L;

	/**
	 * Instantiates a new no image exception.
	 *
	 * @param message the message
	 */
	public NoImageException(String message) {
		super(message);
	}
}
