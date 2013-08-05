package com.neuronet.edged.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class RandomConfiguration implements IConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RandomConfiguration.class);

    private static final Random RANDOM = new Random();

    @Override
    public float getDefaultAlfa() {
        return RANDOM.nextFloat();
    }

    @Override
    public float getDefaultDX() {
        return RANDOM.nextFloat();
    }

    @Override
    public float getDefaultEdgeWeight() {
        return RANDOM.nextFloat();
    }

    @Override
    public float getEducationSpeed() {
        return 0.01f;
    }
}