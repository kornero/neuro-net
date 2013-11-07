package com.neuronet.view;

import com.neuronet.api.IFunction;
import com.neuronet.api.INet;
import com.neuronet.api.RandomWeight;
import com.neuronet.impl.Net;
import com.neuronet.impl.example.SinNetInfo;
import com.neuronet.impl.functions.BipolarSigmaFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class Example {

    private static final Logger logger = LoggerFactory.getLogger(Example.class);

    public static void main(String[] args) throws InterruptedException {
        final INet net = new Net(1, (float) (Math.PI * 6),
                new RandomWeight(
                        0.15f, //Configuration.DEFAULT_DX,
                        0.0015f  //Configuration.DEFAULT_EDUCATION_SPEED,
                )
//                new RandomConfiguration()
        );

        final IFunction functionType = BipolarSigmaFunction.getInstance(1.0f);
        net.addLayer(4, functionType);
        net.addLayer(4, functionType);
        net.addLayer(1, functionType);

        JFrame frame = new JFrame("XChart");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        NetGraphPanel chartPanel = new NetGraphPanel(net, new SinNetInfo());
        frame.add(chartPanel);

        // Display the window.
        frame.pack();
        frame.setVisible(true);


        System.out.println("---------");
        float[] input = new float[1];
        final float[] expected = new float[1];

        for (int i = 0; i < 2500000; i++) {
            if (i > 0) {
                net.educate(new float[]{15}, new float[]{-0.9f});
                net.educate(new float[]{10}, new float[]{0.9f});
                net.educate(new float[]{5}, new float[]{-0.9f});
//                net.educate(new float[]{0},new float[]{0.9f});
                net.educate(new float[]{-5}, new float[]{-0.9f});
                net.educate(new float[]{-10}, new float[]{0.9f});
                net.educate(new float[]{-15}, new float[]{-0.9f});
            } else
                for (float j = (float) (-1 * Math.PI * 6); j < Math.PI * 6; j += 0.1) {
                    input[0] = j;
                    expected[0] = (float) Math.sin(j);
                    net.educate(input, expected);
                }
            if (i % 50 == 0) {
                chartPanel.getChart().setChartTitle("Step: " + i);
                chartPanel.repaint();
            }
        }
        System.out.println("DONE");  /**/
    }
}