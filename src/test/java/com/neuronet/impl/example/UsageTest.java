package com.neuronet.impl.example;

import com.neuronet.api.INet;
import com.neuronet.api.generator.NetGenerator;
import com.neuronet.util.Util;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.TimeUnit;

public class UsageTest {

    private static final Logger logger = LoggerFactory.getLogger(UsageTest.class);

    @Ignore("For debug only")
    @Test
    public void sinNetGenerator() throws IOException {
        final NetGenerator netGenerator = new NetGenerator(new SinNetInfo(), 50);
        final NavigableMap<Float, INet> nets = netGenerator.generateNet(1, 50f, 5, TimeUnit.MINUTES);

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

    private static void printSinValues(final INet net) {
        final float[] in = new float[]{0};
        in[0] = 0.0f;
        float out = net.run(in)[0];
        logger.debug("SIN({})={}", in[0], out);

        in[0] = (float) (Math.PI / 6);
        out = net.run(in)[0];
        logger.debug("SIN({})={}", in[0], out);

        in[0] = (float) (Math.PI / 4);
        out = net.run(in)[0];
        logger.debug("SIN({})={}", in[0], out);

        in[0] = (float) (Math.PI / 3);
        out = net.run(in)[0];
        logger.debug("SIN({})={}", in[0], out);

        in[0] = (float) (Math.PI / 2);
        out = net.run(in)[0];
        logger.debug("SIN({})={}", in[0], out);

        in[0] = (float) (Math.PI);
        out = net.run(in)[0];
        logger.debug("SIN({})={}", in[0], out);

        in[0] = (float) (Math.PI * 2);
        out = net.run(in)[0];
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

            final float act = net.run(input)[0];

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