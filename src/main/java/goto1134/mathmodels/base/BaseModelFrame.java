package goto1134.mathmodels.base;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * @author Andrew Yefanov.
 * @since 22.12.2016.
 */
public abstract class BaseModelFrame extends JFrame {
    private static ResourceBundle res = ResourceBundle.getBundle("base");
    @Setter(AccessLevel.PROTECTED)
    @Getter(AccessLevel.PROTECTED)
    private boolean isExerciseMode = false;
    private JPanel chartPanel;
    private JPanel settingsPanel;
    private JPanel theoryPanel;
    private JPanel mainPanel;
    private JTextPane commentArea;
    private JButton exerciseButton;
    @Setter
    private ExerciseModeListener exerciseRequestListener;

    protected BaseModelFrame(String aTitle) {
        super(aTitle);
        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        exerciseButton.addActionListener(e -> onModeChanged(!isExerciseMode));
    }

    protected void onModeChanged(boolean aValue) {
        isExerciseMode = aValue;
        if(aValue)
        {
            exerciseButton.setText(res.getString("example"));
        }
        else {
            exerciseButton.setText(res.getString("exercise"));
        }
        if (exerciseRequestListener != null) {
            exerciseRequestListener.setExerciseMode(aValue);
        }
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