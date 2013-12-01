package com.neuronet.impl.example;

import com.neuronet.api.generator.AbstractFunctionPredictionNetInfo;

public class CosSinNetInfo extends AbstractFunctionPredictionNetInfo {

    private static final int INPUTS = 4;
    private static final int MIN_INPUT = (int) (Math.PI * (-4)) + 1;
    private static final int MAX_INPUT = (int) (Math.PI * (4)) + 1;
    private static final int MIN_OUTPUT = -2;
    private static final int MAX_OUTPUT = 2;
    private static final int MIN_NEURONS = 5;
    private static final int MAX_NEURONS = 50;
    private static final int MIN_LAYERS = 1;
    private static final int MAX_LAYERS = 3;

    public CosSinNetInfo() {
        super(MIN_NEURONS, MAX_NEURONS, MIN_LAYERS, MAX_LAYERS, INPUTS,
                MIN_INPUT, MAX_INPUT, MIN_OUTPUT, MAX_OUTPUT);
    }

    @Override
    protected float f(final float x) {
        return (float) (Math.sin(3 * x / 6) + Math.cos(x / 6));
    }
}