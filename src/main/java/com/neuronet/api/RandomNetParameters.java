package com.neuronet.api;

import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomNetParameters extends NetParameters {

    private static final Logger logger = LoggerFactory.getLogger(RandomNetParameters.class);

    private static final INetParameters INSTANCE = new NetParameters();

    public static INetParameters getDefaultConfiguration() {
        return INSTANCE;
    }

    @Override
    public float generateDx() {
        return Util.randomFloats(1)[0];
    }

    @Override
    public float generateEdgeWeight() {
        return Util.randomFloats(1)[0];
    }
}