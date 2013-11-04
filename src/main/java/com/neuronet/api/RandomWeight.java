package com.neuronet.api;

import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomWeight extends Configuration {

    private static final Logger logger = LoggerFactory.getLogger(RandomWeight.class);

    public RandomWeight(float defaultAlfa, float defaultDX, float educationSpeed) {
        super(defaultAlfa, defaultDX, 0, educationSpeed);
    }

    @Override
    public float getDefaultEdgeWeight() {
        return Util.randomFloats(1)[0];
    }
}