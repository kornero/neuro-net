package com.neuronet.api;

import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomConfiguration extends Configuration {

    private static final Logger logger = LoggerFactory.getLogger(RandomConfiguration.class);

    private static final IConfiguration INSTANCE = new Configuration();

    public static IConfiguration getDefaultConfiguration() {
        return INSTANCE;
    }

    @Override
    public float getDefaultDX() {
        return Util.randomFloats(1)[0];
    }

    @Override
    public float getDefaultEdgeWeight() {
        return Util.randomFloats(1)[0];
    }
}