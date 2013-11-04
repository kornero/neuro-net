package com.neuronet.api.generator;

import com.neuronet.api.IConfiguration;
import com.neuronet.util.FunctionType;

import java.util.Map;

public interface NetInfo {

    public int getMinLayers();

    public int getMaxLayers();

    public int getMinNeurons();

    public int getMaxNeurons();

    public int getInputs();

    public int getOutputs();

    public FunctionType getOutputFunctionType();

    public float getMaxInputValue();

    public IConfiguration getConfiguration();

    public Map<Float[], Float[]> getEducationData(final int iteration);

    public Map<Float[], Float[]> getTestData(final int iteration);
}
