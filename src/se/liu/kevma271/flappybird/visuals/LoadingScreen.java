package se.liu.kevma271.flappybird.visuals;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * The LoadingScreen class represents a graphical loading screen component
 * used in the Flappy Bird game to display an image while loading.
 */
public class LoadingScreen extends Handler {
    private static final int IMAGE_WIDTH = 660;
    private static final int IMAGE_HEIGHT = 600;

    private Image backgroundImage = null;

    public LoadingScreen() throws FileNotFoundException {
        setFrameSettings();
        loadImages();
    }

    public void setFrameSettings() {
        frame = new JFrame("Image Frame");
        setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        frame.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
    }

    public void loadImages() throws FileNotFoundException {
        backgroundImage = new ImageIcon(ClassLoader.getSystemResource("images" + File.separator + "loadingscreen.png")).getImage();
        if (backgroundImage == null) {
            throw new FileNotFoundException("Image not found: images/loadingscreen.png");
        }
    }

    public void draw(Graphics g) {
	int imageScreenPosition = 0;
	g.drawImage(backgroundImage, imageScreenPosition, imageScreenPosition, IMAGE_WIDTH, IMAGE_HEIGHT, null);
    }
}
