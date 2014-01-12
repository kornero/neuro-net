package com.neuronet.impl;

import com.neuronet.api.IFunction;
import com.neuronet.api.INet;
import com.neuronet.api.INetBuilder;
import com.neuronet.api.RandomWeightNetParameters;
import com.neuronet.api.generator.INetInfo;
import com.neuronet.impl.example.CosSinNetInfo;
import com.neuronet.impl.example.SinNetInfo;
import com.neuronet.impl.example.SqrtNetInfo;
import com.neuronet.impl.functions.BinarySigmaFunction;
import com.neuronet.impl.functions.BipolarSigmaFunction;
import com.neuronet.util.Util;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetLearnerTest {

    private static final Logger logger = LoggerFactory.getLogger(NetLearnerTest.class);

    private static INet createNet(final INetInfo netInfo) {
        final INetBuilder netBuilder = new NetBuilder();
        netBuilder.setNetConfiguration(netInfo.getNetConfiguration());
        netBuilder.setNetParameters(new RandomWeightNetParameters(
                0.05f, //Configuration.DEFAULT_DX,
                0.00051f  //Configuration.DEFAULT_EDUCATION_SPEED,
        ));

//        final IFunction functionType = BipolarSigmaFunction.getInstance(5);
//        netBuilder.addLayer(4, functionType);
//        netBuilder.addLayer(4, functionType);
//        netBuilder.addLayer(1, functionType);

        final IFunction functionType = BipolarSigmaFunction.getInstance(0.05f);
        netBuilder.addLayer(10, functionType);
        netBuilder.addLayer(4, functionType);
        netBuilder.addLayer(1, functionType);


//
//        netBuilder.addLayer(50, BipolarSigmaFunction.getInstance());
//        netBuilder.addLayer(1, GaussFunction.getInstance());

        return netBuilder.build();
    }

    @Test
    public void test_sqrt_net() {
        final INetInfo netInfo = new SqrtNetInfo();
        final INetBuilder netBuilder = new NetBuilder();
        netBuilder.setNetConfiguration(netInfo.getNetConfiguration());
        netBuilder.setNetParameters(new RandomWeightNetParameters(
                0.05f, //Configuration.DEFAULT_DX,
                0.00051f  //Configuration.DEFAULT_EDUCATION_SPEED,
        ));

        final IFunction functionType = BipolarSigmaFunction.getInstance(0.05f);
        netBuilder.addLayer(10, functionType);
        netBuilder.addLayer(4, functionType);
        netBuilder.addLayer(1, functionType);

        final INet net = netBuilder.build();
//        final NetLearner learner = new VisualNetLearner(net, netInfo, 0.00051f);
        final NetLearner learner = new NetLearner(net, netInfo, 0.00051f);
        learner.learn(100000, 7.5f);

        float[] input = new float[1];
        final float[] expected = new float[1];
        for (int i = 5; i < 20; i += 3) {
            input[0] = i;
            expected[0] = (float) Math.sqrt(input[0]);
            final float[] runResult = net.run(input);

            float exp = expected[0];
            float act = Util.denormalizeOutputs(runResult, netInfo.getNetConfiguration())[0];

            logger.debug("Data: sqrt({}) = {}, actual = {}",
                    Util.toString(input[0]),
                    Util.toString(exp),
                    Util.toString(act)
            );
            Assert.assertEquals("Unexpected result.", exp, act, Math.abs(exp * 0.2));
        }
    }

    @Ignore
    @Test
    public void test_cos_sin_net() {
        final INetInfo netInfo = new CosSinNetInfo();
        final INetBuilder netBuilder = new NetBuilder(netInfo);

//        final IFunction functionType = BipolarSigmaFunction.getInstance(5.0f);
        final IFunction functionType = BinarySigmaFunction.getInstance();
//        final IFunction functionType = BipolarSigmaFunction.getInstance();
        netBuilder.addLayer(10, functionType);
        netBuilder.addLayer(5, functionType);
        netBuilder.addLayer(1, functionType);

        final INet net = netBuilder.build();
        final NetLearner learner = new VisualNetLearner(net, netInfo, 0.005f);
        learner.learn(1000 * 1000, 5);

        float[] input = new float[1];
        final float[] expected = new float[1];
        for (int i = 5; i < 20; i += 3) {
            input[0] = i;
            expected[0] = (float) Math.sin(input[0]);
            final float[] runResult = net.run(input);

            float exp = expected[0];
            float act = Util.denormalizeOutputs(runResult, netInfo.getNetConfiguration())[0];

            logger.debug("Data: sin({}) = {}, actual = {}",
                    Util.toString(input[0]),
                    Util.toString(exp),
                    Util.toString(act)
            );
            Assert.assertEquals("Unexpected result.", exp, act, Math.abs(exp * 0.2));
        }
    }

    @Ignore
    @Test
    public void test_sin_net() {
        final INetInfo netInfo = new SinNetInfo();
        final INet net = createNet(netInfo);
        final NetLearner learner = new VisualNetLearner(net, netInfo, 0.00051f);
        learner.learn(1000 * 1000, 10);

        float[] input = new float[1];
        final float[] expected = new float[1];
        for (int i = 5; i < 20; i += 3) {
            input[0] = i;
            expected[0] = (float) Math.sin(input[0]);
            final float[] runResult = net.run(input);

            float exp = expected[0];
            float act = Util.denormalizeOutputs(runResult, netInfo.getNetConfiguration())[0];

            logger.debug("Data: sin({}) = {}, actual = {}",
                    Util.toString(input[0]),
                    Util.toString(exp),
                    Util.toString(act)
            );
            Assert.assertEquals("Unexpected result.", exp, act, Math.abs(exp * 0.2));
        }
    }
}