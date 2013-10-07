package com.neuronet.edged.api;

import com.neuronet.util.FunctionType;
import com.neuronet.util.Util;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class RandomConfiguration implements IConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RandomConfiguration.class);

    private static final Random RANDOM = new Random();

    @Override
    public float getDefaultAlfa(final FunctionType type) {
        switch (type) {
            case BINARY_SIGMA:
                return RANDOM.nextFloat() * 10;
            case BIPOLAR_SIGMA:
                return RANDOM.nextFloat();

            // No alfa in that functions.
            case GAUSS:
            case LINEAR:
            case BINARY:
            case BIPOLAR:
                return 0.0f;

            default:
                throw new NotImplementedException("Not implemented random alfa for function; " + type.name());
        }
    }

    @Override
    public float getDefaultDX() {
        return Util.randomFloats(1)[0];
    }

    @Override
    public float getDefaultEdgeWeight() {
        return Util.randomFloats(1)[0];
    }

    @Override
    public float getEducationSpeed() {
        return 0.01f;
    }
}