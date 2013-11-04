package com.neuronet.impl.example;

import com.neuronet.api.INet;
import com.neuronet.api.generator.NetGenerator;
import com.neuronet.api.generator.NetInfo;
import com.neuronet.util.Util;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.TimeUnit;

public class UsageTest {

    private static final Logger logger = LoggerFactory.getLogger(UsageTest.class);

    @Ignore("For debug only")
    @Test
    public void sinNetGenerator() throws IOException {
        final NavigableMap<Float, INet> nets = NetGenerator.generateNet(new SinNetInfo(), 0.22f, 1, TimeUnit.MINUTES);

        for (int i = 0; i < Math.min(10, nets.size()); i++) {
            Map.Entry<Float, INet> bestNet = nets.pollFirstEntry();
            logger.info("#{} : {}", i, bestNet.getKey());
            logger.info(Util.summary(bestNet.getValue()));

            final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\nets\\sinus_net\\" +
                    "sin.date[" + System.currentTimeMillis() + "].error[" + bestNet.getKey() + "].net");
            if (file.createNewFile()) {
                Util.serialize(bestNet.getValue(), file);
            }
        }
    }

    @Ignore("Debug")
    @Test
    public void debug() {
        final int iterations = 10 * 10;
        final NetInfo netInfo = new SinNetInfo();
        final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\nets\\sinus_net\\sin.1380482643721.net");
//        final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\nets\\sinus_net\\" +
//                "sin.date[1380484558977].error[0.21036892].net");

        final INet net = Util.deserialize(file);
        net.setEducationSpeed(0.001f);

        printSinValues(net);
        printSinGraph(net);

        logger.debug("Error before learning: {}", NetGenerator.examineNet(net, netInfo, 0));
        for (int i = 0; i < iterations; i++) {
            if (i % (iterations / 10) == 0) {
                logger.debug("Iterations left: {}", iterations - i);
            }
            NetGenerator.educateNet(net, netInfo, i);
        }
        logger.debug("Error after learning: {}", NetGenerator.examineNet(net, netInfo, 0));

        printSinValues(net);
        printSinGraph(net);
    }

    @Ignore("For debug only")
    @Test
    public void generateImageNet() throws IOException {
        final NavigableMap<Float, INet> nets = NetGenerator.generateNet(new SimpleImageNetInfo(), 5, 10, 1, TimeUnit.MINUTES);

        for (int i = 0; i < Math.min(10, nets.size()); i++) {
            Map.Entry<Float, INet> bestNet = nets.pollFirstEntry();
            logger.info("#{} : {}", i, bestNet.getKey());
            logger.info(Util.summary(bestNet.getValue()));

            final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\nets\\image_net\\" +
                    "image." + System.currentTimeMillis() + ".net");
            if (file.createNewFile()) {
                Util.serialize(bestNet.getValue(), file);
            }
        }
    }

    @Ignore("Debug")
    @Test
    public void debug2() {
        final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\nets\\image_net\\image.1380135053104.net");
        final NetInfo netInfo = new SimpleImageNetInfo();

        final INet net = Util.deserialize(file);
        net.setEducationSpeed(0.0001f);
        for (int i = 0; i < 5000; i++) {
            NetGenerator.educateNet(net, netInfo, i);
        }

        System.out.println(Arrays.toString(net.runNet(new float[]{
                1f, 0f, 1f,
                1f, 0f, 1f,
                1f, 1f, 1f,
                1f, 0f, 1f,
                1f, 0f, 1f,
        })));
        System.out.println(Arrays.toString(net.runNet(new float[]{
                1f, 0f, 1f,
                1f, 0f, 1f,
                1f, 1f, 1f,
                0f, 0f, 1f,
                0f, 0f, 1f,
        })));
    }

    private static void printSinValues(final INet net) {
        final float[] in = new float[]{0};
        in[0] = 0.0f;
        float out = net.runNet(in)[0];
        logger.debug("SIN({})={}", in[0], out);

        in[0] = (float) (Math.PI / 6);
        out = net.runNet(in)[0];
        logger.debug("SIN({})={}", in[0], out);

        in[0] = (float) (Math.PI / 4);
        out = net.runNet(in)[0];
        logger.debug("SIN({})={}", in[0], out);

        in[0] = (float) (Math.PI / 3);
        out = net.runNet(in)[0];
        logger.debug("SIN({})={}", in[0], out);

        in[0] = (float) (Math.PI / 2);
        out = net.runNet(in)[0];
        logger.debug("SIN({})={}", in[0], out);

        in[0] = (float) (Math.PI);
        out = net.runNet(in)[0];
        logger.debug("SIN({})={}", in[0], out);

        in[0] = (float) (Math.PI * 2);
        out = net.runNet(in)[0];
        logger.debug("SIN({})={}", in[0], out);
    }

    private static void printSinGraph(final INet net) {
        final int limitX = 20;
        final int limitY = 32;
        final float step = 0.1f;
        final String dot = "  ";
        final String mark = "*";

        final float[] input = new float[]{0};

        for (float i = -1 * limitY * step; i < limitY * step; i += step) {
            input[0] = i;

            final float act = net.runNet(input)[0];

            if (act < -1 * limitX * step) {                     // Print value mark before interval.
                System.out.print(mark);
            }
            for (float j = -1 * limitX * step; j < limitX * step; j += step) {
                if (Util.equals(j, 0.0f)) {                     // Print X: sin(x) = y
                    if (i >= 0) {
                        System.out.print("+");
                    }
                    System.out.print(Util.toString(i));
                } else if (j <= act && act <= j + step) {       // Print value mark inside interval.
                    System.out.print(mark);
                } else {                                        // Print free space.
                    System.out.print(dot);
                }
            }
            if (limitX * step < act) {                         // Print value mark after interval.
                System.out.print(mark);
            }
            System.out.println(" (" + Util.toString(act) + ")"); // Print Y: sin(x) = y
        }
    }
}