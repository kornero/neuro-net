package com.neuronet.view;

import com.neuronet.api.IFunction;
import com.neuronet.api.INet;
import com.neuronet.api.INetBuilder;
import com.neuronet.api.RandomWeight;
import com.neuronet.api.generator.EducationSample;
import com.neuronet.api.generator.INetInfo;
import com.neuronet.impl.NetBuilder;
import com.neuronet.impl.NetLearner;
import com.neuronet.impl.example.ParabolaNetInfo;
import com.neuronet.impl.example.SinNetInfo;
import com.neuronet.impl.functions.BipolarSigmaFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.Random;

public class Example {

    private static final Logger logger = LoggerFactory.getLogger(Example.class);

    public static void main(String[] args) {
        final INetInfo netInfo = new ParabolaNetInfo();
        final NetLearner learner = new NetLearner(createNet(netInfo), netInfo, 0.00051f);
        learner.learn(100000);
    }

    private static INet createNet(final INetInfo netInfo) {
        final INetBuilder netBuilder = new NetBuilder();
        netBuilder.setNetConfiguration(netInfo.getNetConfiguration());
        netBuilder.setNetParameters(new RandomWeight(
                0.05f, //Configuration.DEFAULT_DX,
                0.00051f  //Configuration.DEFAULT_EDUCATION_SPEED,
        ));

        netBuilder.addLayer(1, null); // Input layer.

        final IFunction functionType = BipolarSigmaFunction.getInstance(0.05f);
        netBuilder.addLayer(10, functionType);
        netBuilder.addLayer(4, functionType);
        netBuilder.addLayer(1, functionType);

        return netBuilder.build();
    }

    public static void main1(String[] args) throws InterruptedException {
        final SinNetInfo info = new SinNetInfo();

        final INetBuilder netBuilder = new NetBuilder();
        netBuilder.setNetConfiguration(info.getNetConfiguration());
        netBuilder.setNetParameters(new RandomWeight(
                0.05f, //Configuration.DEFAULT_DX,
                0.00051f  //Configuration.DEFAULT_EDUCATION_SPEED,
        ));

        netBuilder.addLayer(1, null); // Input layer.

        final IFunction functionType = BipolarSigmaFunction.getInstance(0.05f);
        netBuilder.addLayer(10, functionType);
        netBuilder.addLayer(4, functionType);
        netBuilder.addLayer(1, functionType);

        final INet net = netBuilder.build();

        final JFrame frame = new JFrame("XChart");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final NetGraphPanel chartPanel = new NetGraphPanel(net, info);
        frame.add(chartPanel);

        // Display the window.
        frame.pack();
        frame.setVisible(true);


        System.out.println("---------");
        float[] input = new float[1];
        final float[] expected = new float[1];

        final Random random = new Random();
        for (long i = 0; i < Long.MAX_VALUE; i++) {
//            net.educate(new float[]{15 - random.nextFloat() / 10}, new float[]{-0.1f});
//            net.educate(new float[]{10 - random.nextFloat() / 10}, new float[]{0.9f});
//            net.educate(new float[]{5 - random.nextFloat() / 10}, new float[]{-0.1f});
//
//            net.educate(new float[]{-5 + random.nextFloat() / 10}, new float[]{0.1f});
//            net.educate(new float[]{-10 + random.nextFloat() / 10}, new float[]{-0.9f});
//            net.educate(new float[]{-15 + random.nextFloat() / 10}, new float[]{0.1f});

            for (final EducationSample sample : info.getEducationDataSource().getEducationData()) {
                net.educate(sample);
            }

//            for (float j = (float) (-1 * Math.PI * 6); j < Math.PI * 6; j += 0.1) {
//                input[0] = j;
//                expected[0] = (float) Math.sin(j);
//                net.educate(input, expected);
//            }

            if (i % 100 == 0) {
//                Thread.sleep(10);
                chartPanel.getChart().setChartTitle("Step: " + i);
                chartPanel.repaint();
            }
        }
        System.out.println("DONE");  /**/
    }
}