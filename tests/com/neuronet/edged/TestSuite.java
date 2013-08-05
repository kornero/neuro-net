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

        for (int tries = 0; tries < 500; tries++) {
            final int layers = random.nextInt(3) + 1;
            final INet net = new Net(inputs, new RandomConfiguration());
            for (int i = 1; i <= layers; i++) {
                final int neurons = random.nextInt(500 / (i * i)) + 1;
                net.addLayer(neurons, getRandomFunctionType());
            }
            net.addLayer(outputs, getRandomFunctionType());
            if (examineNet(net, inputs)) {
                educateSin(net);
                if (examineNet(net, inputs) && examineSin(net)) {
                    printSinValues(net);
                    final File file = new File("C:\\Users\\Sasha\\IdeaProjects\\neuro_net_project\\sin." + tries + ".net");
                    if (file.createNewFile()) {
                        Util.serialize(net, file);
                        System.out.println(Util.summary(net));
                        break;
                    }
                }
            }
        }
        System.out.println(errors.pollFirst() + "<->" + errors.pollLast());
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
            if (examineNet(net, inputs)) {
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
        float[] input;
        final float[] expected = new float[1];
        for (int i = 0; i < 1000; i++) {
            input = randomFloats(2, (float) (Math.PI * 2));
            input[1] = (float) (Math.PI * 2);

            expected[0] = (float) Math.sin(input[0]);

            net.educate(expected, input);
        }
    }

    private static final NavigableSet<Float> errors = new TreeSet<Float>();

    private static boolean examineSin(final INet net) {
        float[] input;
        final float[] expected = new float[1];

        float error = 0.0f;
        for (int i = 0; i < 10; i++) {
            input = randomFloats(2);
            input[1] = (float) (Math.PI * 2);
            expected[0] = (float) Math.sin(input[0]);
            final float[] runResult = net.runNet(input);

            float exp = expected[0];
            float act = runResult[0];

            error += Math.abs(exp - act);
        }
        errors.add(error);
        return error < 2.5f;
    }

    private static boolean examineNet(final INet net, final int in) {
        final Set<Integer> roundedResults = new HashSet<Integer>();
        for (int i = 0; i < 10; i++) {
            final float act = net.runNet(randomFloats(in))[0];
            final int round = (int) (1000 * act);
            roundedResults.add(round);
        }
        return roundedResults.size() == 10;
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
}