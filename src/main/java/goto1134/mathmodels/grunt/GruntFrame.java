package goto1134.mathmodels.grunt;

import goto1134.mathmodels.base.BaseModelFrame;
import lombok.Setter;

import javax.swing.*;
import java.util.ResourceBundle;

import static goto1134.mathmodels.grunt.GruntModel.DEFAULT_PARAMETERS;

/**
 * Created by Andrew
 * on 21.12.2016.
 */
class GruntFrame extends BaseModelFrame {
    private static final String VALUE = "value";
    private static final ResourceBundle res = ResourceBundle.getBundle("tube");

    private JPanel settings;
    private JSlider time_step;
    private JSlider pollution_intensivity;
    private JSlider barrier_intensivity;
    private JSlider barrier_width;
    private JSlider barrier_distanse;

    @Setter
    private ParametersChangeListener parametersChangeListener;

    GruntFrame() {
        super(res.getString("title"));
        setSettingsComponent(settings);

        time_step.setValue(DEFAULT_PARAMETERS.getMax_t());
        pollution_intensivity.setValue(DEFAULT_PARAMETERS.getC());
        barrier_intensivity.setValue(DEFAULT_PARAMETERS.getQ());
        barrier_width.setValue(DEFAULT_PARAMETERS.getX());
        barrier_distanse.setValue(DEFAULT_PARAMETERS.getY());

        time_step.addChangeListener(evt -> onParametersChanged());
        pollution_intensivity.addChangeListener(evt -> onParametersChanged());
        barrier_intensivity.addChangeListener(evt -> onParametersChanged());
        barrier_width.addChangeListener(evt -> onParametersChanged());
        barrier_distanse.addChangeListener(evt -> onParametersChanged());
    }

    private void onParametersChanged() {
        if (parametersChangeListener != null) {
            FunctionParameters parameters = new FunctionParameters(((Integer) time_step.getValue()),
                    pollution_intensivity.getValue(),
                    barrier_intensivity.getValue(),
                    barrier_width.getValue(),
                    barrier_distanse.getValue());
            parametersChangeListener.onParametersChanged(parameters);
        }
    }


    interface ParametersChangeListener {
        void onParametersChanged(FunctionParameters parameters);
    }
}
