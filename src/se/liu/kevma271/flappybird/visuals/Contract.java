package se.liu.kevma271.flappybird.visuals;

import java.awt.*;
import java.io.FileNotFoundException;

/**
 * Interface defining the contract for handling visual aspects of the Flappy Bird game.
 * Classes implementing this interface should provide implementations for the defined methods.
 */
public interface Contract {
    void setFrameSettings();
    void loadImages() throws FileNotFoundException;
    void showFrame();
    void dispose();
    void draw(Graphics g);
}
