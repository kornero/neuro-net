package com.neuronet;

import com.neuronet.common.api.INet;
import com.neuronet.util.FunctionType;
import com.neuronet.util.Functions;
import junit.framework.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    @org.junit.Test
    public void test() {
        logger.info("NeuroNet is starting.");
        float[] input = getData();
        final INet net = create(input);
        final float[] expected = new float[]{-1, -1, 1};

        for (int i = 0; i < 10; i++) {
            input = Functions.randomWeights(3);
            normalize(input);
            final float[] runResult = net.runNet(input);

            logger.debug("run:" + Arrays.toString(runResult));
            net.educate(expected, input);
        }

        input = Functions.randomWeights(3);
        normalize(input);
        final float[] runResult = net.runNet(input);
        for (int i = 0, expectedLength = expected.length; i < expectedLength; i++) {
            float exp = expected[i];
            float act = runResult[i];
            Assert.assertTrue(Math.abs(exp - act) < 0.1);
        }
    }

    private static void normalize(final float[] inputData) {

        //  находим норму:
        final float norma = getNorm(inputData);

        //  нормируем:
        for (int i = 0; i < inputData.length; i++) {
            inputData[i] = (inputData[i]) / norma;
        }
    }

    private static float[] getData() {
        return new float[]{1, 2, 3};
    }

    private static float getNorm(float[] floats) {

        //  находим норму:
        float sum = 0;
        for (final float d : floats) {
            sum += d * d;
        }
        return (float) Math.sqrt(sum);
    }

    private static INet create(final float[] inputData) {
        final INet net = new com.neuronet.common.experimantal.Net();

        normalize(inputData);

//        net.setLayer(0, 300, rawBytes.length, 3, 0.007f);
//        net.setLayer(1, 50, 300, 3, 0.01f);
//        net.setLayer(2, 36, 50, 3, 0.1f);

//        net.setLayer(0, 10, rawBytes.length, 3, 0.0005f);
//        net.setLayer(1, 5, 10, 3, 0.0001f);
//        net.setLayer(2, 3, 5, 3, 0.0001f);

        final FunctionType functionType = FunctionType.BIPOLAR_SIGMA;

        final int multiplexer = 100;

        net.addLayer(10, inputData.length, functionType, 0.0005f * multiplexer);
        net.addLayer(5, 10, functionType, 0.0001f * multiplexer);
        net.addLayer(3, 5, functionType, 0.0001f * multiplexer);

        logger.debug("create(): Net created successful.");

        return net;
    }
}
