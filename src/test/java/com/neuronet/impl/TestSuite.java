package com.neuronet.impl;

import com.neuronet.api.Configuration;
import com.neuronet.api.INet;
import com.neuronet.api.RandomWeight;
import com.neuronet.util.FunctionType;
import com.neuronet.util.Util;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
//            check(exp, act);    // TODO
        }
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
}