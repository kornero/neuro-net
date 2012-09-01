package com.neuronet.edged.api;

import com.neuronet.edged.Edge;

public interface INeuron {

    public float getFunction();

    public float getDerived();

    public float[] educate(final float error, final float[] signal);

    public float getLastPotential();

    public void setLastPotential(final float signal);

    public Edge createInputEdge(final INeuron inputNeuron, final float weight);

    public void addInputEdge(final Edge inputEdge);

    public void addOutputEdge(final Edge outputEdge);
}
