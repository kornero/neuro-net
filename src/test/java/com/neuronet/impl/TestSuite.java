package com.neuronet.impl;

import com.neuronet.api.*;
import com.neuronet.impl.functions.BipolarSigmaFunction;
import com.neuronet.util.Util;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.neuronet.util.Util.randomFloats;

public class TestSuite {

    private static final Logger logger = LoggerFactory.getLogger(TestSuite.class);

    private static INet createMemorizingNet(final int inputs, final int outputs) {
        final INetBuilder netBuilder = new NetBuilder();
        netBuilder.setNetParameters(new NetParameters(
                0.05f,  //Configuration.DEFAULT_DX,
                0.025f, //Configuration.DEFAULT_EDGE_WEIGHT,
                0.01f   //Configuration.DEFAULT_EDUCATION_SPEED,
        ));
        netBuilder.setNetConfiguration(new ImmutableNetConfiguration(inputs, outputs, -1, 1, -1, 1));

        final IFunction functionType = BipolarSigmaFunction.getInstance();

        netBuilder.addLayer(10, functionType);
        netBuilder.addLayer(15, functionType);
        netBuilder.addLayer(150, functionType);
        netBuilder.addLayer(25, functionType);
        netBuilder.addLayer(5, functionType);
        netBuilder.addLayer(outputs, functionType);

        logger.debug("createMemorizingNet(): Net created successful.");

        return netBuilder.build();
    }

    private static INet createSignumNet(final int inputs, final int outputs) {
        final INetBuilder netBuilder = new NetBuilder();
        netBuilder.setNetConfiguration(new ImmutableNetConfiguration(inputs, outputs, -1, 1, -1, 1));

        final IFunction functionType = BipolarSigmaFunction.getInstance();

        netBuilder.addLayer(10, functionType);
        netBuilder.addLayer(5, functionType);
        netBuilder.addLayer(outputs, functionType);

        logger.debug("createSignumNet(): Net created successful.");

        return netBuilder.build();
    }

    private static INet createSinNet() {
        final INetBuilder netBuilder = new NetBuilder();
        netBuilder.setNetConfiguration(new ImmutableNetConfiguration(1, 1, 0, 4 /* pi ~ 3,1415 */, -1, 1));
        netBuilder.setNetParameters(new RandomWeight(
                0.05f, //Configuration.DEFAULT_DX,
                0.00051f  //Configuration.DEFAULT_EDUCATION_SPEED,
        ));

        final IFunction functionType = BipolarSigmaFunction.getInstance(0.05f);
        netBuilder.addLayer(10, functionType);
        netBuilder.addLayer(4, functionType);
        netBuilder.addLayer(1, functionType);

        logger.debug("createSinNet(): Net created successful.");

        return netBuilder.build();
    }

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
            net.educate(randomFloats(inputs), expected);
        }

        final float[] runResult = net.run(randomFloats(inputs));
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
        net.setEducationSpeed(0.5f);

        // sign(+-1) = +-1;
        for (int i = 0; i < 100 * 1000; i++) {
            input = randomFloats(1);
            expected[0] = Math.signum(input[0]);

            net.educate(input, expected);
        }

        // sign(0) = 0;
        input = new float[]{0.0f};
        expected[0] = 0.0f;
        for (int i = 0; i < 1000; i++) {
            net.educate(input, expected);
        }

        for (int i = 0; i < 10; i++) {
            input = randomFloats(1);
            expected[0] = Math.signum(input[0]);
            final float[] runResult = net.run(input);

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

        for (int i = 0; i < 10 * 100; i++) {
            for (float j = 0; j < Math.PI; j += 0.01) {
                input[0] = j;
                expected[0] = (float) Math.sin(input[0]);
                net.educate(input, expected);
            }
        }

        for (float i = 0; i < Math.PI; i += 0.5f) {
            input[0] = i;
            expected[0] = (float) Math.sin(input[0]);
            final float[] runResult = net.run(input);

            float exp = expected[0];
            float act = runResult[0];

            logger.debug("Data: sin({}) = {}, actual = {}",
                    Util.toString(input[0]),
                    Util.toString(exp),
                    Util.toString(act)
            );
//            Assert.assertEquals("Unexpected result.", exp, act, 0.25);
        }
    }
}