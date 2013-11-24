package com.neuronet.impl.example;

import com.neuronet.api.INetParameters;
import com.neuronet.api.RandomNetParameters;

public class CosSinNetInfo extends AbstractMathFunctionNetInfo {
    private static final int MIN_NEURONS = 5;
    private static final int MAX_NEURONS = 50;
    private static final int MIN_LAYERS = 1;
    private static final int MAX_LAYERS = 3;

    private static final int MIN_INPUT = (int) (Math.PI * (-4)) + 1;
    private static final int MAX_INPUT = (int) (Math.PI * 4) + 1; // (float) (Math.PI * 2)
    private static final int MIN_OUTPUT = -2;
    private static final int MAX_OUTPUT = 2;

    public CosSinNetInfo() {
        super(MIN_NEURONS, MAX_NEURONS, MIN_LAYERS, MAX_LAYERS,
                MIN_INPUT, MAX_INPUT, MIN_OUTPUT, MAX_OUTPUT);
    }

    @Override
    protected float f(final float x) {
        return (float) (Math.sin(3 * x) + Math.cos(x));
    }

    @Override
    public INetParameters getParameters() {
        return RandomNetParameters.getDefaultConfiguration();
    }
}