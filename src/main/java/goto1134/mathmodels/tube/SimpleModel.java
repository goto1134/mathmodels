package goto1134.mathmodels.tube;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.XYStyler;

/**
 * Created by Andrew
 * on 13.12.2016.
 */
@Slf4j
class SimpleModel {

    private static final String SAMPLE_CHART = "Density";
    private static final String SERIES_NAME = "y(x)=p(x,t)";

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
    private boolean isSpeedConstant = true;

    private SimpleModel(TubeFrame frame) {

        frame.setTimeSliderListener(this::onTimeChanged);
        frame.setDensityListener(this::onDensityParametersChanged);
        frame.setSpeedParametersListener(this::onSpeedParametersChanged);
        frame.setSpeedTypeListener(this::onSpeedTypeChanged);
        frame.setConstantSpeedListener(this::onConstantSpeedChanged);
        xData = new double[10000];
        double[] yData = new double[xData.length];
        for (int i = 0; i < xData.length; i++) {
            xData[i] = i * ((double) 100) / xData.length;
            yData[i] = density(xData[i], 0);
        }
        XYChart chart = QuickChart.getChart(SAMPLE_CHART, "x", "density(x,t)", SERIES_NAME, xData, yData);
        XYStyler styler = chart.getStyler();
        styler.setXAxisMin(0d).setYAxisMin(0d);
        chartPanel = new XChartPanel<>(chart);
        frame.setChart(chartPanel);
    }

    private double density(double coordinate, double time) {
        if (time == 0) {
            return defaultDensity(coordinate);
        }
        return defaultDensity(coordinate + speedDependentComponent(time));
    }

    private double defaultDensity(double coordinate) {
        return densityParameters.getA() * Math.cos(densityParameters.getB() * coordinate)
                + densityParameters.getC() * Math.sin(densityParameters.getD() * coordinate)
                + densityParameters.getE();
    }

    private double speedDependentComponent(double t) {
        if (isSpeedConstant) {
            return constantSpeed * t;
        } else {
            RombergIntegrator rombergIntegrator = new RombergIntegrator();
            return rombergIntegrator.integrate(10000, this::variableSpeed, 0, t);
        }
    }

    private void onConstantSpeedChanged(Double constantSpeed) {
        this.constantSpeed = constantSpeed;
        updateChart();
    }

    private void updateChart() {
        if (chartPanel != null) {
            double[] yData = new double[xData.length];

            for (int i = 0; i < xData.length; i++) {
                yData[i] = density(xData[i], currentTime);
            }

            chartPanel.getChart().updateXYSeries(SERIES_NAME, xData, yData, null);
            chartPanel.repaint();
        }
    }

    private void onSpeedTypeChanged(boolean isSpeedConstant) {
        this.isSpeedConstant = isSpeedConstant;
        log.debug("type changed");
        updateChart();
    }

    private double variableSpeed(double time) {
        return speedParameters.getA() * Math.cos(speedParameters.getB() * time)
                + speedParameters.getC() * Math.sin(speedParameters.getD() * time)
                + speedParameters.getE();
    }

    private void onDensityParametersChanged(FunctionParameters functionParameters) {
        densityParameters = functionParameters;
        updateChart();
    }

    private void onSpeedParametersChanged(FunctionParameters functionParameters) {
        speedParameters = functionParameters;
        updateChart();
    }

    private void onTimeChanged(int time) {
        this.currentTime = time;
        updateChart();
    }
}
