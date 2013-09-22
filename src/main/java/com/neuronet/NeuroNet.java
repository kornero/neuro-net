package com.neuronet;

import com.neuronet.classic.api.INet;
import com.neuronet.classic.impl.Net;
import com.neuronet.util.FunctionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.neuronet.util.Util.normalize;
import static com.neuronet.util.Util.randomFloats;

public class NeuroNet {

    private static final Logger logger = LoggerFactory.getLogger(NeuroNet.class);

    public static void main(String... args) {
        logger.info("NeuroNet is starting.");
        float[] input = getData();
        final INet net = create(input);

        for (int i = 0; i < 10; i++) {
            input = randomFloats(3);
            normalize(input);
            final float[] runResult = net.runNet(input);

            logger.debug("run:" + Arrays.toString(runResult));
            net.educate(new float[]{-1, -1, 1}, input);
        }
    }

    private static float[] getData() {
        return new float[]{1, 2, 3};
    }

    private static INet create(final float[] inputData) {
        final INet net = new Net();

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