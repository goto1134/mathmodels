package goto1134.mathmodels.tube;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import java.util.ResourceBundle;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by Andrew
 * on 13.12.2016.
 */
@Slf4j
class TubeModel {

    private static final String SERIES_NAME = "Плотность потока";
    private static ResourceBundle res = getBundle("tube");
    private final XChartPanel<XYChart> chartPanel;
    private final double[] xData;
    private double constantSpeed = 3;
    private int currentTime = 0;
    @Setter
    private FunctionParameters densityParameters;
    private FunctionParameters speedParameters;
    private boolean isSpeedConstant = true;

    private TubeModel(TubeFrame frame) {

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
        XYChart chart = QuickChart.getChart(res.getString("chart_name"), "Расстояние", "Плотность", SERIES_NAME, xData, yData);
        chart.getStyler().setXAxisMin(0d).setYAxisMin(0d);
        chartPanel = new XChartPanel<>(chart);
        frame.setChart(chartPanel);
    }

    public static void main(String[] args) {
        // Show it
        TubeFrame frame = new TubeFrame();
        new TubeModel(frame);
        frame.setVisible(true);
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
