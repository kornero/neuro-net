package com.neuronet.impl;

import com.neuronet.api.INet;
import com.neuronet.api.generator.INetInfo;
import com.neuronet.util.GUIUtil;
import com.neuronet.util.Util;
import com.neuronet.view.NetGraphPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VisualNetLearner extends NetLearner {

    private static final int FRAMES_PER_SECOND = 1000 / 24;
    private NetGraphPanel panel;
    private long timeStamp;

    public VisualNetLearner(final INet net, final INetInfo netInfo, final float educationSpeed) {
        super(net, netInfo, educationSpeed);
    }

    @Override
    public void learn(final int learnRoundsThreshold, final float stopLearnError) {
        this.panel = new NetGraphPanel(net, netInfo);
        this.panel.getChart().setChartTitle("Iteration = 0");
        this.timeStamp = System.currentTimeMillis();

        final JButton button = new JButton("Shock");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                shock();
            }
        });
//        this.panel.add(button);

        GUIUtil.createFrame(panel);

        super.learn(learnRoundsThreshold, stopLearnError);
    }

    protected void learnRoundCallback(final int learnRound, final float error) {
        if (System.currentTimeMillis() - timeStamp > FRAMES_PER_SECOND) {
            this.panel.getChart().setChartTitle("" +
                    "Iteration = " + learnRound +
                    ", test error = " + Util.toString(error) +
                    ", speed = " + Util.toString(this.educationSpeed, 4)
            );
            this.panel.repaint();
            this.timeStamp = System.currentTimeMillis();
        }
    }
}