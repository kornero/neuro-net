package com.neuronet.api;

import com.neuronet.api.generator.EducationSample;

import java.io.Serializable;
import java.util.Deque;

public interface INet extends Serializable {

    public float[] run(final float[] inputData);

    public float[] educate(final EducationSample educationSample);

    public float[] educate(final float[] inputData, final float[] expectedOutput);

    public INetParameters getNetParameters();

    public INetConfiguration getNetConfiguration();

    public Deque<ILayer> getLayers();

    public void setEducationSpeed(final float educationSpeed);
}
