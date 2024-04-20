package se.liu.kevma271.flappybird.visuals;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract class providing common functionality for handling visual aspects of the Flappy Bird game.
 * Classes extending this abstract class should implement the methods defined in the {@link Contract} interface.
 */
public abstract class Handler extends JPanel implements Contract {
    protected JFrame frame = null;

    public void showFrame() {
        frame.setVisible(true);
        repaint();
    }

    public void dispose() {
        if (frame != null) {
            frame.dispose();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
}
