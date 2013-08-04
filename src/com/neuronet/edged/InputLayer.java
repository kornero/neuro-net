package com.neuronet.edged;

import com.neuronet.edged.api.IEdge;
import com.neuronet.edged.api.ILayer;
import com.neuronet.edged.api.INeuron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InputLayer implements ILayer {

    private static final Logger logger = LoggerFactory.getLogger(InputLayer.class);

    private final List<INeuron> neurons;

    private float[] lastResult;

    public InputLayer(int inputs) {
        this.neurons = new ArrayList<INeuron>(inputs);
        for (int i = 0; i < inputs; i++) {
            neurons.add(new InputNeuron());
        }
    }

    @Override
    public Collection<INeuron> getNeurons() {
        return neurons;
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

    private static class InputNeuron implements INeuron {

        private float signal = 0;

        @Override
        public float getLastPotential() {
            return this.signal;
        }

        @Override
        public void setLastPotential(float signal) {
            this.signal = signal;
        }

        @Override
        public boolean isAccessible(INeuron neuron) {
            return true;
        }

        @Override
        public short getPosition() {
            throw new UnsupportedOperationException();
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
    }
}
