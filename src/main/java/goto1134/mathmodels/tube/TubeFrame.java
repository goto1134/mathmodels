package goto1134.mathmodels.tube;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Andrew
 * on 14.12.2016.
 */
class TubeFrame extends JFrame {
    private TimeSliderListener timeSliderListener;
    private FunctionParametersListener densityListener;
    private FunctionParametersListener speedParametersListener;
    private JPanel chartPanel;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JSlider timeSlider;
    private JFormattedTextField u_const;
    private JFormattedTextField u_a;
    private JFormattedTextField u_b;
    private JFormattedTextField u_c;
    private JFormattedTextField u_d;
    private JFormattedTextField u_e;
    private JFormattedTextField p_a;
    private JFormattedTextField p_b;
    private JFormattedTextField p_c;
    private JFormattedTextField p_d;
    private JFormattedTextField p_e;

    TubeFrame() {
        super("Tube");
        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();

        timeSlider.addChangeListener(e -> onTimeSliderChanged());
        p_a.addPropertyChangeListener("value", evt -> onDensityParametersChanged());
        p_b.addPropertyChangeListener("value", evt -> onDensityParametersChanged());
        p_c.addPropertyChangeListener("value", evt -> onDensityParametersChanged());
        p_d.addPropertyChangeListener("value", evt -> onDensityParametersChanged());
        p_e.addPropertyChangeListener("value", evt -> onDensityParametersChanged());
    }

    private void onTimeSliderChanged() {
        if (!timeSlider.getValueIsAdjusting() && timeSliderListener != null) {
            int value = timeSlider.getValue();
            timeSliderListener.timeChanged(value);
            repaint();
        }
    }

    private void onDensityParametersChanged() {
        if (densityListener != null) {
            FunctionParameters parameters = new FunctionParameters(((Double) p_a.getValue()),
                    (Double) p_b.getValue(),
                    (Double) p_c.getValue(),
                    (Double) p_d.getValue(),
                    (Double) p_e.getValue());
            densityListener.parametersChanged(parameters);
        }
    }

    public void setDensityListener(FunctionParametersListener densityListener) {
        this.densityListener = densityListener;
        onDensityParametersChanged();
    }

    public void setSpeedParametersListener(FunctionParametersListener speedParametersListener) {
        this.speedParametersListener = speedParametersListener;
    }

    public void setTimeSliderListener(TimeSliderListener timeSliderListener) {
        this.timeSliderListener = timeSliderListener;
        this.p_a.setValue(0.1);
        this.p_b.setValue(0.2);
        this.p_c.setValue(0.3);
        this.p_d.setValue(0.4);
        this.p_e.setValue(0.5);

        this.u_a.setValue(0.1);
        this.u_b.setValue(0.2);
        this.u_c.setValue(0.3);
        this.u_d.setValue(0.4);
        this.u_e.setValue(0.5);
    }


    void setChart(JPanel chart) {
        chartPanel.add(chart, BorderLayout.CENTER);
    }

    public static interface TimeSliderListener {
        void timeChanged(int time);
    }

    public interface FunctionParametersListener {
        void parametersChanged(FunctionParameters parameters);
    }
}
