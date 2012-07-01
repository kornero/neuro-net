package com.neuronet.common.api;

import com.neuronet.util.FunctionType;

public interface INet {

    public void addLayer(final int neurons, final int inputNeurons, final FunctionType functionType, final float alfa);

    public void setLayer(final int layerIndex, final int neurons, final int inputNeurons, final FunctionType functionType, final float alfa);

    public float[] runNet(final float[] inputData);

    public float[] educate(final float[] expectedOutput, final float[] inputData);
}
