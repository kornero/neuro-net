package com.neuronet.edged;

import com.neuronet.edged.api.INet;
import com.neuronet.util.FunctionType;
import com.neuronet.util.Util;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.neuronet.util.FunctionType.BIPOLAR_SIGMA;
import static com.neuronet.util.FunctionType.GAUSS;
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
        final int inputs = 10;
        final float[] expected = new float[]{-0.9f, -0.8f, 0.75f};
        final INet net = createBig(inputs, expected.length);

        for (int i = 0; i < 1000; i++) {
            net.educate(expected, randomFloats(inputs));
        }

        final float[] runResult = net.runNet(randomFloats(inputs));
        logger.debug("result:" + Util.toString(runResult));
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
                            Util.toString(expected[0]),
                            Util.toString(runResult[0])
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

        for (int i = 0; i < 10 * 1000; i++) {
            input = randomFloats(1, (float) Math.PI);
            expected[0] = (float) Math.sin(input[0]);

            net.educate(expected, input);
        }

        for (int i = 0; i < 10; i++) {
            input = randomFloats(1, (float) Math.PI);
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

        float in = 0;
        float out = net.runNet(new float[]{in})[0];
        logger.debug("SIN({})={}", in, out);

        in = (float) (Math.PI / 6);
        out = net.runNet(new float[]{in})[0];
        logger.debug("SIN({})={}", in, out);

        in = (float) (Math.PI / 4);
        out = net.runNet(new float[]{in})[0];
        logger.debug("SIN({})={}", in, out);

        in = (float) (Math.PI / 3);
        out = net.runNet(new float[]{in})[0];
        logger.debug("SIN({})={}", in, out);

        in = (float) (Math.PI / 2);
        out = net.runNet(new float[]{in})[0];
        logger.debug("SIN({})={}", in, out);

        in = (float) (Math.PI);
        out = net.runNet(new float[]{in})[0];
        logger.debug("SIN({})={}", in, out);
    }

    @Test
    public void debug() {
        for (int i = 0; i < 100; i++) {
//            test_memorizingNet();
            test_signumNet();
        }
    }

    private static INet createBig(final int inputs, final int outputs) {
        final INet net = new Net(inputs);

        final FunctionType functionType = BIPOLAR_SIGMA;

        net.addLayer(10, functionType);
        net.addLayer(15, functionType);
        net.addLayer(150, functionType);
        net.addLayer(25, functionType);
        net.addLayer(5, functionType);
        net.addLayer(outputs, functionType);

        logger.debug("createBig(): Net created successful.");

        ((Net) net).printStatistic();

        return net;
    }

    private static INet createSinNet() {
        final INet net = new Net(1);

        net.addLayer(100, BIPOLAR_SIGMA);
//        net.addLayer(15, BIPOLAR_SIGMA);
//        net.addLayer(500, BIPOLAR_SIGMA);
        net.addLayer(250, GAUSS);
        net.addLayer(50, BIPOLAR_SIGMA);
        net.addLayer(15, BIPOLAR_SIGMA);
        net.addLayer(5, BIPOLAR_SIGMA);
        net.addLayer(2, BIPOLAR_SIGMA);
        net.addLayer(1, BIPOLAR_SIGMA);

        logger.debug("createBig(): Net created successful.");

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

    private static void check(final float expected, final float actual) {
        Assert.assertTrue("Unexpected result: exp=" + expected + ", act=" + actual, Math.abs(expected - actual) < 0.25);
    }
}