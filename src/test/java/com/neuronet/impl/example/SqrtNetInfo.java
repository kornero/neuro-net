package com.neuronet.impl.example;

import com.neuronet.api.INetParameters;
import com.neuronet.api.RandomNetParameters;

public class SqrtNetInfo extends AbstractMathFunctionNetInfo {
    private static final int MIN_NEURONS = 5;
    private static final int MAX_NEURONS = 50;
    private static final int MIN_LAYERS = 1;
    private static final int MAX_LAYERS = 3;
    private static final int MIN_INPUT = 4;
    private static final int MAX_INPUT = 25;
    private static final int MIN_OUTPUT = (int) Math.sqrt(MIN_INPUT) - 1;
    private static final int MAX_OUTPUT = (int) Math.sqrt(MAX_INPUT) + 1;
    private static final float MAX_STEP_SIZE = 0.1f;

    public SqrtNetInfo() {
        super(MIN_NEURONS, MAX_NEURONS, MIN_LAYERS, MAX_LAYERS,
                MIN_INPUT, MAX_INPUT, MIN_OUTPUT, MAX_OUTPUT);
    }

    @Override
    public INetParameters getParameters() {
        return RandomNetParameters.getDefaultConfiguration();
    }

    @Override
    protected float f(final float x) {
        return (float) Math.sqrt(x);
    }
}