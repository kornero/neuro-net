package com.neuronet.api.generator;

import com.neuronet.api.IConfiguration;
import com.neuronet.api.IFunction;

import java.util.List;

public interface NetInfo {

    public int getMinLayers();

    public int getMaxLayers();

    public int getMinNeurons();

    public int getMaxNeurons();

    public int getInputs();

    public int getOutputs();

    public IFunction getOutputFunction();

    public float getMaxInputValue();

    public IConfiguration getConfiguration();

    public List<EductionSample> getEducationData();

    public List<EductionSample> getTestData();
}
