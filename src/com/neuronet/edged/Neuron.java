package com.neuronet.edged;

import com.neuronet.edged.api.INeuron;
import com.neuronet.util.FunctionType;
import com.neuronet.util.Functions;
import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Neuron implements INeuron {

    private static final Logger logger = LoggerFactory.getLogger(Neuron.class);

    private static final float educationSpeed = 0.2f;

    private final List<Edge> inputEdgeList = new ArrayList<Edge>();
    private final List<Edge> outputEdgeList = new ArrayList<Edge>();

    private final FunctionType functionType;
    private final float alfa;

    private float dx = 0;
    private float lastPotential = 0;

    public Neuron(final float dx, final FunctionType functionType, final float alfa) {
        this.dx = dx;
        this.alfa = alfa;
        this.functionType = functionType;
    }

    @Override
    public Edge createInputEdge(final INeuron inputNeuron, final float weight) {
        final Edge edge;
        // TODO: add metric, instead of randomization.
        if (Util.chance(3)) {
            edge = NullEdge.getInstance();
        } else {
            edge = new Edge(inputNeuron, this, weight);
        }
        this.addInputEdge(edge);
        return edge;
    }

    @Override
    public void addInputEdge(final Edge inputEdge) {
        this.inputEdgeList.add(inputEdge);
    }

    @Override
    public void addOutputEdge(final Edge outputEdge) {
        this.outputEdgeList.add(outputEdge);
    }

    @Override
    public float getFunction() {
        float potential = dx;
        for (Edge edge : inputEdgeList) {
            potential += edge.getWeight() * edge.getInput().getLastPotential();
        }
        setLastPotential(potential);
        return Functions.getFunction(this.getLastPotential(), this.functionType, this.alfa);
    }

    @Override
    public float getDerived() {
        return Functions.getDerived(this.getLastPotential(), this.functionType, this.alfa);
    }

    @Override
    public float[] educate(float error, float[] s) {
        final float[] weights = this.getWeights();
        error *= this.getDerived();

        //  Calculating error (dx not included, that's why "length - 1").
        final float[] tmp = new float[weights.length];
        for (int i = 0; i < weights.length; i++) {
            tmp[i] = weights[i] * error;
        }

        //  Changing synapses.
        this.educateWeights(error, s);
        return tmp;
    }

    @Override
    public float getLastPotential() {
        return lastPotential;
    }

    @Override
    public void setLastPotential(float signal) {
        this.lastPotential = signal;
    }

    /**
     * Synapse educating.
     */
    private void educateWeights(final float error, final float[] s) {
        this.dx += error * educationSpeed;

        if (s.length != this.inputEdgeList.size()) {
            throw new RuntimeException();
        }

        int i = 0;
        for (final Edge edge : inputEdgeList) {
            edge.incrementWeight(s[i] * error * educationSpeed);
            i++;
        }
    }

    private float[] getWeights() {
        final float[] weights = new float[inputEdgeList.size()];

        int i = 0;
        for (final Edge edge : inputEdgeList) {
            weights[i] = edge.getWeight();
            i++;
        }
        return weights;
    }
}
