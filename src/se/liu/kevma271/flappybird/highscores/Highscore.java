package se.liu.kevma271.flappybird.highscores;

/**
 * The Highscore class represents a single high score in the Flappy Bird game.
 */
public class Highscore {
    private int score;
    private String username = "";

    /**
     * Constructs a Highscore object with the specified score value.
     *
     * @param score The score value for this high score.
     */
    public Highscore(int score) {
	this.score = score;
    }

    public int getScore() {
	return score;
    }
    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
    	this.username = username;
    }
}
