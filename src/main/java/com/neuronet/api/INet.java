package com.neuronet.api;

import java.io.Serializable;
import java.util.Deque;

public interface INet extends Serializable {

    public void addLayer(final int neurons, final IFunction function);

    public float[] run(final float[] inputData);

    public float[] educate(final float[] inputData, final float[] expectedOutput);

    public IConfiguration getConfiguration();

    public Deque<ILayer> getLayers();

    public int getInputsAmount();

    public int getOutputsAmount();

    public void setEducationSpeed(final float educationSpeed);
}
