package se.liu.kevma271.flappybird.objects;

/**
 * Abstract class providing common functionality for handling game objects of
 * the Flappy Bird game. Classes extending this abstract class should implement
// * the methods defined in the {@link se.liu.kevma271.flappybird.objects.Contract} interface.
 */
public abstract class Handler implements Contract {
    protected int width;
    protected int height;
    protected int posX;
    protected double speed = 0;
    protected int posY = 0;

    protected Handler(final int width, final int height) {
	this.width = width;
	this.height = height;
    }

    public int getPosX() {
	return posX;
    }
    public int getWidth() {
	return width;
    }
    public int getHeight() {
	return height;
    }
    public double getSpeed() {
	return speed;
    }
    public int getPosY() {
	return posY;
    }

    public void setPosY(final int posY) {
	this.posY = posY;
    }
}
