package goto1134.mathmodels.grunt;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import java.util.ResourceBundle;

/**
 * Created by Andrew
 * on 21.12.2016.
 */
class GruntModel {

    static final FunctionParameters DEFAULT_PARAMETERS = new FunctionParameters(100, 100, 50, 8, 40);
    private static final ResourceBundle res = ResourceBundle.getBundle("grunt");
    private static final String SERIES1_NAME = res.getString("pollution");
    private static final String SERIES2_NAME = res.getString("barrier");
    private static final String SERIES3_NAME = res.getString("result_pollution");

    public static void main(String[] args) {
        // Show it
        GruntFrame frame = new GruntFrame();
        new GruntModel(frame);
        frame.setVisible(true);
    }
    private final XChartPanel<XYChart> chartPanel;
    private FunctionParameters parameters = DEFAULT_PARAMETERS;

    private GruntModel(GruntFrame frame) {

        frame.setParametersChangeListener(this::onParametersChanged);

        XYChart chart = QuickChart.getChart("chart_name", "x", "p(x,t)", SERIES1_NAME, new double[]{1, 2}, new double[]{1, 2});
        chart.addSeries(SERIES2_NAME, new double[]{1, 2}, new double[]{1, 2});
        chart.addSeries(SERIES3_NAME, new double[]{1, 2}, new double[]{1, 2});
        chart.getStyler().setXAxisMin(0d).setYAxisMin(0d);
        chartPanel = new XChartPanel<>(chart);
        frame.setChart(chartPanel);
        updateChart();
    }

    private void updateChart() {

        int max_t = parameters.getMax_t();

        double[] masC = new double[max_t];
        masC[0] = parameters.getC();
        double[] masQ = new double[max_t];
        masQ[0] = parameters.getQ();
        double[] masS = new double[max_t];
        masS[0] = masC[0] - masQ[0];

        double c = masC[0] * 2.5 / (Math.sqrt(2.0 * Math.PI));
        double q = masQ[0] * 2.5 / (Math.sqrt(2.0 * Math.PI));

        double[] xData = new double[max_t];
        xData[0] = 0;

        for (int i = 1; i < max_t; i++) {
            xData[i] = i;
            double y0 = parameters.getY();
            if (Math.abs(y0 - i) > parameters.getX() / 2) {
                masQ[i] = q * Math.exp(exp1(i, (int) y0));
            } else {
                masQ[i] = masQ[i - 1];
            }
            masC[i] = c * Math.exp(exp(i));
            masS[i] = masC[i] - masQ[i];
            if (masS[i] < 0) {
                masS[i] = 0;
            }

        }

        chartPanel.getChart().updateXYSeries(SERIES1_NAME, xData, masC, null);
        chartPanel.getChart().updateXYSeries(SERIES2_NAME, xData, masQ, null);
        chartPanel.getChart().updateXYSeries(SERIES3_NAME, xData, masS, null);
        chartPanel.repaint();
    }

    private double exp1(int i, int k) {
        return (double) (k - i) * (i - k) / parameters.getMax_t();
    }

    private double exp(int i) {
        return (double) (1 - i) * (i - 1) / (parameters.getMax_t() * 10);
    }

    private void onParametersChanged(FunctionParameters parameters) {
        this.parameters = parameters;
        updateChart();
    }

}
