package com.neuronet.classic.api;

public interface INeuron {

    public float[] getWeights();

    public float getFunction(final float[] inputData);

    public float getDerived();

    public float[] educate(final float error, final float[] signal);

    public float getLastPotential();

    public void setLastPotential(final float signal);
}