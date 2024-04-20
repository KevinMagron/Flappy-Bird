package se.liu.kevma271.flappybird.collisions;

import se.liu.kevma271.flappybird.PowerUp;
import se.liu.kevma271.flappybird.objects.Bird;
import se.liu.kevma271.flappybird.Board;
import se.liu.kevma271.flappybird.objects.Pipe;
import java.util.List;

/**
 * Interface for handling collisions between pipes, the game board and the bird.
 * Implementing classes should include own implmenantations of these methods.
 */
public interface Contract {

    /**
     * Checks if there is a collision between the bird and pipes on the game board.
     *
     * @param pipes The list of pipes in the game.
     * @param board The game board containing the pipes.
     * @param bird  The bird object representing the player.
     * @return True if a collision is detected, false otherwise.
     */
    boolean hasCollision(List<Pipe> pipes, Board board, Bird bird);

    /**
     * Returns the type of cillision.
     */
    public PowerUp getDescription();
}
