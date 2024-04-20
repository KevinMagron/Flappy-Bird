package se.liu.kevma271.flappybird.objects;

/**
 * The game object interface defines attributes and fields
 * that every object the game should have.
 */
public interface Contract {
    public int getWidth();
    public int getHeight();
    public int getPosX();
    public int getPosY();
    public double getSpeed();
}
