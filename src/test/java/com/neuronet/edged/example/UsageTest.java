package com.neuronet.edged.example;

import com.neuronet.edged.api.INet;
import com.neuronet.generator.NetGenerator;
import com.neuronet.util.Util;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class UsageTest {

    private static final Logger logger = LoggerFactory.getLogger(UsageTest.class);

    @Ignore("For debug only")
    @Test
    public void sinNetGenerator() throws IOException {
        final NavigableMap<Float, INet> nets = NetGenerator.generateNet(new SinNetInfo(), 5, 10, 5, TimeUnit.MINUTES);
        logger.info(nets.toString());

        for (int i = 0; i < Math.min(10, nets.size()); i++) {
            Map.Entry<Float, INet> bestNet = nets.pollFirstEntry();
            logger.info("#{} : {}", i, bestNet.getKey());
            logger.info(Util.summary(bestNet.getValue()));

            final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\nets\\sinus_net\\" +
                    "sin." + System.currentTimeMillis() + ".net");
            if (file.createNewFile()) {
                Util.serialize(bestNet.getValue(), file);
            }
        }
    }

    @Ignore("Debug")
    @Test
    public void debug() {
        final int iterations = 10 * 1000;
        final Random random = new Random();

//        final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\sin.1379863442168.net");
        final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\sin.1379863442314.net");
//        final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\sin.1379863442469.net");
//        final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\sin.1379863442554.net");
//        final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\sin.1379863443110.net");
//        final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\sin.1379863443255.net");

        final INet net = Util.deserialize(file);
        net.setEducationSpeed(0.01f);
        printSinValues(net);

        final float[] input = new float[1];
        final float[] expected = new float[1];
        for (int i = 0; i < iterations; i++) {
            if (i % (iterations / 20) == 0) {
                logger.debug("Iterations left: {}", iterations - i);
//                printSinValues(net);
            }
            for (float j = 0; j < Math.PI * 2; j += random.nextFloat()) {
                input[0] = j;
                expected[0] = (float) Math.sin(input[0]);
                net.educate(expected, input);
            }
        }

        printSinValues(net);
        printSinGraph(net);
    }

    @Ignore("For debug only")
    @Test
    public void generateImageNet() throws IOException {
        final NavigableMap<Float, INet> nets = NetGenerator.generateNet(new SimpleImageNetInfo(), 5, 10, 2, TimeUnit.MINUTES);
        logger.info(nets.toString());

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