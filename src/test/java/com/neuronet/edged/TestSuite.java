package com.neuronet.edged;

import com.neuronet.edged.api.Configuration;
import com.neuronet.edged.api.INet;
import com.neuronet.edged.api.RandomWeight;
import com.neuronet.example.SinNetInfo;
import com.neuronet.generator.NetGenerator;
import com.neuronet.util.FunctionType;
import com.neuronet.util.Util;
import org.junit.Assert;
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

import static com.neuronet.util.FunctionType.BINARY_SIGMA;
import static com.neuronet.util.FunctionType.BIPOLAR_SIGMA;
import static com.neuronet.util.Util.randomFloats;

public class TestSuite {

    private static final Logger logger = LoggerFactory.getLogger(TestSuite.class);

    /**
     * Trivial memorizing net.
     */
    @Test
    public void test_memorizingNet() {
        logger.info("NeuroNet is starting.");
        final int inputs = 3;
        final float[] expected = new float[]{-0.9f, -0.8f, 0.75f};
        final INet net = createMemorizingNet(inputs, expected.length);

        for (int i = 0; i < 1000; i++) {
            net.educate(expected, randomFloats(inputs));
        }

        final float[] runResult = net.runNet(randomFloats(inputs));
        logger.debug("result:{}", Util.toString(runResult));
        for (int i = 0, expectedLength = expected.length; i < expectedLength; i++) {
            float exp = expected[i];
            float act = runResult[i];
            Assert.assertEquals("Unexpected result.", exp, act, 0.25);
        }
    }

    /**
     * Trivial net for signum function:
     * <pre>
     * sign(x):
     *      x > 0 ->  1
     *      x = 0 ->  0
     *      x < 0 -> -1
     * </pre>
     */
    @Test
    public void test_signumNet() {
        logger.info("NeuroNet is starting.");
        float[] input;
        final float[] expected = new float[1];
        final INet net = createSignumNet(1, 1);

        // sign(+-1) = +-1;
        for (int i = 0; i < 10000; i++) {
            input = randomFloats(1);
            expected[0] = Math.signum(input[0]);

            net.educate(expected, input);
        }

        // sign(0) = 0;
        input = new float[]{0.0f};
        expected[0] = 0.0f;
        for (int i = 0; i < 1000; i++) {
            net.educate(expected, input);
        }

        for (int i = 0; i < 10; i++) {
            input = randomFloats(1);
            expected[0] = Math.signum(input[0]);
            final float[] runResult = net.runNet(input);

            float exp = expected[0];
            float act = runResult[0];

            logger.debug("Data: sign({}) = {}, actual = {}",
                    Util.toString(input[0]),
                    Util.toString(exp),
                    Util.toString(act)
            );
            Assert.assertEquals("Unexpected result.", exp, act, 0.25);
        }
    }

    @Ignore("Implement checks")
    @Test
    public void test_sinusNet() {
        logger.info("NeuroNet is starting.");
        float[] input = new float[1];
        final float[] expected = new float[1];
        final INet net = createSinNet();

        for (int i = 0; i < 100; i++) {
            for (float j = 0; j < Math.PI; j += 0.1) {
                input[0] = j;
                expected[0] = (float) Math.sin(input[0]);
                net.educate(expected, input);
            }
        }

        for (int i = 0; i < 10; i++) {
            input = randomFloats(1, (float) Math.PI * 2);
            expected[0] = (float) Math.sin(input[0]);
            final float[] runResult = net.runNet(input);

            float exp = expected[0];
            float act = runResult[0];

            logger.debug("Data: sin({}) = {}, actual = {}",
                    Util.toString(input[0]),
                    Util.toString(exp),
                    Util.toString(act)
            );
//            check(exp, act);
        }
        printSinValues(net);
        printSinGraph(net);
    }

    @Ignore("For debug only")
    @Test
    public void sinNetGenerator() throws IOException {
        final NavigableMap<Float, INet> nets = NetGenerator.generateNet(new SinNetInfo(), 5, 10, 5, TimeUnit.MINUTES);
        logger.info(nets.toString());

        for (int i = 0; i < Math.min(10, nets.size()); i++) {
            Map.Entry<Float, INet> bestNet = nets.pollFirstEntry();
            logger.info("#{} : {}", i, bestNet.getKey());
            logger.info(Util.summary(bestNet.getValue()));

            final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\" +
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

    private static INet createMemorizingNet(final int inputs, final int outputs) {
        final INet net = new Net(inputs, new Configuration(
                0.001f,//Configuration.DEFAULT_ALFA,
                0.0025f,//Configuration.DEFAULT_DX,
                0.0025f,//Configuration.DEFAULT_EDGE_WEIGHT,
                Configuration.EDUCATION_SPEED)
        );

        final FunctionType functionType = BIPOLAR_SIGMA;

        net.addLayer(10, functionType);
        net.addLayer(15, functionType);
        net.addLayer(150, functionType);
        net.addLayer(25, functionType);
        net.addLayer(5, functionType);
        net.addLayer(outputs, functionType);

        logger.debug("createMemorizingNet(): Net created successful.");

        ((Net) net).printStatistic();

        return net;
    }

    private static INet createSignumNet(final int inputs, final int outputs) {
        final INet net = new Net(inputs);

        final FunctionType functionType = BIPOLAR_SIGMA;

        net.addLayer(10, functionType);
        net.addLayer(5, functionType);
        net.addLayer(outputs, functionType);

        logger.debug("createSignumNet(): Net created successful.");

        ((Net) net).printStatistic();

        return net;
    }

    private static INet createSinNet() {
        final INet net = new Net(1, (float) Math.PI,
                new RandomWeight(
                        0.18f, //Configuration.DEFAULT_ALFA,
                        0.15f,//Configuration.DEFAULT_DX,
                        0.1f  //Configuration.EDUCATION_SPEED
                )
//                new RandomConfiguration()
        );

        net.addLayer(25, BINARY_SIGMA);
        net.addLayer(1, BINARY_SIGMA);

        logger.debug("createSinNet(): Net created successful.");

        return net;
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