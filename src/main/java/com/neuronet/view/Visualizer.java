package com.neuronet.view;

import com.neuronet.api.INet;
import com.neuronet.api.generator.EducationSample;
import com.neuronet.api.generator.INetInfo;
import com.neuronet.util.Util;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;

import javax.swing.*;
import java.util.List;

public class Visualizer implements IVisualizer {

    @Override
    public Chart createChart(final INet net, final INetInfo netInfo) {
        final List<EducationSample> samples = netInfo.getEducationDataSource().getTestData();
        final double[] xData = getX(samples);
        final double[] expData = getY(samples);
        final double[] actData = getY(samples, net);

        // Create Chart
        return QuickChart.getChart("Neural Net", "X", "Y",
                new String[]{EXPECTED, ACTUAL},
                xData,
                new double[][]{expData, Util.denormalizeOutputs(actData, netInfo.getNetConfiguration())}
        );
    }

    @Override
    public JFrame createFrame(final INet net, final INetInfo netInfo) {
        return createFrame(new NetGraphPanel(net, netInfo, this));
    }

    @Override
    public JFrame createFrame(final JPanel chartPanel) {
        final JFrame frame = new JFrame("XChart");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(chartPanel);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
        return frame;
    }

    @Override
    public void visualize(final INet net, final INetInfo netInfo) {

        // Create Chart
        final Chart chart = createChart(net, netInfo);

        // Show it
        new SwingWrapper(chart).displayChart();
    }

    @Override
    public double[] getX(final List<EducationSample> samples) {
        final double[] xData = new double[samples.size()];
        int i = 0;
        for (final EducationSample sample : samples) {
            xData[i] = sample.getInputsSample()[0];
            i++;
        }
        return xData;
    }

    @Override
    public double[] getY(final List<EducationSample> samples) {
        final double[] expData = new double[samples.size()];
        int i = 0;
        for (final EducationSample sample : samples) {
            expData[i] = sample.getExpectedOutputs()[0];
            i++;
        }
        return expData;
    }

    @Override
    public double[] getY(final List<EducationSample> samples, final INet net) {
        final double[] actData = new double[samples.size()];
        int i = 0;
        for (final EducationSample sample : samples) {
            actData[i] = net.run(sample.getInputsSample())[0];
            i++;
        }
        return actData;
    }
}