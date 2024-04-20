package se.liu.kevma271.flappybird.visuals;

import se.liu.kevma271.flappybird.Board;
import se.liu.kevma271.flappybird.BoardListener;
import se.liu.kevma271.flappybird.Direction;
import se.liu.kevma271.flappybird.PowerUp;
import se.liu.kevma271.flappybird.objects.Bird;
import se.liu.kevma271.flappybird.objects.Pipe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.EnumMap;
import java.util.Map;

/**
 * The GameViewer class represents the graphical user interface for
 * displaying the Flappy Bird game.
 * It extends JPanel and implements the BoardListener interface to
 * handle updates and user input.
 */
public class GameScreen extends Handler implements BoardListener {
    private Board board;
    private Bird bird;

    private Map<GameImages, Image> imageMap = new EnumMap<>(GameImages.class);

    /**
     * Constructs a GameViewer object with a specified game board and
     * bird.
     *
     * @param board The game board.
     * @param bird  The player-controlled bird.
     */
    public GameScreen(Board board, Bird bird) throws FileNotFoundException {
	// Initialize instances and frame
	this.board = board;
	this.bird = bird;

	setFrameSettings();
	loadImages();
	board.addBoardListener(this);
    }

    public void setFrameSettings() {
	frame = new JFrame("Flappy bird");
	setPreferredSize(new Dimension(board.getWidth(), board.getHeight()));
	frame.setSize(board.getWidth(), board.getHeight());
	frame.setLocationRelativeTo(null);
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.add(this);
	frame.pack();

	setKeyBindings();
    }

    public void loadImages() throws FileNotFoundException {
	imageMap.put(GameImages.BACKGROUND, new ImageIcon(ClassLoader.getSystemResource("images/flappybirdbg.png")).getImage());
	imageMap.put(GameImages.FLOOR, new ImageIcon(ClassLoader.getSystemResource("images/flappybirdground.png")).getImage());
	imageMap.put(GameImages.REGULAR_FLAPPY, new ImageIcon(ClassLoader.getSystemResource("images/flappybird.png")).getImage());
	imageMap.put(GameImages.SUPER_FLAPPY, new ImageIcon(ClassLoader.getSystemResource("images/blueflappybird.png")).getImage());
	imageMap.put(GameImages.GRAVITY_FLAPPY, new ImageIcon(ClassLoader.getSystemResource("images/redflappybird.png")).getImage());
	imageMap.put(GameImages.FLAPPY, imageMap.get(GameImages.SUPER_FLAPPY)); // Assigning flappy to super to begin with
	imageMap.put(GameImages.TOP_PIPE, new ImageIcon(ClassLoader.getSystemResource("images/toppipe.png")).getImage());
	imageMap.put(GameImages.BOTTOM_PIPE, new ImageIcon(ClassLoader.getSystemResource("images/bottompipe.png")).getImage());

	validateImageMap();
   }

    /**
     * Iterates through all images to see if any of them are null.
     *
     * @throws FileNotFoundException If an image is not found.
     */
    private void validateImageMap() throws FileNotFoundException {
	for (Map.Entry<GameImages, Image> entry : imageMap.entrySet()) {
	    if (entry.getValue() == null) {
		throw new FileNotFoundException("Image resource not found: " + entry.getKey());
	    }
	}
    }

    /**
     *     The method maps relevant keystrokes to respective action.
     *     Sets the different keybindings in the game.
     */
    private void setKeyBindings() {
	JComponent pane = frame.getRootPane();

	final InputMap inputs = pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	inputs.put(KeyStroke.getKeyStroke("SPACE"), "moveUp");
	inputs.put(KeyStroke.getKeyStroke("UP"), "moveUp");
	inputs.put(KeyStroke.getKeyStroke("W"), "moveUp");

	final ActionMap actions = pane.getActionMap();
	actions.put("moveUp", new MoveAction(Direction.UP));
    }

    public void boardChanged() {
	repaint();
    }

    /**
     * Sets the type of Flappy Bird image based on the specified bird
     * type. Since there oly exists 3 different PowerUp values in the
     * enum class, a default case is not needed.
     *
     * @param birdType The type of Flappy Bird ("regular," "super," or
     *                 "gravity").
     */
    public void birdChanged() {
	imageMap.remove(GameImages.FLAPPY);
	PowerUp birdType = bird.getType();

	switch (birdType) {
	    case REGULAR:
		imageMap.put(GameImages.FLAPPY, imageMap.get(GameImages.REGULAR_FLAPPY));
		break;
	    case SUPER:
		imageMap.put(GameImages.FLAPPY, imageMap.get(GameImages.SUPER_FLAPPY));
		break;
	    case GRAVITY:
		imageMap.put(GameImages.FLAPPY, imageMap.get(GameImages.GRAVITY_FLAPPY));
		break;
	}
    }

    /**
     * Draws the game elements on the panel, including pipes, ground, Flappy Bird, and the score.
     *
     * @param g The graphics context used for drawing.
     */
    public void draw(Graphics g) {
	// Create variables
	final Font font = new Font(" Serif", Font.PLAIN, 42);
	final int groundYPosition = 575;
	final int groundWidth = 360;
	final int groundHeight = 112;
	final int wordMargin = 18;
	final int scoreXPos = board.getWidth() / 2 - wordMargin;
	final int scoreYPos = board.getHeight() / 6;

	g.drawImage(imageMap.get(GameImages.BACKGROUND), 0, 0, board.getWidth(), board.getHeight(), null);

	for (Pipe pipe : board.getPipes()) {
	    g.drawImage(imageMap.get(GameImages.TOP_PIPE), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
	    g.drawImage(imageMap.get(GameImages.BOTTOM_PIPE), pipe.getPosX(), pipe.getPosY() + pipe.getHeight() + pipe.getGap(), pipe.getWidth(), pipe.getHeight(), null);
	}

 	g.drawImage(imageMap.get(GameImages.FLOOR), 0, groundYPosition, groundWidth, groundHeight, null);
	g.drawImage(imageMap.get(GameImages.FLAPPY), bird.getPosX(), bird.getPosY(), bird.getWidth(), bird.getHeight(), null);
	g.setFont(font);
	g.drawString(String.valueOf(board.getScore()), scoreXPos, scoreYPos);
    }

    /**
     * Represents an action for handling player movement based on keyboard input.
     */
    private class MoveAction extends AbstractAction {

	/**
	 * The direction of the movement.
	 */
	private Direction moveDirection;

	/**
	 * Constructs a MoveAction with the specified movement direction.
	 *
	 * @param moveDirection The direction of the movement.
	 */
	private MoveAction(Direction moveDirection) {
	    this.moveDirection = moveDirection;
	}

	/**
	 * Clones the MoveAction instance.
	 *
	 */
	@Override public void actionPerformed(final ActionEvent e) {
	    board.moveBird(moveDirection);
	}
    }
}
