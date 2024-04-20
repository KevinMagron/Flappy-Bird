package se.liu.kevma271.flappybird.objects;

/**
 * The Pipe class represents an obstacle in the Flappy Bird
 * game that the bird must navigate through.
 */
public class Pipe extends Handler {
    private static final int WIDTH = 64;
    private static final int HEIGHT = 464;
    /**
     * Margin between every couple of pipes.
     */
    private static final int MARGIN = 90;

    /**
     * The gap between the lower- and upperpipe.
     */
    private static final int GAP = 140;

    private static final int DEFAULT_SPEED = -5;
    private static final int INTANGIBILITY_SPEED = -30;

    /**
     * The upper part of the lowerpipe that looks like a mushroom.
     */
    private static final  int MUSHROOM = 29;

    public Pipe() {
        super(WIDTH, HEIGHT);
    }

    /**
     * Retrives a random starting point for where to draw the upper pipe.
     * If the starting point is less than -335 - which is the minimum
     * height for the upper pipe, than the default height is used.
     *
     * @return Y-position of where upper pipe shall be drawed.
     */
    public int getRandomHeight() {
        int randomHeight = (int) -(HEIGHT * Math.random());
        return randomHeight > -335 ? randomHeight : -HEIGHT;
    }

    public int getDefaultSpeed() {
        return DEFAULT_SPEED;
    }
    public int getIntangibilitySpeed() {
        return INTANGIBILITY_SPEED;
    }
    public int getGap() {
        return GAP;
    }
    public int getMargin() {
        return MARGIN;
    }
    public int getMushroom() {
        return MUSHROOM;
    }

    public void updatePosX(final int posX) {
        this.posX += posX;
    }
    public void setSpeed(final double speed) {
        this.speed = speed;
    }
}
