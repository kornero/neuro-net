package com.neuronet.view;

import com.neuronet.api.INet;
import com.neuronet.api.generator.EductionSample;
import com.neuronet.api.generator.NetInfo;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.XChartPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public class NetGraphPanel extends XChartPanel {

    private static final Logger logger = LoggerFactory.getLogger(NetGraphPanel.class);
    private static final String EXPECTED = "exp(x)";
    private static final String ACTUAL = "act(x)";

    private final Chart chart;
    private final INet net;
    private final NetInfo netInfo;

    private NetGraphPanel(final Chart chart, INet net, NetInfo netInfo) {
        super(chart);
        if (chart == null) {
            throw new NullPointerException("Chart can not be null!");
        }
        this.chart = chart;
        this.net = net;
        this.netInfo = netInfo;

        this.repaint();
    }

    public NetGraphPanel(final INet net, final NetInfo netInfo) {
        this(createChart(net, netInfo), net, netInfo);
    }

    @Override
    public void repaint() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    if (chart != null) {
                        final Collection<Number> actual = chart.getSeriesMap().get(1).getyData();
                        actual.clear();
                        for (final EductionSample sample : netInfo.getTestData()) {
                            actual.add(net.run(sample.getInputsSample())[0]);
                            if (logger.isTraceEnabled()) {
                                logger.trace("f({})={}", sample.getInputsSample(), net.run(sample.getInputsSample()));
                            }
                        }
                    }

                }
            });
        } catch (InterruptedException | InvocationTargetException e) {
            logger.error("repaint(): ", e);
        }
        super.repaint();
    }

    public Chart getChart() {
        return this.chart;
    }

    public INet getNet() {
        return net;
    }

    public NetInfo getNetInfo() {
        return netInfo;
    }

    private static Chart createChart(final INet net, final NetInfo netInfo) {
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
        return QuickChart.getChart("Neural Net", "X", "Y",
                new String[]{EXPECTED, ACTUAL}, xData, new double[][]{expData, actData});
    }
}