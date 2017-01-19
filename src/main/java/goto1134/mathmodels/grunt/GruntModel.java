package goto1134.mathmodels.grunt;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.util.Arrays;
import java.util.ResourceBundle;

import static org.knowm.xchart.QuickChart.getChart;

/**
 * Created by Andrew
 * on 21.12.2016.
 */
class GruntModel {

    static final FunctionParameters DEFAULT_PARAMETERS = new FunctionParameters(100, 100, 50, 8, 40);
    private static final ResourceBundle res = ResourceBundle.getBundle("grunt");
    private static final String POLLUTION = res.getString("pollution");
    private static final String BARRIER = res.getString("barrier");
    private static final String RESULT_POLLUTION = res.getString("result_pollution");

    public static void main(String[] args) {
        // Show it
        GruntFrame frame = new GruntFrame();
        new GruntModel(frame);
        frame.setVisible(true);
    }

    private final XChartPanel<XYChart> chartPanel;
    boolean isExerciseMode = false;
    private FunctionParameters parameters = DEFAULT_PARAMETERS;

    private GruntModel(GruntFrame frame) {

        frame.setParametersChangeListener(this::onParametersChanged);

        XYChart chart = getChart(res.getString("title"), "Время", "Уровень загрящнения", POLLUTION,
                                 new double[]{1, 2}, new double[]{1, 2});
        chart.addSeries(BARRIER, new double[]{1, 2}, new double[]{1, 2});
        chart.addSeries(RESULT_POLLUTION, new double[]{1, 2}, new double[]{1, 2});
        chart.getStyler()
             .setXAxisMin(0d)
             .setYAxisMin(0d);
        chartPanel = new XChartPanel<>(chart);
        frame.setChart(chartPanel);
        frame.setExerciseRequestListener(this::onExerciseMode);
        updateChart();
    }

    private void updateChart() {

        int max_t = parameters.getMax_t();

        double[] pollution = new double[max_t];
        pollution[0] = parameters.getC();
        double[] barrier = new double[max_t];
        barrier[0] = parameters.getQ();
        double[] resultPollution = new double[max_t];
        resultPollution[0] = pollution[0] - barrier[0];

        double c = pollution[0] * 2.5 / (Math.sqrt(2.0 * Math.PI));
        double q = barrier[0] * 2.5 / (Math.sqrt(2.0 * Math.PI));

        double[] xData = new double[max_t];
        xData[0] = 0;

        for (int i = 1; i < max_t; i++) {
            xData[i] = i;
            double y0 = parameters.getY();
            if (Math.abs(y0 - i) > parameters.getX() / 2) {
                barrier[i] = q * Math.exp(exp1(i, (int) y0));
            } else {
                barrier[i] = barrier[i - 1];
            }
            pollution[i] = c * Math.exp(exp(i));
            resultPollution[i] = pollution[i] - barrier[i];
            if (resultPollution[i] < 0) {
                resultPollution[i] = 0;
            }

        }

        chartPanel.getChart()
                  .updateXYSeries(POLLUTION, xData, pollution, null);
        chartPanel.getChart()
                  .updateXYSeries(BARRIER, xData, barrier, null);
        chartPanel.getChart()
                  .updateXYSeries(RESULT_POLLUTION, xData, resultPollution, null);
        chartPanel.repaint();

        if (isExerciseMode) {
            if (Arrays.stream(resultPollution)
                      .skip(20)
                      .allMatch(value -> value < 15)) {
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Результат достигнут");
            }
        }
    }

    private double exp1(int i, int k) {
        return (double) (k - i) * (i - k) / parameters.getMax_t();
    }

    private double exp(int i) {
        return (double) (1 - i) * (i - 1) / (parameters.getMax_t() * 10);
    }

    private void onExerciseMode(boolean b) {
        isExerciseMode = b;
        if (b) {
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Добейтесь того, чтобы через 20 минут\n" +
                    "уровень загрязнения был ниже 15");
        }
    }

    private void onParametersChanged(FunctionParameters parameters) {
        this.parameters = parameters;
        updateChart();
    }

}
