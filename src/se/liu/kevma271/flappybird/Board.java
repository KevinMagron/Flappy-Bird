package se.liu.kevma271.flappybird;

import se.liu.kevma271.flappybird.collisions.Contract;
import se.liu.kevma271.flappybird.collisions.DefaultCollisions;
import se.liu.kevma271.flappybird.collisions.Intangibility;
import se.liu.kevma271.flappybird.collisions.UpperPipeIntangibility;
import se.liu.kevma271.flappybird.objects.Bird;
import se.liu.kevma271.flappybird.objects.Pipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game board in the Flappy Bird game.
 * <p>
 * 	The game board includes various elements such as pipes and the
 * 	bird. It is responsible for making the game continue by calling
 * 	relevant methods and creating relevant instances of classes. It
 * 	is responsible for all mechanics.
 * </p>
 */
public class Board {
    private static final int WIDTH = 360;
    private static final int HEIGHT = 640;

    /**
     * List of listeners to be notified when the game board changes.
     */
    private List<BoardListener> boardListeners = new ArrayList<>();
    private List<Pipe> pipes = new ArrayList<>();
    private Contract collisionHandler = new DefaultCollisions();

    /**
     * Counter for how long given superpower should be active.
     */
    private int superPowerTicker = 0;

    /**
     * Counter for how long invicibility should be activated.
     */
    private int invincibilityTicker = 0;

    private Pipe pipe = null;
    private Bird bird;

    private static final int DEFAULT_GRAVITY_STRENGTH = 1;
    private double gravity = 1;
    private int score = 0;
    private boolean gameOver = false;
    private boolean running = false;
    private boolean superPowersDisabled = true;

    public Board(Bird bird) {
	// Initialize the bird
	this.bird = bird;
	bird.setPosX(WIDTH / 8);
	bird.updatePosY(HEIGHT / 2);

	// Create the first pipes
	createPipes();
    }

    /**
     * Calls other methods every tick of the game so the game continues
     */
    public void tick() {
	// Update speed of the bird
	bird.updateSpeed(gravity);
	bird.updatePosY((int) bird.getSpeed());

	// Call methods to continue game
	areThereCollisions();
	createSuperPowersRandomly();
	updateSuperPowerCounters();
	updatePipes();
	updateScore();
	notifyListenersBoardChanged();
    }

    /**
     * Checks if any collisions have happened
     */
    private void areThereCollisions() {
	// Check for collisions
    if (collisionHandler.hasCollision(pipes, this, bird)) {
		gameOver = true;
	}
    }

    /**
     * Creates superpowers randomly using formula.
     * <p>
     *     On each tick, a random number is generated. If the random
     *     number falls within certain intervals, a corresponding
     *     power-up is created. Unless another power-up is already
     *     activared. Each power-up activates a timer to determine
     *     its duration.
     * </p>
     */
    private void createSuperPowersRandomly() {
	final int randomRange = 2500;
	final int birdSuperThreshHold = 1;
	final int gravitySuperThreshHold = 2495;

	int randomNumber = (int) (randomRange * Math.random());
	if (randomNumber < birdSuperThreshHold && superPowersDisabled) {
	    setBirdSuper();
	} else if (randomNumber > gravitySuperThreshHold && superPowersDisabled) {
	    setGravitySuper();
	}
    }

    /**
     * Checks if any superpowers are active and handles their tickers correctly.
     * <p>
     *     This method updates the timers for active superpowers an
     *     checks if they have reached zero.
     *     Upon reaching zero, the method creates a post-superpower
     *     shield that lasts for a duration.
     *     The method also updates the ticker for the shield duration.
     * </p>
     */
    private void updateSuperPowerCounters() {
	// Check if superpower is activated
	 if (superPowerTicker > 0) {
	     addScoreDuringSuperPower();
	     superPowerTicker -= 1;
	     if (superPowerTicker == 0) {
		 createPostPowerShield();
	     }
	}
	 // Check if post-superpower shield is activated
	 else if (invincibilityTicker > 0) {
		invincibilityTicker -= 1;
	 }
	 // If everything is as normal
	 else if (invincibilityTicker == 0) {
	     makeGameNormal();
	 }
    }

    /**
     * Sets objects back to default settings after a superpower.
     */
    private void makeGameNormal() {
	superPowersDisabled = true;
	collisionHandler = new DefaultCollisions();
	bird.setType(collisionHandler.getDescription());
	notifyListenersSuperPower();
    }

    /**
     * Creates a post-superpower shield.
     * <p>
     *     Sets the collisionhandler to intangibility to momentairly turn
     *     collisions. The method creates a ticker that will revert the
     *     collisionhandler, pipespeed and gravity to default once it '
     *     has reached zero.
     * </p>
     */
    private void createPostPowerShield() {
	// Invincibility will be active for 55 ticks
	final int invincibilityTime = 55;

	collisionHandler = new Intangibility();
	bird.setType(collisionHandler.getDescription());
	notifyListenersSuperPower();
	invincibilityTicker = invincibilityTime;
	pipe.setSpeed(pipe.getDefaultSpeed());
	gravity = DEFAULT_GRAVITY_STRENGTH;
    }

    /**
     * Handles scores during superpowers.
     * <p>
     *     Since the default score handling counts how many pipes one has
     *     passed, it would be unbalanced with superpowers. This method
     *     adds to the score every second instead.
     * </p>
     */
    private void addScoreDuringSuperPower() {
	final int framesPerSecond = 60;
	final int scoreAddInterval = framesPerSecond;

	if (superPowerTicker % scoreAddInterval == 0 && Math.abs(pipe.getSpeed() - pipe.getDefaultSpeed()) > 1) {
	    score += 1;
	}
    }

    /**
     * Handles the creation of the superpower.
     * <p>
     *     Activates the ticker for a randomly generated period within a
     *     minimum and maximum time. It increaces the speed of the pipes
     *     drastically and sets the collisionhandler to intangibility
     *     to minimize collisions. It also changes the color of the bird
     *     to blue.
     * </p>
     */
    private void setBirdSuper() {
	// Min and max ticks for intangibility
	final int minIntangibilityTime = 200;
	final int maxIntangibilityTime = 400;

	collisionHandler = new Intangibility();
	bird.setType(collisionHandler.getDescription());
	superPowerTicker = (int) (minIntangibilityTime + maxIntangibilityTime * Math.random());
	pipe.setSpeed(pipe.getIntangibilitySpeed());
	superPowersDisabled = false;
	notifyListenersSuperPower();
    }

    /**
     * Activates the gravity power-up.
     * <p>
     *     Activates the ticker for a randomly generated period within a
     *     minimum and maximum time as before. It then increaces the
     *     gravity with 50% and sets the bird's color to red.
     * </p>
     */
    private void setGravitySuper() {
	// Min and max ticks for increaced gravity
	final int minGravityTime = 100;
	final int maxGravityTime = 200;

	superPowersDisabled = false;
	collisionHandler = new UpperPipeIntangibility();
	bird.setType(collisionHandler.getDescription());
	superPowerTicker = (int) (minGravityTime + maxGravityTime * Math.random());
	gravity = DEFAULT_GRAVITY_STRENGTH * 1.5;
	notifyListenersSuperPower();
    }

    /**
     * Creates a new pipe instance and adds it to the pipe list.
     */
    private void createPipes() {
	final int defaultPipeSpeed = -5;
	double pipeSpeed = defaultPipeSpeed;
	// Check if we have created pipes before
	if (pipe != null) {
	    pipeSpeed = pipe.getSpeed();
	}

	Pipe pipe = new Pipe();
	pipe.setSpeed(pipeSpeed);
	pipes.add(pipe);
	this.pipe = pipe;
	pipe.updatePosX(WIDTH);
	pipe.setPosY(pipe.getRandomHeight());
    }

    /**
     * Handles the user inputs for movement of the bird.
     *
     * @param direction The direction that the player wants the bird to
     *                  move.
     */
    public void moveBird(Direction direction) {
	running = true;

	if (direction == Direction.UP) {
	    final int upwardMovement = -13;
	    bird.updateSpeed(upwardMovement);
	}
    }

    /**
     * Checks if pipes have to be removed.
     * <p>
     *     Tt checks if the last created couple of pipes have
     *     enough margin to the right that new pipes can be created.
     *     It removes pipes that have gone outside of the frame
     *     afterwards.
     * </p>
     */
    private void updatePipes() {
	final int lastCreatedPipeX = pipes.getLast().getPosX();
	final int mostLeftestPipeX = pipes.getFirst().getPosX();

	if (lastCreatedPipeX < pipe.getMargin()) {
	    createPipes();
	    // Check if leftest pipe is outside the board
	    final int boardLeftFrame = 0;
	    if (mostLeftestPipeX < boardLeftFrame) {
		pipes.removeFirst(); 
	    }
	}
	movePipes();
    }

    /**
     * Changes the x-position of all pipes to make them move left.
     */
    public void movePipes() {
	for (Pipe pipe : pipes) {
	    pipe.updatePosX((int) pipe.getSpeed());
	}
    }

    /**
     * Checks if the bird is between any pipes and adds to the score
     * if it is.
     */
    private void updateScore() {
	// Check if pipes are inside x-interval of the
	// bird and if the superpower isn't active
	for (Pipe pipe : pipes) {
	    if (pipe.getPosX() <= bird.getPosX()) {
		if (pipe.getPosX() >= bird.getPosX()) {
		    if ((int) pipe.getSpeed() == pipe.getDefaultSpeed())
			score += 1;
		}
	    }
	}
    }

    /**
     * Notifies all boardlisteners that the board has changed.
     */
    public void notifyListenersBoardChanged() {
	for (BoardListener boardListener : boardListeners) {
	    boardListener.boardChanged();
	}
    }

    /**
     * Notifies all boardlisteners that a superpower is activated.
     *
     * @param birdType Indicates the color that the bird should be
     *                 painted.
     */
    public void notifyListenersSuperPower() {
	for (BoardListener boardListener : boardListeners) {
	    boardListener.birdChanged();
	}
    }

    public void addBoardListener(BoardListener boardListener) {
	boardListeners.add(boardListener);
    }

    public boolean isRunning() {
	return running;
    }
    public boolean isGameOver() {
	return gameOver;
    }
    public List<Pipe> getPipes() {
	return pipes;
    }
    public int getCeilingPosY() {
	final int boardCeiling = 0;
	return boardCeiling;
    }
    public int getFloorPosY() {
	final int floorWidth = 100;
	final int floor = HEIGHT - floorWidth;
	return floor;
    }
    public int getWidth() {
	return WIDTH;
    }
    public int getHeight() {
	return HEIGHT;
    }
    public int getScore() {
	return score;
    }
}
