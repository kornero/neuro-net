package com.neuronet.edged.api;

public interface IEdge {
    public float getPotential();

    public float getWeight();

    public INeuron getInput();

    public INeuron getOutput();

    public void incrementWeight(float incWeight);

    public void setWeight(float weight);
}
