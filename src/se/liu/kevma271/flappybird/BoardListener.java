package se.liu.kevma271.flappybird;

/**
 * The BoardListener interface defines methods that allow objects to listen for changes
 * in the game board of the Flappy Bird game.
 */
public interface BoardListener {

    /**
     * Called when the game board has changed.
     */
    void boardChanged();

    /**
     * Called when the type of the Flappy Bird has changed.
     */
    void birdChanged();
}
