package com.neuronet.impl;

import com.neuronet.api.INetConfiguration;

public class ImmutableNetConfiguration implements INetConfiguration {
    private final int inputs, outputs;
    private final int minInput, maxInput;
    private final int minOutput, maxOutput;

    public ImmutableNetConfiguration(int inputs, int outputs, int minInput, int maxInput, int minOutput, int maxOutput) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.minInput = minInput;
        this.maxInput = maxInput;
        this.minOutput = minOutput;
        this.maxOutput = maxOutput;
    }

    @Override
    public int getInputsAmount() {
        return this.inputs;
    }

    @Override
    public int getOutputsAmount() {
        return this.outputs;
    }

    @Override
    public int getMinInput() {
        return this.minInput;
    }

    @Override
    public int getMaxInput() {
        return this.maxInput;
    }

    @Override
    public int getMinOutput() {
        return this.minOutput;
    }

    @Override
    public int getMaxOutput() {
        return this.maxOutput;
    }
}