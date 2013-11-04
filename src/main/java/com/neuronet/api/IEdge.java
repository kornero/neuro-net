package com.neuronet.api;

import java.io.Serializable;

public interface IEdge extends Serializable {
    public float getPotential();

    public float getWeight();

    public INeuron getInput();

    public INeuron getOutput();

    public void incrementWeight(float incWeight);

    public void setWeight(float weight);
}
