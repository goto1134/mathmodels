package goto1134.mathmodels.tube;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

/**
 * Created by Andrew
 * on 13.12.2016.
 */
class SimpleModel
        implements TubeFrame.TimeSliderListener {

    public static final String SAMPLE_CHART = "Sample Chart";
    public static final String SERIES_NAME = "y(x)";

    public static void main(String[] args) {
        // Show it
        TubeFrame frame = new TubeFrame();
        new SimpleModel(frame);
        frame.setVisible(true);
    }

    private final XChartPanel<XYChart> chartPanel;
    private final double[] x_row;
    private double u0 = 3;
    private int time = 0;

    SimpleModel(TubeFrame frame) {
        x_row = new double[10000];
        double[] yData = new double[x_row.length];
        for (int i = 0; i < x_row.length; i++) {
            x_row[i] = i * ((double) 100) / x_row.length;
            yData[i] = p(x_row[i], 0);
        }

        XYChart chart = QuickChart.getChart(SAMPLE_CHART, "X", "Y", SERIES_NAME, x_row, yData);
        chart.getAxisPair().overrideMinMax();
        frame.setTimeSliderListener(this);
        chartPanel = new XChartPanel<>(chart);
        frame.setChart(chartPanel);
    }

    double p(double x, double t) {
        return p0(x + u0 * t);
    }

    double p0(double x) {
        return 0.1 * Math.cos(0.2 * x) + 3;
    }

    @Override
    public void timeChanged(int time) {
        this.time = time;
        updateChart();
    }

    private void updateChart() {
        double[] yData = new double[x_row.length];

        for (int i = 0; i < x_row.length; i++) {
            yData[i] = p(x_row[i], time);
        }

        chartPanel.getChart().updateXYSeries(SERIES_NAME, x_row, yData, null);
        chartPanel.repaint();
    }
}
