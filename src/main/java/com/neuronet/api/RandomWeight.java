package com.neuronet.api;

import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomWeight extends NetParameters {

    private static final Logger logger = LoggerFactory.getLogger(RandomWeight.class);

    public RandomWeight(float defaultDX, float educationSpeed) {
        super(defaultDX, 0, educationSpeed);
    }

    @Override
    public float generateEdgeWeight() {
        return Util.randomFloats(1)[0];
    }
}