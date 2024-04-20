package se.liu.kevma271.flappybird;

import se.liu.kevma271.flappybird.highscores.Highscore;
import se.liu.kevma271.flappybird.highscores.HighscoreList;
import se.liu.kevma271.flappybird.objects.Bird;
import se.liu.kevma271.flappybird.visuals.GameScreen;
import se.liu.kevma271.flappybird.visuals.LoadingScreen;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @author	Kevin Magron kevma271@student.liu.se
 * @since 	2024-03-08
 */

/**
 * The GameEngine class serves as the main controller for the Flappy Bird game,
 * managing the game loop, initialization, and user interactions.
 */
public class GameEngine {
    private static final int FRAMES_PER_SECOND = 1000 / 60;

    private Board board = null;
    private GameScreen gameScreen = null;
    private HighscoreList highScores = new HighscoreList();
    private Timer clockTimer = new Timer(FRAMES_PER_SECOND, null);

    /**
     * The main method that initializes and starts the Flappy Bird game.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        GameEngine game = new GameEngine();

        try {
            game.initializeGame();
        } catch (FileNotFoundException e) {
            ErrorHandler.handleFileNotFoundError(e);
        } catch (InterruptedException e) {
            ErrorHandler.handleThreadInterruptedError(e);
        } catch (IOException e) {
            ErrorHandler.handleHighscoresLoadingError(e);
        }
    }

    /**
     * Initializes the game by calling relevant methods. This method
     * is only called once, when the program is executed.
     *
     * @throws FileNotFoundException If an error occurs while
     * loading files.
     * @throws InterruptedException If an error occurs while
     * sleeping code.
     * @throws IOException If an error occurs during score saving.
     */
    @SuppressWarnings("CatchFallthrough")
    private void initializeGame() throws FileNotFoundException, InterruptedException, IOException {
        showLoadingScreen();
        loadHighScores();
        startGame();
    }

    /**
     * Display the loadingscreen for 4 seconds (4000 ms). This may
     * cause errors, which are caught here.
     *
     * @throws FileNotFoundException If an error occurs while
     * loading files.
     * @throws InterruptedException If an error occurs while
     * sleeping code.
     */
    private void showLoadingScreen() throws FileNotFoundException, InterruptedException {
	LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.showFrame();
        Thread.sleep(4000);
        loadingScreen.dispose();
    }

    /**
     * Load previous highscores that are stored locally on the
     * computer. If an error occurs than it is caught here and
     * handled using ErrorHandler class.
     *
     * @throws IOException If an error occurs during score saving.
     * @throws FileNotFoundException If an error occurs while
     * loading files.
     */
    private void loadHighScores() throws FileNotFoundException, IOException {
        List<Highscore> oldHighscores = highScores.getOldScores();
        highScores.setHighscores(oldHighscores);
    }

    /**
     * Starts the game by creating necessary instances and launching the
     * game loop.
     *
     * @throws FileNotFoundException If an error occurs while
     * loading files.
     */
    private void startGame() throws FileNotFoundException {
        // Create necassary instances and variables
        Bird bird = new Bird();
        board = new Board(bird);
        gameScreen = new GameScreen(board, bird);

        // Show the gamescreen and start the game loop
        gameScreen.showFrame();
        clockTimer = new Timer(FRAMES_PER_SECOND, null);
        clockTimer.setCoalesce(true);
        clockTimer.start();

        clockTimer.addActionListener(e -> {
	    try {
		updateGameState();
	    } catch (IOException ex) {
                ErrorHandler.handleSavingScoreError(ex);
  	    }
	});
    }

    /**
     * Performs one step of the game loop, updating the board and
     * checking for game over.
     *
     * @throws IOException If an error occurs during score saving.
     * @throws FileNotFoundException If an error occurs while
     * loading files.
     */
    private void updateGameState() throws IOException, FileNotFoundException {
        if (board.isRunning()) {
            board.tick();
        }
        if (board.isGameOver()) {
            handleGameOver();
        }
    }

    /**
     * Handles game over events by stopping the timer, saving the
     * highscore, and prompting the user to restart or exit.
     *
     * @throws IOException If an error occurs during score saving.
     * @throws FileNotFoundException If an error occurs while
     * loading files.
     */
    private void handleGameOver() throws IOException, FileNotFoundException {
        // Stop the timer and create a new highscore
        clockTimer.stop();
        Highscore highScore = new Highscore(board.getScore());

        if (highScores.isThereNewHighscore(highScore)) {
            String username = askForUsername();
            if (username != null && !username.isEmpty()) {
                highScore.setUsername(username);
                highScores.addHighscore(highScore);
            }
        }

        highScores.saveScoresAsJson();

        // Give the user an option to restart or exit when finished
        String[] options = { "Retry", "Chicken out"};
	int selection = JOptionPane.showOptionDialog(null, highScores.getHighscore(), "Magron Inc.", JOptionPane.YES_NO_OPTION,
                                                     JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (selection == 0) {
            gameScreen.dispose();
            restartGame();
        } else if (selection == 1) {
            System.exit(0);
        }
    }

    private String askForUsername() {
        return JOptionPane.showInputDialog("Please input your lame username");
    }

    private void restartGame() throws FileNotFoundException {
        gameScreen.dispose();
        startGame();
    }
}
