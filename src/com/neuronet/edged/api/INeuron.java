package com.neuronet.edged.api;

public interface INeuron {

    public boolean isAccessible(final INeuron neuron);

    public short getPosition();

    public float getFunction();

    public float getDerived();

    public float[] educate(final float error, final float[] signal);

    public float getLastPotential();

    public void setLastPotential(final float signal);

    public IEdge createInputEdge(final INeuron inputNeuron, final float weight);

    public void addInputEdge(final IEdge inputEdge);

    public void addOutputEdge(final IEdge outputEdge);
}
