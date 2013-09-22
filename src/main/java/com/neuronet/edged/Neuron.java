package com.neuronet.edged;

import com.neuronet.edged.api.IConfiguration;
import com.neuronet.edged.api.IEdge;
import com.neuronet.edged.api.ILayer;
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

    private final List<IEdge> inputEdgeList = new ArrayList<>();
    private final List<IEdge> outputEdgeList = new ArrayList<>();

    private final FunctionType functionType;
    private final ILayer layer;
    private final short position;
    private final float alfa;
    private final float educationSpeed;

    private float dx = 0;
    private float lastPotential = 0;

    public Neuron(final FunctionType functionType, final ILayer layer, short position) {
        if (position <= 0) {
            throw new IllegalArgumentException("Neuron position must be positive, but was = " + position);
        }

        this.layer = layer;
        this.functionType = functionType;
        this.position = position;

        final IConfiguration configuration = layer.getNet().getConfiguration();

        this.dx = configuration.getDefaultDX();
        this.alfa = configuration.getDefaultAlfa();
        this.educationSpeed = configuration.getEducationSpeed();
    }

    @Override
    public IEdge createInputEdge(final INeuron inputNeuron, final float weight) {
        final IEdge edge;

        if (inputNeuron.isAccessible(this)) {
            edge = NullEdge.getInstance();
        } else {
            edge = new Edge(inputNeuron, this, weight);
        }
        this.addInputEdge(edge);
        return edge;
    }

    @Override
    public void addInputEdge(final IEdge inputEdge) {
        this.inputEdgeList.add(inputEdge);
    }

    @Override
    public void addOutputEdge(final IEdge outputEdge) {
        this.outputEdgeList.add(outputEdge);
    }

    @Override
    public List<IEdge> getInputEdges() {
        return this.inputEdgeList;
    }

    @Override
    public List<IEdge> getOutputEdges() {
        return this.outputEdgeList;
    }

    @Override
    public ILayer getLayer() {
        return this.layer;
    }

    @Override
    public boolean isAccessible(INeuron neuron) {
//        logger.debug(10.0f / (1 + Math.abs(this.getPosition() - neuron.getPosition())));
        return Util.chance(10.0f / (1 + Math.abs(this.getPosition() - neuron.getPosition())));
    }

    @Override
    public float runNeuron() {
        float potential = dx;
        for (IEdge edge : inputEdgeList) {
            potential += edge.getPotential();
        }
        setLastPotential(potential);
        return Functions.getFunction(this.getLastPotential(), this.functionType, this.alfa);
    }

    @Override
    public float getDerived() {
        return Functions.getDerived(this.getLastPotential(), this.functionType, this.alfa);
    }

    @Override
    public float getAlfa() {
        return this.alfa;
    }

    @Override
    public float getDX() {
        return this.dx;
    }

    @Override
    public float[] educate(float error, float[] s) {
        if (s.length != this.inputEdgeList.size()) {
            throw new RuntimeException();
        }
        final float[] commonNeuronError = new float[this.inputEdgeList.size()];

        final float commonError = error * this.getDerived();
        final float errorCoefficient = commonError * this.educationSpeed;

        // Educating neuron dx.
        this.dx += errorCoefficient;

        int i = 0;
        for (final IEdge edge : inputEdgeList) {

            //  Calculating common error.
            commonNeuronError[i] = edge.getWeight() * commonError;

            //  Changing synapses.
            edge.incrementWeight(s[i] * errorCoefficient);
            i++;
        }

        return commonNeuronError;
    }

    @Override
    public float getLastPotential() {
        return lastPotential;
    }

    @Override
    public void setLastPotential(float signal) {
        this.lastPotential = signal;
    }

    @Override
    public short getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }

    /**
     * Synapse educating.
     */
    private void educateWeights(final float error, final float[] s) {
        final float er = error * this.educationSpeed;
        this.dx += er;

        if (s.length != this.inputEdgeList.size()) {
            throw new RuntimeException();
        }

        int i = 0;
        for (final IEdge edge : inputEdgeList) {
            edge.incrementWeight(s[i] * er);
            i++;
        }
    }

    private float[] getWeights() {
        final float[] weights = new float[inputEdgeList.size()];

        int i = 0;
        for (final IEdge edge : inputEdgeList) {
            weights[i] = edge.getWeight();
            i++;
        }
        return weights;
    }
}
