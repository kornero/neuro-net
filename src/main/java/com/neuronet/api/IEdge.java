package com.neuronet.api;

import java.io.Serializable;

public interface IEdge extends Serializable {
    public float run();

    public float getWeight();

    public void setWeight(final float weight);

    public INeuron getInput();

    public INeuron getOutput();

    public void incrementWeight(final float incWeight);
}
