package com.neuronet.edged;

import com.neuronet.edged.api.INet;
import com.neuronet.util.FunctionType;
import com.neuronet.util.Util;
import junit.framework.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.neuronet.util.Util.randomWeights;

public class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    @org.junit.Test
    public void test() {
        logger.info("NeuroNet is starting.");
        float[] input = getData();
        final INet net = create(input.length);
        final float[] expected = new float[]{-1, -1, 1};

        for (int i = 0; i < 10; i++) {
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

    private static float[] getData() {
        //return new float[]{1, 2, 3};
        return randomWeights(3);
    }

    private static INet create(final int inputs) {
        final INet net = new com.neuronet.edged.Net(inputs);

        final FunctionType functionType = FunctionType.BIPOLAR_SIGMA;

        final int multiplexer = 100;

        net.addLayer(10, functionType, 0.0005f * multiplexer);
        net.addLayer(5, functionType, 0.0001f * multiplexer);
        net.addLayer(3, functionType, 0.0001f * multiplexer);

        logger.debug("create(): Net created successful.");

        return net;
    }
}
