package com.neuronet.edged.api;

import java.io.Serializable;
import java.util.List;

public interface INeuron extends Serializable {

    public float runNeuron();

    public float[] educate(final float error, final float[] signal);

    public boolean isAccessible(final INeuron neuron);

    public short getPosition();

    public float getDerived();

    public float getAlfa();

    public float getDX();

    public float getLastPotential();

    public void setLastPotential(final float signal);

    public IEdge createInputEdge(final INeuron inputNeuron, final float weight);

    public void addInputEdge(final IEdge inputEdge);

    public void addOutputEdge(final IEdge outputEdge);

    public List<IEdge> getInputEdges();

    public List<IEdge> getOutputEdges();

    public ILayer getLayer();
}
