package com.neuronet.common.experimantal;

import com.neuronet.common.classic.Neuron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Edge {

    private static final Logger logger = LoggerFactory.getLogger(Edge.class);

    private final Neuron input;
    private final Neuron output;

    private float weight = 0;
    private float signal = 0;

    public Edge(Neuron input, Neuron output) {
        this.input = input;
        this.output = output;
    }

    public float getWeight() {
        return weight;
    }

    public Neuron getInput() {
        return input;
    }

    public Neuron getOutput() {
        return output;
    }

    public float getSignal() {
        return signal;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setSignal(float signal) {
        this.signal = signal;
    }
}
