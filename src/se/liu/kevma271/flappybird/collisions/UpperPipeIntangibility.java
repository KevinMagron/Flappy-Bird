package se.liu.kevma271.flappybird.collisions;

import se.liu.kevma271.flappybird.Board;
import se.liu.kevma271.flappybird.PowerUp;
import se.liu.kevma271.flappybird.objects.Bird;
import se.liu.kevma271.flappybird.objects.Pipe;

import java.util.List;

/**
 * Implementation of the CollisionHandler interface for handling
 * collisions during the gravity superpower, where collisions
 * cannot occur with upperpipes.
 */
public class UpperPipeIntangibility implements Contract {

    /**
     * {@inheritDoc}
     * <p>
     * This implementation checks for collisions based on the default
     * strategy with an exception. The bird can fly through the upperpipe
     * as long as it is above the mushroom of the pipe.
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
		if (bird.getPosY() <= topPipeEnd && bird.getPosY() >= topPipeEnd-pipe.getMushroom()) {
		    return true;
		} else if (bottomPipeStart <= bird.getPosY()) {
		    return true;
		}
	    }
	}
	return false;
    }

    public PowerUp getDescription() {
	return PowerUp.GRAVITY;
    }

}
