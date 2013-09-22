package com.neuronet.generator;

import com.neuronet.edged.api.IConfiguration;

import java.util.Map;

public interface NetInfo {

    public int getMinLayers();

    public int getMaxLayers();

    public int getMinNeurons();

    public int getMaxNeurons();

    public int getInputs();

    public int getOutputs();

    public IConfiguration getConfiguration();

    public Map<Float[], Float[]> getEducationData(final int iteration);

    public Map<Float[], Float[]> getTestData(final int iteration);
}
