package com.neuronet.api;

import java.io.Serializable;
import java.util.List;

public interface INeuron extends Serializable {

    public float run();

    public float[] educate(final float error, final float[] signal, final float educationSpeed);

    public boolean isAccessible(final INeuron neuron);

    public int getPosition();

    public float getDerived();

    public float getDx();

    public void setDx(final float dx);

    public float getLastPotential();

    public void setLastPotential(final float signal);

    public void addInputEdge(final IEdge inputEdge);

    public void addOutputEdge(final IEdge outputEdge);

    public List<IEdge> getInputEdges();

    public List<IEdge> getOutputEdges();

    public ILayer getLayer();
}
