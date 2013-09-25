package com.neuronet.edged;

import com.neuronet.edged.api.IEdge;
import com.neuronet.edged.api.ILayer;
import com.neuronet.edged.api.INet;
import com.neuronet.edged.api.INeuron;
import com.neuronet.util.FunctionType;
import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class InputLayer implements ILayer {

    private static final long serialVersionUID = 3186131631068845365L;

    private static final Logger logger = LoggerFactory.getLogger(InputLayer.class);

    private final List<INeuron> neurons;
    private final INet net;
    private float[] lastResult;

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
    public FunctionType getFunctionType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEducationSpeed(final float educationSpeed) {
        for (final INeuron neuron : getNeurons()) {
            neuron.setEducationSpeed(educationSpeed);
        }
    }

    @Override
    public float[] runLayer() {
        this.lastResult = new float[neurons.size()];
        int i = 0;
        for (INeuron n : getNeurons()) {
            lastResult[i] = n.getLastPotential();
            i++;
        }
        return lastResult;
    }

    @Override
    public float[] getLastResult() {
        return this.lastResult;
    }

    @Override
    public float[] educate(float[] inputData, float[] error) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }

    private class InputNeuron implements INeuron {

        private static final long serialVersionUID = -3624025048831900574L;

        private final short position;
        private float signal = 0;

        private InputNeuron(final short position) {
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
        public void setLastPotential(float signal) {
            this.signal = signal;
        }

        @Override
        public void setEducationSpeed(float educationSpeed) {
        }

        @Override
        public boolean isAccessible(INeuron neuron) {
            return true;
        }

        @Override
        public short getPosition() {
            return this.position;
        }

        @Override
        public float runNeuron() {
            throw new UnsupportedOperationException();
        }

        @Override
        public float getDerived() {
            throw new UnsupportedOperationException();
        }

        @Override
        public float getAlfa() {
            throw new UnsupportedOperationException();
        }

        @Override
        public float getDX() {
            throw new UnsupportedOperationException();
        }

        @Override
        public float[] educate(float error, float[] signal) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IEdge createInputEdge(INeuron inputNeuron, float weight) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addInputEdge(IEdge inputEdge) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addOutputEdge(IEdge outputEdge) {
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
            return Util.toString(this);
        }
    }
}
