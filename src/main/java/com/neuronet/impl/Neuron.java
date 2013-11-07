package com.neuronet.impl;

import com.neuronet.api.IEdge;
import com.neuronet.api.IFunction;
import com.neuronet.api.ILayer;
import com.neuronet.api.INeuron;
import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Neuron implements INeuron {

    private static final long serialVersionUID = 200404112013L;
    private static final Logger logger = LoggerFactory.getLogger(Neuron.class);

    private final List<IEdge> inputEdgeList = new ArrayList<>();
    private final List<IEdge> outputEdgeList = new ArrayList<>();

    private final IFunction function;
    private final ILayer layer;
    private final int position;

    private volatile float dx = 0;
    private volatile float lastPotential = 0;

    public Neuron(final IFunction function, final float defaultDx, final int position, final ILayer layer) {
        if (position <= 0) {
            throw new IllegalArgumentException("Neuron position must be positive, but was = " + position);
        }

        this.function = function;
        this.layer = layer;
        this.position = position;
        this.setDx(defaultDx);
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
        return true;
//        logger.debug(10.0f / (1 + Math.abs(this.getPosition() - neuron.getPosition())));
//        return Util.chance(10.0f / (1 + Math.abs(this.getPosition() - neuron.getPosition())));
    }

    @Override
    public float run() {
        float potential = this.getDx();
        for (final IEdge edge : this.getInputEdges()) {
            potential += edge.run();
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Potential: {}, Function: {}", potential,
                    this.function.executeFunction(potential));
        }
        potential = this.function.executeFunction(potential);
        setLastPotential(potential);
        return potential;
    }

    @Override
    public float getDerived() {
        return this.function.executeDerived(this.getLastPotential());
    }

    @Override
    public float getDx() {
        return this.dx;
    }

    @Override
    public void setDx(final float dx) {
        this.dx = dx;
    }

    @Override
    public float[] educate(final float error, final float[] s, final float educationSpeed) {
        if (s.length != this.getInputEdges().size()) {
            throw new RuntimeException();
        }
        final float[] commonNeuronError = new float[this.getInputEdges().size()];

        final float commonError = error * this.getDerived();
        final float errorCoefficient = commonError * educationSpeed;

        // Educating neuron dx.
//        this.setDx(this.getDx() + errorCoefficient); // Do we really need this?

        int i = 0;
        for (final IEdge edge : this.getInputEdges()) {

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
        return this.lastPotential;
    }

    @Override
    public void setLastPotential(final float signal) {
        this.lastPotential = signal;
    }

    @Override
    public int getPosition() {
        return this.position;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
