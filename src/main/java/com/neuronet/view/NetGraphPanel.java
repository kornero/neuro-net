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

import static com.neuronet.view.Visualizer.createChart;

public class NetGraphPanel extends XChartPanel {

    private static final Logger logger = LoggerFactory.getLogger(NetGraphPanel.class);
    private final Chart chart;
    private final INet net;
    private final INetInfo netInfo;

    private NetGraphPanel(final Chart chart, INet net, INetInfo netInfo) {
        super(chart);
        if (chart == null) {
            throw new NullPointerException("Chart can not be null!");
        }
        this.chart = chart;
        this.net = net;
        this.netInfo = netInfo;

        this.repaint();
    }

    public NetGraphPanel(final INet net, final INetInfo netInfo) {
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
                        for (final EducationSample sample : netInfo.getEducationDataSource().getTestData()) {
                            actual.add(Util.denormalizeOutputs(net.run(sample.getInputsSample()), netInfo.getNetConfiguration())[0]);

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

    public INetInfo getNetInfo() {
        return netInfo;
    }
}