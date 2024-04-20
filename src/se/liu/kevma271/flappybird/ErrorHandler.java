package se.liu.kevma271.flappybird;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The ErrorHandler class provides methods for handling and logging errors in the Flappy Bird game.
 */
public class ErrorHandler {
    private static final String GAME_LOGS_FILE_PATH = "resources" + File.separator + "application.log";
    private static final Logger LOGGER = Logger.getLogger(ErrorHandler.class.getName());

    // Initialize the logger with file handler
    static {
	try {
	    FileHandler fileHandler = new FileHandler(GAME_LOGS_FILE_PATH);
	    LOGGER.addHandler(fileHandler);
	    SimpleFormatter formatter = new SimpleFormatter();
	    fileHandler.setFormatter(formatter);
	} catch (IOException e) {
	    LOGGER.severe("Could not create file to store logs: " + e.getMessage());
	    showErrorDialog("Could not create file to store logs.");
	}
    }

    public static void handleThreadInterruptedError(InterruptedException e) {
	LOGGER.severe("An error occurred while sleeping the thread: " + e.getMessage());
	showErrorDialog("Thread sleep interrupted.");
    }

    /**
     * Handles an error while loading highscores from JSON by logging the error,
     * displaying an error dialog, and throwing an IllegalStateException since
     * it is an error of high importance and needs to be handled immediatly.
     *
     * @param e The Exception object.
     */
    public static void handleHighscoresLoadingError(Exception e) {
	LOGGER.severe("An error occurred while loading scores from JSON: " + e.getMessage());
	showErrorDialog("An error occurred while loading scores from JSON.");
    }

    public static void handleFileNotFoundError(FileNotFoundException e) {
	LOGGER.severe("An error occured while loading file: " + e.getMessage());
	showErrorDialog("An error occured while loading file.");
    }

    public static void handleSavingScoreError(Exception ex) {
	LOGGER.severe("An error occurred during the game: " + ex.getMessage());
	showErrorDialog("An error occurred during the game: " + ex.getMessage());
    }

    private static void showErrorDialog(String message) {
	JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
