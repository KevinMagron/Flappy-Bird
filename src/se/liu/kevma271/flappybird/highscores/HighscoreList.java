package se.liu.kevma271.flappybird.highscores;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The HighscoreList class manages and manipulates a list of high scores in the Flappy Bird game.
 * It provides methods for adding new high scores, retrieving the current highest score,
 * and saving and loading high scores to and from a JSON file.
 */
public class HighscoreList {
    /**
     * The file path for the root of the user folder.
     */
    private static final String FILE_PATH = "resources" + File.separator + "halloffame.json";

    private Highscore highscore = null;

    private List<Highscore> highscores = new ArrayList<>();

    /**
     * Checks if given highscore is addable to the highscore-list.
     *
     * @param highscore The given highscore to check.
     * @return Returns true if it is the highest score, otherwise false.
     */
    public boolean isThereNewHighscore(Highscore highscore) {
	if (highscores.isEmpty()) {
	    return true;
	} else if (highscores.getFirst().getScore() < highscore.getScore()) {
	    return true;
	}
	this.highscore = highscores.getFirst();
	return false;
    }

    /**
     * Adds a new high score to the list.
     * If the list is not empty than the first highscore is removed.
     * Then the new highscore is added to the highscore-list.
     *
     * @param highscore The highscore to be added.
     */
    public void addHighscore(Highscore highscore) {
	if (!highscores.isEmpty()) {
	    highscores.remove(0);
	}
	this.highscore = highscore;
	highscores.add(highscore);
    }

    public String getHighscore() {
	return "HOF: " + highscore.getScore() + "       " + highscore.getUsername();
    }

    public void setHighscores(final List<Highscore> highscores) {
	this.highscores = highscores;
    }


    /**
     * Retrieves old high scores from a JSON file.
     *
     * @return The list of old highscores.
     * @throws FileNotFoundException If the file is not found.
     * @throws IOException           If an I/O error occurs.
     */
    public List<Highscore> getOldScores() throws FileNotFoundException, IOException {
	Gson gson = new Gson();

	final BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
	List<Highscore> oldHighscores = gson.fromJson(reader, new TypeToken<ArrayList<Highscore>>()
	{
	}.getType());

	return oldHighscores != null ? oldHighscores : new ArrayList<>();
    }

    /**
     * Saves the current highscores to a JSON file.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void saveScoresAsJson() throws IOException {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	String highscoresJson = gson.toJson(highscores);

	final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_PATH));
	bufferedWriter.write(highscoresJson);
	bufferedWriter.flush();
    }
}
