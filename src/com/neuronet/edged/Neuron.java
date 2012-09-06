package com.neuronet.edged;

import com.neuronet.edged.api.IEdge;
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

    private final List<IEdge> inputEdgeList = new ArrayList<IEdge>();
    private final List<IEdge> outputEdgeList = new ArrayList<IEdge>();

    private final FunctionType functionType;
    private final float alfa;
    private final short position;

    private float dx = 0;
    private float lastPotential = 0;

    public Neuron(final float dx, final FunctionType functionType, final float alfa) {
        this(dx, functionType, alfa, (short) 0);
    }

    public Neuron(final float dx, final FunctionType functionType, final float alfa, short position) {
        this.dx = dx;
        this.alfa = alfa;
        this.functionType = functionType;
        this.position = position;
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
    public boolean isAccessible(INeuron neuron) {
        return Util.chance((int) Util.getNorm(this.getPosition(), neuron.getPosition()) + 1);
    }

    @Override
    public float getFunction() {
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
    public float[] educate(float error, float[] s) {
        if (s.length != this.inputEdgeList.size()) {
            throw new RuntimeException();
        }
        final float[] commonNeuronError = new float[this.inputEdgeList.size()];

        final float commonError = error * this.getDerived();
        final float errorCoefficient = commonError * educationSpeed;

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

    /**
     * Synapse educating.
     */
    private void educateWeights(final float error, final float[] s) {
        final float er = error * educationSpeed;
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
