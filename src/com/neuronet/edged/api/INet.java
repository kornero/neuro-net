package com.neuronet.edged.api;

import com.neuronet.util.FunctionType;

public interface INet {

    public void addLayer(int neurons, FunctionType functionType);

    public float[] runNet(final float[] inputData);

    public float[] educate(final float[] expectedOutput, final float[] inputData);

    public IConfiguration getConfiguration();
}
