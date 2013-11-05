package com.neuronet.view;

import com.neuronet.api.INet;
import com.neuronet.api.generator.EductionSample;
import com.neuronet.api.generator.NetInfo;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.XChartPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class Visualizer {

    private static final Logger logger = LoggerFactory.getLogger(Visualizer.class);

    public static Chart createChart(final INet net, final NetInfo netInfo) {
        final int size = netInfo.getTestData().size();
        final double[] xData = new double[size];
        final double[] expData = new double[size];
        final double[] actData = new double[size];
        int i = 0;
        for (final EductionSample sample : netInfo.getTestData()) {
            xData[i] = sample.getInputsSample()[0];
            expData[i] = sample.getExpectedOutputs()[0];
            actData[i] = net.run(sample.getInputsSample())[0];
            i++;
        }

        // Create Chart
        return QuickChart.getChart("Test data", "X", "Y",
                new String[]{"exp(x)", "act(x)"}, xData, new double[][]{expData, actData});
    }

    public static JFrame createFrame(final INet net, final NetInfo netInfo) {
        return createFrame(createChart(net, netInfo));
    }

    public static JFrame createFrame(final Chart chart) {
        JFrame frame = new JFrame("XChart");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel chartPanel = new XChartPanel(chart);
        frame.add(chartPanel);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
        return frame;
    }

    public static void visualize(final INet net, final NetInfo netInfo) {

        // Create Chart
        final Chart chart = createChart(net, netInfo);

        // Show it
        new SwingWrapper(chart).displayChart();
    }
}