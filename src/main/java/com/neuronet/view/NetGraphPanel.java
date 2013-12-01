package com.neuronet.view;

import com.neuronet.api.INet;
import com.neuronet.api.generator.EducationSample;
import com.neuronet.api.generator.INetInfo;
import com.neuronet.util.Util;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.XChartPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

public class NetGraphPanel extends XChartPanel {

    private static final Logger logger = LoggerFactory.getLogger(NetGraphPanel.class);
    private final IVisualizer visualizer;
    private final Chart chart;
    private final INet net;
    private final INetInfo netInfo;

    private NetGraphPanel(final Chart chart, INet net, INetInfo netInfo, final IVisualizer visualizer) {
        super(chart);
        if (chart == null) {
            throw new NullPointerException("Chart can not be null!");
        }
        this.visualizer = visualizer;
        this.chart = chart;
        this.net = net;
        this.netInfo = netInfo;

        this.repaint();
    }

    private NetGraphPanel(final INet net, final INetInfo netInfo, final IVisualizer visualizer) {
        this(visualizer.createChart(net, netInfo), net, netInfo, visualizer);
    }

    public NetGraphPanel(final INet net, final INetInfo netInfo) {
        this(net, netInfo, netInfo.getVisualizer());
    }

    @Override
    public void repaint() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    if (chart != null && visualizer != null) {
                        final Collection<Number> actual = chart.getSeriesMap().get(1).getyData();
                        actual.clear();
                        final List<EducationSample> samples = netInfo.getEducationDataSource().getTestData();
                        final double[] actData = visualizer.getY(samples, net);
                        for (final Double denormalized : Util.denormalizeOutputs(actData, netInfo.getNetConfiguration())) {
                            actual.add(denormalized);
                        }
                        if (logger.isTraceEnabled()) {
                            logger.trace("f({})={}", visualizer.getX(samples), actData);
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

    public INetInfo getNetInfo() {
        return netInfo;
    }

    public IVisualizer getVisualizer() {
        return visualizer;
    }
}