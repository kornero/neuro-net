package com.neuronet.edged;

import com.neuronet.edged.api.IEdge;
import com.neuronet.edged.api.INeuron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Edge implements IEdge {

    private static final Logger logger = LoggerFactory.getLogger(Edge.class);

    private final INeuron input;
    private final INeuron output;

    private float weight = 0;

    public Edge(INeuron input, INeuron output) {
        this.input = input;
        this.output = output;
    }

    public Edge(INeuron input, INeuron output, float weight) {
        this.input = input;
        this.output = output;
        this.weight = weight;
    }

    @Override
    public float getPotential() {
        return weight * input.getLastPotential();
    }

    @Override
    public float getWeight() {
        return weight;
    }

    @Override
    public INeuron getInput() {
        return input;
    }

    @Override
    public INeuron getOutput() {
        return output;
    }

    @Override
    public void incrementWeight(float incWeight) {
        this.weight += incWeight;
    }

    @Override
    public void setWeight(float weight) {
        this.weight = weight;
    }
}
