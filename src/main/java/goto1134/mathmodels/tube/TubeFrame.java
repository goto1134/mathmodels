package goto1134.mathmodels.tube;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import goto1134.mathmodels.base.BaseModelFrame;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;
import java.util.Scanner;


/**
 * Created by Andrew
 * on 14.12.2016.
 */
@Slf4j
class TubeFrame
        extends BaseModelFrame {
    private static final String VALUE = "value";
    private static final ResourceBundle res = ResourceBundle.getBundle("tube");

    private VariableListener<Integer> timeSliderListener;
    @Setter
    private VariableListener<Double> constantSpeedListener;
    private FunctionParametersListener densityListener;
    private FunctionParametersListener speedParametersListener;
    @Setter
    private VariableListener<Boolean> speedTypeListener;
    private JTabbedPane tabbedPane;
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
    private JPanel settings;

    TubeFrame() {
        super(res.getString("title"));
        timeSlider.addChangeListener(evt -> onTimeSliderChanged());

        p_a.addPropertyChangeListener(VALUE, evt -> onDensityParametersChanged());
        p_b.addPropertyChangeListener(VALUE, evt -> onDensityParametersChanged());
        p_c.addPropertyChangeListener(VALUE, evt -> onDensityParametersChanged());
        p_c.addPropertyChangeListener(VALUE, evt -> onPCParamChanged());
        p_d.addPropertyChangeListener(VALUE, evt -> onDensityParametersChanged());
        p_e.addPropertyChangeListener(VALUE, evt -> onDensityParametersChanged());

        u_a.addPropertyChangeListener(VALUE, evt -> onSpeedParametersChanged());
        u_b.addPropertyChangeListener(VALUE, evt -> onSpeedParametersChanged());
        u_c.addPropertyChangeListener(VALUE, evt -> onSpeedParametersChanged());
        u_d.addPropertyChangeListener(VALUE, evt -> onSpeedParametersChanged());
        u_e.addPropertyChangeListener(VALUE, evt -> onSpeedParametersChanged());

        u_const.addPropertyChangeListener(VALUE, evt -> onConstantSpeedChanged());
        tabbedPane.addChangeListener(evt -> setSpeed());

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
        this.u_const.setValue(3.);

        setSettingsComponent(settings);

        JTextPane theoryPane = new JTextPane();
        theoryPane.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
        theoryPane.setContentType("text/html");
        theoryPane.setText("<html>" + getTheory() + "</html>");
        setTheoryComponent(theoryPane);
    }

    private void onTimeSliderChanged() {
        if (!timeSlider.getValueIsAdjusting() && timeSliderListener != null) {
            int value = timeSlider.getValue();
            timeSliderListener.valueChanged(value);
            setComment(res.getString("time"));
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

    private void onPCParamChanged() {
        if (isExerciseMode() && ((Double) p_c.getValue()) == 0) {
            JOptionPane.showMessageDialog(this, "Достигнута целевая плотность потока");
        }
    }

    private void onSpeedParametersChanged() {
        if (speedParametersListener != null) {
            FunctionParameters parameters = new FunctionParameters(((Double) u_a.getValue()),
                                                                   (Double) u_b.getValue(),
                                                                   (Double) u_c.getValue(),
                                                                   (Double) u_d.getValue(),
                                                                   (Double) u_e.getValue());
            speedParametersListener.parametersChanged(parameters);
        }
    }

    private void onConstantSpeedChanged() {
        if (constantSpeedListener != null) {
            constantSpeedListener.valueChanged(((Double) u_const.getValue()));
        }
    }

    private void setSpeed() {
        if (speedTypeListener != null) {
            boolean isConstant = tabbedPane.getSelectedIndex() == 0;
            speedTypeListener.valueChanged(isConstant);
            if (isConstant) {
                setComment(res.getString("const"));
            } else {
                setComment(res.getString("variable"));
            }
        }
    }

    private String getTheory() {
        Parser parser = Parser.builder()
                              .build();

        String input = new Scanner(getClass()
                                           .getClassLoader()
                                           .getResourceAsStream("theory/tube_theory_ru.md"), "UTF-8")
                .useDelimiter("\\Z")
                .next();

        Node document = parser.parse(input);
        HtmlRenderer renderer = HtmlRenderer.builder()
                                            .build();
        return renderer.render(document);
    }

    @Override
    protected void onModeChanged(boolean aValue) {
        p_a.setValue(0.1);
        p_b.setValue(0.2);
        p_c.setValue(0.3);
        p_d.setValue(0.4);
        p_e.setValue(0.5);

        p_a.setEditable(!aValue);
        p_b.setEditable(!aValue);
        p_d.setEditable(!aValue);
        p_e.setEditable(!aValue);

        super.onModeChanged(aValue);
        if (aValue) {
            JOptionPane.showMessageDialog(this,
                                          "Добейтесь того, чтобы в любой момент времени\n Плотность потока была >=0.4");
        }
    }

    void setDensityListener(FunctionParametersListener densityListener) {
        this.densityListener = densityListener;
        onDensityParametersChanged();
    }

    void setSpeedParametersListener(FunctionParametersListener speedParametersListener) {
        this.speedParametersListener = speedParametersListener;
        onSpeedParametersChanged();
    }

    void setTimeSliderListener(VariableListener<Integer> timeSliderListener) {
        this.timeSliderListener = timeSliderListener;
    }

    public interface VariableListener<T> {
        void valueChanged(T value);
    }

    public interface FunctionParametersListener {
        void parametersChanged(FunctionParameters parameters);
    }
}
