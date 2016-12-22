package goto1134.mathmodels.tube;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import goto1134.mathmodels.base.BaseModelFrame;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;


/**
 * Created by Andrew
 * on 14.12.2016.
 */
@Slf4j
class TubeFrame extends BaseModelFrame {
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

    private String getTheory() {
        Parser parser = Parser.builder().build();

        String input = "Error while loading resource file";
        try {
            Path path = Paths.get(getClass()
                    .getClassLoader()
                    .getResource("theory/tube_theory_ru.md")
                    .toURI());
            byte[] bytes = Files.readAllBytes(path);
            input = new String(bytes);
        } catch (URISyntaxException | IOException e) {
            log.error("", e);
        }

        Node document = parser.parse(input);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    public interface VariableListener<T> {
        void valueChanged(T value);
    }

    public interface FunctionParametersListener {
        void parametersChanged(FunctionParameters parameters);
    }
}
