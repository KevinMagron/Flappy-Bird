package se.liu.kevma271.flappybird.collisions;

import se.liu.kevma271.flappybird.PowerUp;
import se.liu.kevma271.flappybird.objects.Bird;
import se.liu.kevma271.flappybird.Board;
import se.liu.kevma271.flappybird.objects.Pipe;
import java.util.List;

/**
 * Implementation of the CollisionHandler interface for handling
 * collisions during the intangibility superpower, where collisions
 * only occur with the board floor.
 */
public class Intangibility implements Contract {

    /**
     * {@inheritDoc}
     * <p>
     * This implementation checks if the bird has touched the ground. If true, it returns true.
     * If the bird is above the board ceiling, it adjusts the bird's position to stay under the ceiling.
     * </p>
     *
     * @param pipes The list of pipes in the game.
     * @param board The game board containing the pipes.
     * @param bird  The bird object representing the player.
     * @return True if a collision is detected, false otherwise.
     */
    @Override
    public boolean hasCollision(List<Pipe> pipes, Board board, Bird bird) {
	// Check if bird has touched the ground
	if (bird.getPosY() >= board.getFloorPosY()) {
	    return true;
	// Check if it has touched the ceiling, if so, then move it down.
	} else if (bird.getPosY() <= board.getCeilingPosY()) {
		keepUnderCeiling(board, bird);
	}
	return false;
    }

    /**
     * Adjusts the bird's position to stay under the board ceiling
     * during the intangibility superpower. It also updates the bird's
     * speed so that it cannot accumilate speed.
     *
     * @param board The game board containing the pipes.
     */
    public void keepUnderCeiling(Board board, Bird bird) {
	final int counterMovement = 3;
	bird.setPosY(board.getCeilingPosY());
	bird.updateSpeed(counterMovement);
    }

    public PowerUp getDescription() {
	return PowerUp.SUPER;
    }
}
