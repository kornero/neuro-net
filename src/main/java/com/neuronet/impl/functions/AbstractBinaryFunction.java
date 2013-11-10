package com.neuronet.impl.functions;

import com.neuronet.api.IFunction;

public abstract class AbstractBinaryFunction implements IFunction {

    private static final float MIN_OUTPUT = 0.0f;
    private static final float MAX_OUTPUT = 1.0f;

    @Override
    public float getMinOutput() {
        return MIN_OUTPUT;
    }

    @Override
    public float getMaxOutput() {
        return MAX_OUTPUT;
    }
}