package entity.base;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import app.Main;
import gui.ControlBar;
import gui.Global;
import gui.SimulationArea;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import utils.Sound;
import utils.Vector;

/**
 * The Class Ant. An ant can exist, move, make money, and die.
 */
public class Ant implements Renderable, Restartable {

	/**
	 * This enumerator tells the specific kind of the ant being created.
	 */
	public enum AntType {

		/** Indicates a fire ant. */
		FIRE,
		/** Indicates a flash ant. */
		FLASH
	}

	/** The vector representation of the ant's velocity. */
	protected Vector velocity;

	/** The vector representation of the ant's position. */
	protected Vector position;

	/** The last seen position vector of a candy. */
	protected Vector lastSeenPosition;

	/** The magnitude of the ant's velocity. */
	protected double speed;

	/** The image representation of the ant. */
	protected ImageView img;

	/** The find food thread. */
	protected Thread settingOffJourneyThread;

	/**
	 * The steps that the ant has moved, i.e. the number of iterations that the
	 * ant's findFoodThread
	 */
	protected int steps;

	/** This tells if a piece of candy is within the ant's sight or not. */
	protected boolean hasFoundCandy;

	/** This tells if the food is within the ant's reach. */
	protected boolean hasReachedCandy;

	/** The candy that the ant's going to grab or currently bringing home. */
	protected Candy foundCandy;

	/** The number of candies that the ant has brought home so far. */
	protected int broughtHomeCount;

	/**
	 * A fire ant reproduces if broughtHomeCount is a multiple of this value, i.e.
	 * broughtHomeCount % multipleReproduce = 0
	 */
	protected int multipleReproduce;

	/**
	 * The angle in degree that the ant can see across. The higher of this value,
	 * the wider the ant can see.
	 */
	protected double visionSpan;

	/**
	 * The depth in pixel that the ant can see. The higher of this value, the longer
	 * distance the ant can see.
	 */
	protected double visionDepth;

	/**
	 * After the ant bringing candy home, the player gets the amount of money of
	 * moneyMultiplier * the cost of a piece of candy.
	 */
	protected double moneyMultiplier;

	/** A media player instance that plays when the ant is created. */
	protected MediaPlayer introPlayer;

	/** A media player instance that plays when the ant is removed. */
	protected MediaPlayer outroPlayer;

	/** Tells if the ant is heading back to the last seen position or not. */
	private boolean isHeadingBackToLastSeenPosision;

	/** Tells if the ant is heading to home or not. */
	private boolean isHeadingToHome;

	/**
	 * This is used to generate random numbers, mainly in randomizing the velocity
	 * vector direction.
	 */
	protected static final Random random = new Random();

	/** Instantiates a new default ant. */
	public Ant() {
		this.setupCommon(1d);
		this.visionSpan = 90d;
		this.visionDepth = 100d;
		this.moneyMultiplier = 1.5;
		this.setupImage("ant.png");
		this.outroPlayer = Sound.getMediaPlayer("oof.wav", .5);
	}

	/**
	 * Constructor for flash ant to call as super( ... ).
	 */
	public Ant(int speedMultiplier, int visionSpan, int visionDepth, double moneyMultiplier, String imageName,
			String dyingSoundFileNameString, double dyingSoundVolume) {
		this.setupCommon(speedMultiplier);
		this.visionSpan = visionSpan;
		this.visionDepth = visionDepth;
		this.moneyMultiplier = moneyMultiplier;
		this.setupImage(imageName);
		this.outroPlayer = Sound.getMediaPlayer(dyingSoundFileNameString, dyingSoundVolume);
	}

	/**
	 * Sets up common values between different kinds of ants.
	 */
	private void setupCommon(double speedMultiplier) {
		this.revive();
		this.steps = 0;
		this.broughtHomeCount = 0;
		this.multipleReproduce = 10;
		this.position = new Vector(SimulationArea.origin);
		this.speed = 1d;
		this.speed *= speedMultiplier;
		this.velocity = Vector.createVector2FromAngle(random.nextDouble() * 360, this.speed);
		this.settingOffJourneyThread = new Thread(() -> this.start());
	}

	/**
	 * Resets all states to the default values.
	 */
	private void revive() {
		this.isHeadingBackToLastSeenPosision = false;
		this.isHeadingToHome = false;
		this.lastSeenPosition = null;
		this.hasFoundCandy = false;
		this.hasReachedCandy = false;
		this.foundCandy = null;
	}

	/**
	 * Sets the up image.
	 *
	 * @param imageName the image file name
	 */
	protected void setupImage(String imageName) {
		this.img = new ImageView(ClassLoader.getSystemResource(imageName).toExternalForm());
		final var antHeight = 15;
		this.img.setFitHeight(antHeight);
		this.img.setPreserveRatio(true);
	}

	/**
	 * Starts the ant's life long journey consisting of moving, looking for candy,
	 * bringing it home, repeat, or eating a poisonous candy and die.
	 */
	private void start() {
		while (true) {
			try {
				Thread.sleep(10);
				this.move();

				if (!this.hasFoundCandy) {
					final var candyOnSight = this.lookForCandy();
					if (candyOnSight != null) {
						this.targetTheCandy(candyOnSight);
					}

					if (this.lastSeenPosition != null && this.hasReachedLastSeenPosition()) {
						this.isHeadingBackToLastSeenPosision = false;
					}
				} else {
					if (this.isCandyReachable()) {
						this.hasReachedCandy = true;
						this.foundCandy.setPosition(this.position);
						if (!this.isHeadingToHome)
							// check this to avoid unnecessary vector re-creation
							this.headToHome();

						if (this.foundCandy instanceof Poisonable)
							((Poisonable) this.foundCandy).poison(this);

						if (this.hasReachedHome()) {
							this.deliverCandy();
							this.isHeadingToHome = false;
							this.headToLastSeenPosition();
						}
					}
				}

			} catch (InterruptedException e) {
				final var simulationArea = Main.getSimulationArea();
				Platform.runLater(() -> simulationArea.removeImage(this.img));
				break;
			}
		}
	}

	/**
	 * Targets the candy, heads to its position, and remember the area to come back
	 * later.
	 *
	 * @param candyOnSight the candy on sight
	 */
	private void targetTheCandy(Candy candyOnSight) {
		this.isHeadingBackToLastSeenPosision = false;
		this.foundCandy = candyOnSight;
		this.hasFoundCandy = true;
		this.headToCandy();
		this.lastSeenPosition = new Vector(this.foundCandy.position); // will move back here again
	}

	/**
	 * Checks if the ant has reached the last seen position.
	 *
	 * @return true, if the ant is within a range of the last seen position, else
	 *         false
	 */
	private boolean hasReachedLastSeenPosition() {
		final double range = 2;
		final double displacement = Math.sqrt(Math.pow(this.lastSeenPosition.getX() - this.position.getX(), 2)
				+ Math.pow(this.lastSeenPosition.getY() - this.position.getY(), 2));
		return displacement <= range;
	}

	/**
	 * Changes the ant's velocity direction to point to the last seen position.
	 */
	private void headToLastSeenPosition() {
		this.isHeadingBackToLastSeenPosision = true;
		final var antToLastSeenPostion = new Vector(this.lastSeenPosition.getX() - this.position.getX(),
				this.lastSeenPosition.getY() - this.position.getY(), 0);
		this.velocity = Vector.createVector2FromAngle(antToLastSeenPostion.getAngle(), this.speed);
	}

	/**
	 * Delivers candy. This removes the existence of a recently brought candy and
	 * update status values.
	 */
	private void deliverCandy() {
		final var simulationArea = Main.getSimulationArea();
		Platform.runLater(() -> {
			final ImageView foundFoodImage = this.foundCandy.getImg();
			foundFoodImage.setVisible(false);
			simulationArea.removeImage(foundFoodImage);
			simulationArea.getCandies().remove(this.foundCandy);
			this.hasFoundCandy = false;
			this.hasReachedCandy = false;
		});

		++this.broughtHomeCount;
		final var controlBar = Main.getControlBar();
		controlBar.setBroughtHomeCandyCount(controlBar.getBroughtHomeCandyCount() + 1);
		controlBar.setMoney(controlBar.getMoney() + controlBar.getFoodCost() * this.moneyMultiplier);
		controlBar.rerender();

		this.reproduce(simulationArea, controlBar);
	}

	/**
	 * When a fire ant is ready, it gives birth to a new fire ant and has a 30%
	 * chance of giving birth to a flash ant.
	 *
	 * @param simulationArea the simulation area
	 * @param controlBar     the control bar
	 */
	protected void reproduce(SimulationArea simulationArea, ControlBar controlBar) {
		if (controlBar.hasReachedMaxPopulation()) {
			// more than this is no longer safe
			return;
		}

		if (this.broughtHomeCount % this.multipleReproduce == 0) {
			// get some food to reproduce
			simulationArea.addAnt(AntType.FIRE);
			this.multipleReproduce += 10; // require more food next time, so that it is harder to reproduce

			if (Ant.random.nextDouble() < 0.3)
				// chance to get flash ant
				simulationArea.addAnt(AntType.FLASH);
		}
	}

	/**
	 * Checks for reached home.
	 *
	 * @return true, if the ant's position is within 2 pixel range from home, else
	 *         false.
	 */
	private boolean hasReachedHome() {
		final double range = 2;
		return this.getAntToHomeVector().modulus() < range;
	}

	/**
	 * Replaces the ant's velocity with a new velocity whose only difference is the
	 * direction that now points to home.
	 */
	private void headToHome() {
		final var antToHome = this.getAntToHomeVector();
		this.velocity = Vector.createVector2FromAngle(antToHome.getAngle(), this.speed);
	}

	/**
	 * Returns the vector starting from the ant and ending at home.
	 *
	 * @return the ant-to-home vector
	 */
	private Vector getAntToHomeVector() {
		return new Vector(SimulationArea.origin.getX() - this.position.getX(),
				SimulationArea.origin.getY() - this.position.getY(), 0d);
	}

	/**
	 * Checks if is candy reachable.
	 *
	 * @return true, if the found candy is reachable, i.e. within 5 pixel range,
	 *         else false.
	 */
	private boolean isCandyReachable() {
		// return true if the food is now within a small range from the ant
		final double range = 5;
		return this.getAntToCandyVector().modulus() <= range;
	}

	/**
	 * Replaces the ant's velocity with a new velocity whose only difference is the
	 * direction that now points to the found candy.
	 */
	private void headToCandy() {
		// make the velocity point to the food position
		final var antToFood = this.getAntToCandyVector();
		this.velocity = Vector.createVector2FromAngle(antToFood.getAngle(), this.speed);
	}

	/**
	 * Returns the vector starting from the ant and the found candy.
	 *
	 * @return the ant to food vector
	 */
	private Vector getAntToCandyVector() {
		final Vector foodPos = this.foundCandy.getPosition();
		return new Vector(foodPos.getX() - this.position.getX(), foodPos.getY() - this.position.getY(), 0d);
	}

	/**
	 * Returns the candy in the candy array that is first met the conditions that
	 * are the candy is within the ant's vision depth and within the ant's vision
	 * span ant the candy hasn't been found by another ant.
	 *
	 * @return the found candy, if the conditions are met, else null.
	 */
	private Candy lookForCandy() {
		// return the first food in the array that is on sight
		// return null if there's no food on sight

		final var lower = Vector.createVector2FromAngle(this.velocity.getAngle() - this.visionSpan / 2,
				this.visionDepth);
		final var upper = Vector.createVector2FromAngle(lower.getAngle() + this.visionSpan, this.visionDepth);

		final CopyOnWriteArrayList<Candy> foods = (CopyOnWriteArrayList<Candy>) Main.getSimulationArea().getCandies();
		for (final Candy food : foods) {
			if (food.isFound()) // this food currently belongs to another ant
				continue;

			final Vector foodPos = food.getPosition();
			final var antToFood = new Vector(foodPos.getX() - this.position.getX(),
					foodPos.getY() - this.position.getY(), 0d);

			final boolean isWithinRange = antToFood.modulus() <= upper.modulus();
			final boolean isWithinSpan = upper.getAngle() >= this.velocity.getAngle()
					&& this.velocity.getAngle() >= lower.getAngle();

			if (isWithinRange && isWithinSpan) {
				food.setFound(true); // to tell other ants not to mess with this food
				return food;
			}
		}
		return null;
	}

	/**
	 * Updates the ant's position vector and the number of steps.
	 */
	private void updatePosition() {
		// changes position
		this.position.setX(this.position.getX() + this.velocity.getX());
		this.position.setY(this.position.getY() + this.velocity.getY());
		this.steps++;
	}

	/**
	 * Randomize the direction of the ant's velocity for the next step.
	 */
	protected void randomizeDirection() {
		if (this.isHeadingBackToLastSeenPosision)
			return;

		if (!hasFoundCandy) {
			final double span = 20;
			final double angleOffset = random.nextDouble() * span - span / 2;
			this.velocity = Vector.createVector2FromAngle(this.velocity.getAngle() + angleOffset, this.speed);
		}
	}

	/**
	 * Bounces the ant off the window screen.
	 * 
	 * @return true, if the ant bounces off the wall, else false
	 */
	private boolean bounceWallIfNecessary() {
		var hasBounced = false;

		if (this.position.getX() < 0) {
			this.velocity.reverseX();
			this.position.setX(0);
			hasBounced = true;
		} else if (this.position.getX() > Global.WIDTH) {
			this.velocity.reverseX();
			this.position.setX(Global.WIDTH);
			hasBounced = true;
		}
		if (this.position.getY() > 0) {
			this.velocity.reverseY();
			this.position.setY(0);
			hasBounced = true;
		} else if (this.position.getY() < -SimulationArea.height) {
			this.velocity.reverseY();
			this.position.setY(-SimulationArea.height);
			hasBounced = true;
		}

		return hasBounced;
	}

	/**
	 * Updates the ant's overall movement.
	 */
	private void update() {
		this.updatePosition();
		this.randomizeDirection();
		if (this.bounceWallIfNecessary())
			// When the game lags, some of the ants no longer function, so we have to revive
			// them. And when an ant hit a wall, it's very likely that it hasn't found a
			// candy, so it's safe to do so.
			this.revive();
	}

	/**
	 * Renders the ant on the pane with updated position.
	 */
	@Override
	public void rerender() {
		final var simulationArea = Main.getSimulationArea();
		Platform.runLater(() -> {
			simulationArea.removeImage(this.img);
			this.img.relocate(this.position.getX(), -this.position.getY()); // window position
			this.img.setRotate(90 - this.velocity.getAngle());
			simulationArea.addImage(this.img);
		});
	}

	/**
	 * Updates the ant's position and re-render, and if it has found a candy,
	 * re-render the candy as well.
	 */
	private void move() {
		update();
		rerender();
		if (this.hasReachedCandy)
			this.foundCandy.rerender();
	}

	/**
	 * Gets the ant's journey thread.
	 *
	 * @return the find food thread
	 */
	public Thread getSettingOffJourneyThread() {
		return settingOffJourneyThread;
	}

	/**
	 * Gets the image of the ant.
	 *
	 * @return the ant's image
	 */
	@Override
	public ImageView getImg() {
		return this.img;
	}

	/**
	 * Sets the ant's image.
	 *
	 * @param img the new image
	 */
	@Override
	public void setImg(ImageView img) {
		this.img = img;
	}

	/**
	 * Does nothing because there's no introduction sound for normal ant.
	 */
	@Override
	public void playIntroSound() {
		// no introduction sound for normal ant
	}

	/**
	 * Plays a sound when the ant dies.
	 */
	@Override
	public void playOutroSound() {
		// play when it dies, just ate poison food
		outroPlayer.play();
	}
}
