package com.neuronet.edged;

import com.neuronet.edged.api.INet;
import com.neuronet.util.FunctionType;
import com.neuronet.util.Util;
import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.neuronet.util.Util.randomWeights;

public class TestSuite {

    private static final Logger logger = LoggerFactory.getLogger(TestSuite.class);

    /**
     * Trivial memorizing net.
     */
    @Test()
    public void test1() {
        logger.info("NeuroNet is starting.");
        float[] input = getData();
        final float[] expected = new float[]{-1, -1, 1};
        final INet net = createBig(input.length, expected.length);

        for (int i = 0; i < 100; i++) {
            input = randomWeights(3);
            net.educate(expected, input);
        }

        input = randomWeights(3);
        final float[] runResult = net.runNet(input);
        logger.debug("result:" + Util.toString(runResult));
        for (int i = 0, expectedLength = expected.length; i < expectedLength; i++) {
            float exp = expected[i];
            float act = runResult[i];
            Assert.assertTrue(Math.abs(exp - act) < 0.1);
        }
    }


    /**
     * Trivial net for signum function.
     */
    @Test()
    public void test2() {
        logger.info("NeuroNet is starting.");
        float[] input;
        final float[] expected = new float[1];
        final INet net = createSmall(1, 1);

        for (int i = 0; i < 100; i++) {
            input = randomWeights(1);
            expected[0] = Math.signum(input[0]);
            net.educate(expected, input);
        }

        for (int i = 0; i < 10; i++) {
            input = randomWeights(1);
            expected[0] = Math.signum(input[0]);
            final float[] runResult = net.runNet(input);

            float exp = expected[0];
            float act = runResult[0];
            Assert.assertTrue(Math.abs(exp - act) < 0.1);
            logger.trace("Data: " + Util.toString(input[0]) + ", expected = " + expected[0] + ", actual = " + Util.toString(runResult[0]));
        }
    }

    private static float[] getData() {
        //return new float[]{1, 2, 3};
        return randomWeights(3);
    }

    private static INet createBig(final int inputs, final int outputs) {
        final INet net = new Net(inputs);

        final FunctionType functionType = FunctionType.BIPOLAR_SIGMA;

        final int multiplexer = 100;

        net.addLayer(10, functionType, 0.0005f * multiplexer);
        net.addLayer(15, functionType, 0.0001f * multiplexer);
        net.addLayer(150, functionType, 0.0001f * multiplexer);
        net.addLayer(25, functionType, 0.0001f * multiplexer);
        net.addLayer(5, functionType, 0.0001f * multiplexer);
        net.addLayer(outputs, functionType, 0.0001f * multiplexer);

        logger.debug("createBig(): Net created successful.");

        ((Net) net).printStatistic();

        return net;
    }

    private static INet createSmall(final int inputs, final int outputs) {
        final INet net = new Net(inputs);

        final FunctionType functionType = FunctionType.BIPOLAR_SIGMA;

        final int multiplexer = 100;

        net.addLayer(10, functionType, 0.0005f * multiplexer);
        net.addLayer(5, functionType, 0.0001f * multiplexer);
        net.addLayer(outputs, functionType, 0.0001f * multiplexer);

        logger.debug("createSmall(): Net created successful.");

        ((Net) net).printStatistic();

        return net;
    }
}
