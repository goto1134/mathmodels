package goto1134.mathmodels.tube;

import goto1134.mathmodels.tube.TubeFrame.TimeSliderListener;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.XYStyler;

/**
 * Created by Andrew
 * on 13.12.2016.
 */
class SimpleModel
        implements TimeSliderListener {

    public static final String SAMPLE_CHART = "Density";
    public static final String SERIES_NAME = "y(x)=p(x,t)";

    public static void main(String[] args) {
        // Show it
        TubeFrame frame = new TubeFrame();
        new SimpleModel(frame);
        frame.setVisible(true);
    }

    private final XChartPanel<XYChart> chartPanel;
    private final double[] xData;
    private double constantSpeed = 3;
    private int currentTime = 0;
    private FunctionParameters densityParameters;
    private FunctionParameters speedParameters;

    SimpleModel(TubeFrame frame) {

        frame.setTimeSliderListener(this);
        frame.setDensityListener(this::onDensityParametersChanged);
        xData = new double[10000];
        double[] yData = new double[xData.length];
        for (int i = 0; i < xData.length; i++) {
            xData[i] = i * ((double) 100) / xData.length;
            yData[i] = p(xData[i], 0);
        }
        XYChart chart = QuickChart.getChart(SAMPLE_CHART, "x", "p(x,t)", SERIES_NAME, xData, yData);
        XYStyler styler = chart.getStyler();
        styler.setXAxisMin(0d).setYAxisMin(0d);
        chartPanel = new XChartPanel<>(chart);
        frame.setChart(chartPanel);
    }

    double p(double x, double t) {
        return p0(x + constantSpeed * t);
    }

    double p0(double x) {
        return densityParameters.getA() * Math.cos(densityParameters.getB() * x)
                + densityParameters.getC() * Math.sin(densityParameters.getD() * x)
                + densityParameters.getE();
    }

    private void onDensityParametersChanged(FunctionParameters functionParameters) {
        densityParameters = functionParameters;
        updateChart();
    }

    private void updateChart() {
        if (chartPanel != null) {
            double[] yData = new double[xData.length];

            for (int i = 0; i < xData.length; i++) {
                yData[i] = p(xData[i], currentTime);
            }

            chartPanel.getChart().updateXYSeries(SERIES_NAME, xData, yData, null);
            chartPanel.repaint();
        }
    }

    @Override
    public void timeChanged(int time) {
        this.currentTime = time;
        updateChart();
    }
}
