package com.neuronet.api;

import java.io.Serializable;
import java.util.List;

public interface ILayer extends Serializable {

    public float[] run();

    public float[] educate(final float[] inputData, final float[] error, final float educationSpeed);

    public float[] getLastResult();

    public List<INeuron> getNeurons();

    public IFunction getFunction();

    public INet getNet();
}
