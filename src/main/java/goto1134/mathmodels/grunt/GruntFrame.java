package goto1134.mathmodels.grunt;

import goto1134.mathmodels.base.BaseModelFrame;
import lombok.AllArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import static goto1134.mathmodels.grunt.GruntModel.DEFAULT_PARAMETERS;

/**
 * Created by Andrew
 * on 21.12.2016.
 */
class GruntFrame extends BaseModelFrame {
    private static final ResourceBundle res = ResourceBundle.getBundle("grunt");

    private JPanel settings;
    private JSlider timeStep;
    private JSlider pollutionIntensity;
    private JSlider barrierIntensity;
    private JSlider barrierWidth;
    private JSlider barrierDistance;
    private FunctionParameters parameters = DEFAULT_PARAMETERS;

    @Setter
    private ParametersChangeListener parametersChangeListener;

    GruntFrame() {
        super(res.getString("title"));
        setSettingsComponent(settings);
        setTheoryComponent(new GruntTheory().mainPanel);

        timeStep.setValue(parameters.getMax_t());
        pollutionIntensity.setValue(parameters.getC());
        barrierIntensity.setValue(parameters.getQ());
        barrierWidth.setValue(parameters.getX());
        barrierDistance.setValue(parameters.getY());

        timeStep.addChangeListener(new PropertyListener(parameters1 -> parameters.getMax_t() < parameters1.getMax_t(),
                res.getString("time_inc"),
                res.getString("time_dec")));
        pollutionIntensity.addChangeListener(new PropertyListener(parameters1 -> parameters.getC() < parameters1.getC(),
                res.getString("source_int_inc"),
                res.getString("source_int_dec")));
        barrierIntensity.addChangeListener(new PropertyListener(parameters1 -> parameters.getQ() < parameters1.getQ(),
                res.getString("bar_int_inc"),
                res.getString("bar_int_dec")));
        barrierWidth.addChangeListener(new PropertyListener(parameters1 -> parameters.getX() < parameters1.getX(),
                res.getString("bar_width_inc"),
                res.getString("bar_width_dec")));
        barrierDistance.addChangeListener(new PropertyListener(parameters1 -> parameters.getY() < parameters1.getY(),
                res.getString("bar_dist_inc"),
                res.getString("bar_dist_dec")));
    }

    private void onParametersChanged(FunctionParameters parameters) {
        this.parameters = parameters;
        if (parametersChangeListener != null) {

            parametersChangeListener.onParametersChanged(parameters);
        }
    }

    private FunctionParameters getFunctionParameters() {
        return new FunctionParameters(timeStep.getValue(),
                pollutionIntensity.getValue(),
                barrierIntensity.getValue(),
                barrierWidth.getValue(),
                barrierDistance.getValue());
    }

    interface ParametersChangeListener {
        void onParametersChanged(FunctionParameters parameters);
    }

    @AllArgsConstructor
    private class PropertyListener implements ChangeListener {
        Predicate<FunctionParameters> parametersPredicate;
        String caseTrue;
        String caseFalse;


        @Override
        public void stateChanged(ChangeEvent e) {

            FunctionParameters parameters = getFunctionParameters();
            if (((JSlider) e.getSource()).getValueIsAdjusting()) {
                String comment;
                if (parametersPredicate.test(parameters)) {
                    comment = caseTrue;
                } else {
                    comment = caseFalse;
                }
                setComment(comment);
            }
            onParametersChanged(parameters);
        }
    }
}
