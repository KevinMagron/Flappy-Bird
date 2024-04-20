package se.liu.kevma271.flappybird.objects;

import se.liu.kevma271.flappybird.PowerUp;

/**
 * Represents the bird in the Flappy Bird game. Every game a new instance
 * is created with the same width and height.
 */
public class Bird extends Handler {
    private static final int WIDTH = 30;
    private static final int HEIGHT = 24;
    private PowerUp type = null;

    public Bird() {
        super(WIDTH, HEIGHT);
    }

    public PowerUp getType() {
	return type;
    }

    public void setType(PowerUp type) {
	this.type = type;
    }
    public void setPosX(final int posX) {
	this.posX = posX;
    }
    public void updatePosY(final int posY) {
        this.posY += posY;
    }
    public void updateSpeed(final double velocityChange) {
        this.speed += velocityChange;
    }
}
