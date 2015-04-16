package com.neuronet.api;

import com.neuronet.util.Util;

public class RandomWeightNetParameters extends NetParameters {

    private static final INetParameters INSTANCE = new RandomWeightNetParameters(0.15f, 0.05f);

    public RandomWeightNetParameters(final float defaultDX, final float educationSpeed) {
        super(defaultDX, 0, educationSpeed);
    }

    public static INetParameters getDefaultParameters() {
        return INSTANCE;
    }

    @Override
    public float generateEdgeWeight() {
        return Util.randomFloats(1)[0];
    }
}