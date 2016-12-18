package goto1134.mathmodels.tube;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Andrew
 * on 14.12.2016.
 */
class TubeFrame extends JFrame {
    private TimeSliderListener timeSliderListener;
    private JPanel chartPanel;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JSlider timeSlider;

    TubeFrame() {
        super("Tube");
        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();

        timeSlider.addChangeListener(e -> onTimeSliderChanged());
    }

    private void onTimeSliderChanged() {
        if (!timeSlider.getValueIsAdjusting() && timeSliderListener != null) {
            int value = timeSlider.getValue();
            timeSliderListener.timeChanged(value);
            repaint();
        }
    }

    public void setTimeSliderListener(TimeSliderListener timeSliderListener) {
        this.timeSliderListener = timeSliderListener;
    }

    void setChart(JPanel chart) {
        chartPanel.add(chart, BorderLayout.CENTER);
    }

    public static interface TimeSliderListener {
        void timeChanged(int time);
    }
}
