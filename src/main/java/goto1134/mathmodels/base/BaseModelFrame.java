package goto1134.mathmodels.base;

import lombok.Setter;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andrew Yefanov.
 * @since 22.12.2016.
 */
public abstract class BaseModelFrame extends JFrame {
    private JPanel chartPanel;
    private JPanel settingsPanel;
    private JPanel theoryPanel;
    private JPanel mainPanel;
    private JTextPane commentArea;
    private JTabbedPane modePanel;
    @Setter
    private ExerciseModeListener exerciseRequestListener;

    protected BaseModelFrame(String aTitle) {
        super(aTitle);
        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        modePanel.addChangeListener(e -> {
            if (exerciseRequestListener != null) {
                exerciseRequestListener.setExerciseMode(modePanel.isEnabledAt(1));
            }
        });
    }

    public void setChart(JPanel chart) {
        chartPanel.removeAll();
        chartPanel.add(chart);
        chartPanel.revalidate();
        chartPanel.repaint();
        pack();
    }

    protected void setSettingsComponent(Component settingsComponent) {
        settingsPanel.removeAll();
        settingsPanel.add(settingsComponent);
        settingsPanel.revalidate();
        settingsPanel.repaint();
    }

    protected void setTheoryComponent(Component theoryComponent) {
        theoryPanel.removeAll();
        theoryPanel.add(new JScrollPane(theoryComponent));
        theoryPanel.revalidate();
        theoryPanel.repaint();

    }

    protected void setComment(String aCommentText) {
        commentArea.setText(aCommentText);
    }

    public interface ExerciseModeListener {
        void setExerciseMode(boolean exerciseMode);
    }
}