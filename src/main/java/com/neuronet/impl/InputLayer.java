package com.neuronet.impl;

import com.neuronet.api.*;
import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class InputLayer implements ILayer {

    private static final long serialVersionUID = 202204112013L;
    private static final Logger logger = LoggerFactory.getLogger(InputLayer.class);
    private final List<INeuron> neurons;
    private final INet net;
    private volatile float[] lastResult;

    public InputLayer(final int inputs, final INet net) {
        this.neurons = new ArrayList<>(inputs);
        this.net = net;
        for (short i = 1; i <= inputs; i++) {
            neurons.add(new InputNeuron(i));
        }
    }

    @Override
    public List<INeuron> getNeurons() {
        return neurons;
    }

    @Override
    public INet getNet() {
        return this.net;
    }

    @Override
    public float[] run() {
        final float[] result = new float[neurons.size()];
        int i = 0;
        for (final INeuron n : getNeurons()) {
            result[i] = n.getLastPotential();
            i++;
        }
        return this.lastResult = result;
    }

    @Override
    public float[] getLastResult() {
        return this.lastResult;
    }

    @Override
    public ILayerConfiguration getLayerConfiguration() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float[] educate(final float[] inputData, final float[] error, final float educationSpeed) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }

    private class InputNeuron implements INeuron {

        private static final long serialVersionUID = 201504112013L;
        private final int position;
        private volatile float signal = 0;

        private InputNeuron(final int position) {
            if (position <= 0) {
                throw new IllegalArgumentException("Neuron position must be positive, but was = " + position);
            }

            this.position = position;
        }

        @Override
        public float getLastPotential() {
            return this.signal;
        }

        @Override
        public void setLastPotential(final float signal) {
            this.signal = signal;
        }

        @Override
        public boolean isAccessible(final INeuron neuron) {
            return true;
        }

        @Override
        public int getPosition() {
            return this.position;
        }

        @Override
        public float run() {
            throw new UnsupportedOperationException();
        }

        @Override
        public float getDerived() {
            throw new UnsupportedOperationException();
        }

        @Override
        public float getDx() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setDx(final float dx) {
            throw new UnsupportedOperationException();
        }

        @Override
        public float[] educate(final float error, final float[] signal, final float educationSpeed) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addInputEdge(final IEdge inputEdge) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addOutputEdge(final IEdge outputEdge) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<IEdge> getInputEdges() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<IEdge> getOutputEdges() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ILayer getLayer() {
            return InputLayer.this;
        }

        @Override
        public String toString() {
            return "Input neuron [" + this.position + "]";
        }
    }
}
