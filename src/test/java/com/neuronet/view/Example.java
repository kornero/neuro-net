package com.neuronet.view;

import com.neuronet.api.INet;
import com.neuronet.impl.example.SinNetInfo;
import com.neuronet.util.Util;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.SeriesColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;

public class Example {

    private static final Logger logger = LoggerFactory.getLogger(Example.class);

    public static void main(String[] args) throws InterruptedException {
        final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\nets\\sinus_net\\sin.date[1383593252607].error[0.4962418].net");
//
        final INet net = Util.deserialize(file);
        Chart chart = Visualizer.createChart(net, new SinNetInfo());
        JFrame frame = Visualizer.createFrame(chart);

        System.out.println("---------");
        for (int i = 0; i < 5; i++) {

            chart.setChartTitle("---" + i + "---");
            frame.repaint();
            System.out.println("---" + i + "---");
            Thread.sleep(1000);
            chart.getSeriesMap().get(1).setLineColor(SeriesColor.BLACK);
        }
    }
}