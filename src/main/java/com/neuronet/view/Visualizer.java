package com.neuronet.view;

import com.neuronet.api.INet;
import com.neuronet.api.generator.EducationSample;
import com.neuronet.api.generator.NetInfo;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class Visualizer {

    private static final Logger logger = LoggerFactory.getLogger(Visualizer.class);

    private static final String EXPECTED = "exp(x)";
    private static final String ACTUAL = "act(x)";

    public static Chart createChart(final INet net, final NetInfo netInfo) {
        final int size = netInfo.getTestData().size();
        final double[] xData = new double[size];
        final double[] expData = new double[size];
        final double[] actData = new double[size];
        int i = 0;
        for (final EducationSample sample : netInfo.getTestData()) {
            xData[i] = sample.getInputsSample()[0];
            expData[i] = sample.getExpectedOutputs()[0];
            actData[i] = net.run(sample.getInputsSample())[0];
            i++;
        }

        // Create Chart
        return QuickChart.getChart("Neural Net", "X", "Y",
                new String[]{EXPECTED, ACTUAL}, xData, new double[][]{expData, actData});
    }

    public static JFrame createFrame(final INet net, final NetInfo netInfo) {
        return createFrame(new NetGraphPanel(net, netInfo));
    }

    public static JFrame createFrame(final JPanel chartPanel) {
        final JFrame frame = new JFrame("XChart");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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