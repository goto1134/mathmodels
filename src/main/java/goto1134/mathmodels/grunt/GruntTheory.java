package goto1134.mathmodels.grunt;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.net.URL;

/**
 * @author Andrew Yefanov.
 * @since 22.12.2016.
 */
@Slf4j
class GruntTheory {
    JPanel mainPanel;
    private JPanel imagePanel;

    GruntTheory() {
        URL barrierIMG = getClass().getClassLoader().getResource("theory/barrier.png");
        ImageIcon imageIcon = new ImageIcon(barrierIMG);
        imagePanel.add(new JLabel(imageIcon));

    }
}