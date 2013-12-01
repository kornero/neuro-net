package com.neuronet.impl.example;

import com.neuronet.api.INetParameters;
import com.neuronet.api.RandomNetParameters;
import com.neuronet.api.generator.AbstractMathFunctionNetInfo;

public class ParabolaNetInfo extends AbstractMathFunctionNetInfo {
    private static final int MIN_NEURONS = 5;
    private static final int MAX_NEURONS = 50;
    private static final int MIN_LAYERS = 1;
    private static final int MAX_LAYERS = 3;
    private static final int MIN_INPUT = -10;
    private static final int MAX_INPUT = 10;
    private static final int MIN_OUTPUT = 0;
    private static final int MAX_OUTPUT = 100;
    private static final float MAX_STEP_SIZE = 1f;

    public ParabolaNetInfo() {
        super(MIN_NEURONS, MAX_NEURONS, MIN_LAYERS, MAX_LAYERS,
                MIN_INPUT, MAX_INPUT, MIN_OUTPUT, MAX_OUTPUT);
    }

    @Override
    public INetParameters getParameters() {
        return RandomNetParameters.getDefaultParameters();
    }

    @Override
    protected float f(final float x) {
        return (float) Math.pow(x, 2);
    }
}