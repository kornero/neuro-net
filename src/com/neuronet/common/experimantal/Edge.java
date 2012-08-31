package com.neuronet.common.experimantal;

import com.neuronet.common.api.INeuron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Edge {

    private static final Logger logger = LoggerFactory.getLogger(Edge.class);

    private final INeuron input;
    private final INeuron output;

    private float weight = 0;
    private float signal = 0;

    public Edge(INeuron input, INeuron output) {
        this.input = input;
        this.output = output;
    }

    public Edge(INeuron input, INeuron output, float weight) {
        this.input = input;
        this.output = output;
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    public INeuron getInput() {
        return input;
    }

    public INeuron getOutput() {
        return output;
    }

    public float getSignal() {
        return signal;
    }

    public void incrementWeight(float incWeight) {
        this.weight += incWeight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setSignal(float signal) {
        this.signal = signal;
    }
}
