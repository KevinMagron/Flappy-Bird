package se.liu.kevma271.flappybird.collisions;

import se.liu.kevma271.flappybird.PowerUp;
import se.liu.kevma271.flappybird.objects.Bird;
import se.liu.kevma271.flappybird.Board;
import se.liu.kevma271.flappybird.objects.Pipe;
import java.util.List;

/**
 * Implementation of the CollisionHandler interface for handling
 * collisions between pipes, the game board, and the bird.
 */
public class DefaultCollisions implements Contract {

    /**
     * {@inheritDoc}
     * <p>
     * This implementation checks for collisions based on the default
     * strategy. It checks if the bird has touched the ground or ceiling
     * and if it has collided with any pipes on the game board.
     * </p>
     */
    public boolean hasCollision(List<Pipe> pipes, Board board, Bird bird) {
	// Check if bird has touched the ground or ceiling
	if (bird.getPosY() <= board.getCeilingPosY() || bird.getPosY() >= board.getFloorPosY())
	    return true;

	// Check if the pipes are within the x poistions of the bird
	for (Pipe pipe : pipes) {
	    if (bird.getPosX() - pipe.getWidth() <= pipe.getPosX() && pipe.getPosX() <= bird.getPosX() + bird.getWidth()) {

		int topPipeEnd = pipe.getPosY() + pipe.getHeight();
		int bottomPipeStart = topPipeEnd+pipe.getGap() - pipe.getMushroom();

		// Check if the top and bottom pipe are  within the y position of the bird respectively
		if ((bird.getPosY() <= topPipeEnd)) {
		    return true;
		} else if (bottomPipeStart <= bird.getPosY()) {
		    return true;
		}
	    }
	}
	return false;
    }

    public PowerUp getDescription() {
	return PowerUp.REGULAR;
    }
}
