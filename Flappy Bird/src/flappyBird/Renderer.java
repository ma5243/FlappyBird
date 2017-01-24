package flappyBird;

import javax.swing.*;
import java.awt.*;

/**
 * Created by abdullah on 1/22/2017.
 */
public class Renderer extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        FlappyBird.flappyBird.repaint(g);
    }
}
