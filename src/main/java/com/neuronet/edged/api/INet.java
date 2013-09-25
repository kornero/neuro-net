package com.neuronet.edged.api;

import com.neuronet.util.FunctionType;

import java.io.Serializable;
import java.util.Deque;

public interface INet extends Serializable {

    public void addLayer(int neurons, FunctionType functionType);

    public float[] runNet(final float[] inputData);

    public float[] educate(final float[] expectedOutput, final float[] inputData);

    public IConfiguration getConfiguration();

    public Deque<ILayer> getLayers();

    public int getInputsAmount();

    public int getOutputsAmount();

    public void setEducationSpeed(final float educationSpeed);
}
