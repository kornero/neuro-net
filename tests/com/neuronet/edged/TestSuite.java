package com.neuronet.edged;

import com.neuronet.edged.api.Configuration;
import com.neuronet.edged.api.INet;
import com.neuronet.edged.api.RandomConfiguration;
import com.neuronet.util.FunctionType;
import com.neuronet.util.Util;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.neuronet.util.FunctionType.*;
import static com.neuronet.util.Util.randomFloats;

public class TestSuite {

    private static final Logger logger = LoggerFactory.getLogger(TestSuite.class);

    static {
    }

    /**
     * Trivial memorizing net.
     */
    @Test()
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
            check(exp, act);
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
    @Test()
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
                    new String[]{
                            Util.toString(input[0]),
                            Util.toString(exp),
                            Util.toString(act)
                    }
            );
            check(exp, act);
        }
    }

    @Test()
    public void test_sinusNet() {
        logger.info("NeuroNet is starting.");
        float[] input;
        final float[] expected = new float[1];
        final INet net = createSinNet();

        for (int i = 0; i < -10 * 1000; i++) {
            input = randomFloats(1, (float) Math.PI * 2);
            expected[0] = (float) Math.sin(input[0]);

            net.educate(expected, input);
        }

        for (int i = 0; i < 10; i++) {
            input = randomFloats(1, (float) Math.PI * 2);
            expected[0] = (float) Math.sin(input[0]);
            final float[] runResult = net.runNet(input);

            float exp = expected[0];
            float act = runResult[0];

            logger.debug("Data: sin({}) = {}, actual = {}",
                    new String[]{
                            Util.toString(input[0]),
                            Util.toString(exp),
                            Util.toString(act)
                    }
            );
//            check(exp, act);
        }
        printSinValues(net);
    }

    @Test
    public void debug() {
        for (int i = 0; i < 100; i++) {
            test_memorizingNet();
//            test_signumNet();
        }
    }

    @Test
    public void sinNetGenerator() throws IOException {
        final Random random = new Random();
        final int inputs = 2;
        final int outputs = 1;
        final int educateRounds = 25;
        final int maxLayers = 2;
        final int maxNeurons = 200;
        final float maxError = 7.0f;

        for (int tries = 0; tries < 5000; tries++) {
            if (tries % 500 == 0) {
                System.out.println(tries);
            }
            final int layers = 2;//random.nextInt(maxLayers - 1) + 1;
            final INet net = new Net(inputs, new RandomConfiguration());
            for (int i = 1; i <= layers; i++) {
                final int neurons = random.nextInt(maxNeurons - 1) + 1;
                net.addLayer(neurons, getRandomFunctionType());
            }
            net.addLayer(outputs, getRandomFunctionType());

            if (examineNet(net)) {
                for (int i = 0; i < educateRounds; i++) {
                    educateSin(net);
                }
                if (examineNet(net)) {
                    final float error = examineSin(net);
                    if (error < 10.0f) {
                        System.out.println("10.0:" + Util.summary(net));
                    }
                    if (error < maxError) {
                        printSinValues(net);
                        final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\" +
                                "sin." + System.currentTimeMillis() + ".net");
                        if (file.createNewFile()) {
                            Util.serialize(net, file);
                            System.out.println(Util.summary(net));
                        }
                    }
                }
            }
        }
        System.out.println(errors.pollFirst() + "<->" + errors.pollLast());
    }

    @Test
    public void sinNetTest() {
        final INet net = Util.deserialize(new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\sin.2271.net"));

        for (int i = 0; i < 1500; i++) {
            educateSin(net);
        }

        printSinValues(net);
        printSinGraph(net);
    }

    @Test
    public void netGenerator() {
        final Random random = new Random();
        final int inputs = 1;
        final int outputs = 1;

        for (int tries = 0; tries < 5000; tries++) {
            if (tries % 500 == 0) {
                System.out.println(tries / 500);
            }
            final int layers = random.nextInt(5);
            final INet net = new Net(inputs, new RandomConfiguration());
            for (int i = 1; i <= layers; i++) {
                final int neurons = random.nextInt(150) + 1;
                net.addLayer(neurons, getRandomFunctionType());
            }
            net.addLayer(outputs, getRandomFunctionType());

//            educateSin(net);
            if (examineNet(net)) {
                printSinValues(net);
            }
        }
//        System.out.println(errors.pollFirst() + "<->" + errors.pollLast());
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
        final INet net = new Net(1,
//                new Configuration(
//                        0.0005f, //Configuration.DEFAULT_ALFA,
//                        0.05f,//Configuration.DEFAULT_DX,
//                        0.05f,//Configuration.DEFAULT_EDGE_WEIGHT,
//                        0.001f  //Configuration.EDUCATION_SPEED
//                )
                new RandomConfiguration()
        );

        net.addLayer(250, BIPOLAR);
        net.addLayer(150, BIPOLAR_SIGMA);
//        net.addLayer(500, BIPOLAR_SIGMA);
//        net.addLayer(250, GAUSS);
        net.addLayer(100, BIPOLAR_SIGMA);
        net.addLayer(15, BIPOLAR_SIGMA);
        net.addLayer(5, BIPOLAR_SIGMA);
//        net.addLayer(2, BIPOLAR_SIGMA);
        net.addLayer(1, BIPOLAR_SIGMA);

        logger.debug("createMemorizingNet(): Net created successful.");

        ((Net) net).printStatistic();

        return net;
    }

    private static void check(final float expected, final float actual) {
        Assert.assertTrue("Unexpected result: exp=" + expected + ", act=" + actual, Math.abs(expected - actual) < 0.25);
    }

    private static void educateSin(final INet net) {
        final Random random = new Random();
        final float[] input = new float[]{0, (float) (Math.PI * 2)};
        final float[] expected = new float[1];
        final float start = 0.0f;
        final float end = (float) Math.PI;

        for (float j = start; j < end; j += random.nextFloat()) {
            input[0] = j;

            expected[0] = (float) Math.sin(j);

            for (int i = 0; i < 10; i++) {
                net.educate(expected, input);
            }
        }
    }

    private static final NavigableSet<Float> errors = new TreeSet<Float>();

    private static float examineSin(final INet net) {
        final float[] input = new float[]{0, (float) (Math.PI * 2)};
        final float[] expected = new float[1];

        float error = 0.0f;

        final float start = 0.0f;
        final float end = (float) Math.PI;

        for (float j = start; j < end; j += 0.1f) {
            input[0] = j;

            expected[0] = (float) Math.sin(j);

            final float[] runResult = net.runNet(input);

            float exp = expected[0];
            float act = runResult[0];

            error += Math.abs(exp - act);
        }
        errors.add(error);
        return error;
    }

    private static boolean examineNet(final INet net) {
        final int checks = 5;
        final Set<Integer> roundedResults = new HashSet<Integer>();
        final int in = net.getInputsAmount();
        for (int i = 0; i < checks; i++) {
            final float act = net.runNet(randomFloats(in))[0];
            final int round = (int) (1000 * act);
            roundedResults.add(round);
        }
        return roundedResults.size() == checks;
    }

    private static void printSinValues(final INet net) {
        final float[] in = new float[]{0, (float) (Math.PI * 2)};
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
        final String dot = "_";
        final String mark = "*";

        final float[] input = new float[]{0, (float) (Math.PI * 2)};

        for (float i = -1 * limitY * step; i < limitY * step; i += step) {
            input[0] = i;
            final float act = net.runNet(input)[0];

            for (float j = -1 * limitX * step; j < limitX * step; j += step) {
                if (Util.equals(j, 0.0f)) {
                    if (act > 0) {
                        System.out.print("+");
                    }
                    System.out.print(Util.toString(i));
                } else if (j <= act && act <= j + step) {
                    System.out.print(mark);
                } else {
                    System.out.print(dot);
                }
            }
            System.out.println("(" + Util.toString(act) + ")");
        }
    }
}