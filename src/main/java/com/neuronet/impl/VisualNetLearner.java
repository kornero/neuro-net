package com.neuronet.impl;

import com.neuronet.api.INet;
import com.neuronet.api.generator.INetInfo;
import com.neuronet.util.Util;
import com.neuronet.view.NetGraphPanel;
import com.neuronet.view.Visualizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VisualNetLearner extends NetLearner {

    private static final Logger logger = LoggerFactory.getLogger(VisualNetLearner.class);
    private static final int FRAMES_PER_SECOND = 1000 / 24;
    private NetGraphPanel panel;
    private long timeStamp;

    public VisualNetLearner(INet net, INetInfo netInfo, float educationSpeed) {
        super(net, netInfo, educationSpeed);
    }

    @Override
    public void learn(final int learnRoundsThreshold, final float stopLearnError) {
        this.panel = new NetGraphPanel(net, netInfo);
        this.panel.getChart().setChartTitle("Iteration = 0");
        this.timeStamp = System.currentTimeMillis();

        Visualizer.createFrame(panel);

        super.learn(learnRoundsThreshold, stopLearnError);
    }

    protected void learnRoundCallback(final int learnRound, final float error) {
        if (System.currentTimeMillis() - timeStamp > FRAMES_PER_SECOND) {
            this.panel.getChart().setChartTitle("Iteration = " + learnRound + ", test error = " + Util.toString(error));
            this.panel.repaint();
            this.timeStamp = System.currentTimeMillis();
        }
    }
}