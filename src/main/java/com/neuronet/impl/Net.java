package com.neuronet.impl;

import com.neuronet.api.*;
import com.neuronet.util.FunctionType;
import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

public class Net implements INet {

    private static final long serialVersionUID = -759059968976888401L;

    private static final Logger logger = LoggerFactory.getLogger(Net.class);

    private final Deque<ILayer> layers = new LinkedList<>();
    private final IConfiguration configuration;
    private final float maxInputValue;

    public Net(final int inputs) {
        this(inputs, 0, Configuration.getDefaultConfiguration());
    }

    public Net(final int inputs, final float maxInputValue) {
        this(inputs, maxInputValue, Configuration.getDefaultConfiguration());
    }

    public Net(final int inputs, final IConfiguration configuration) {
        this(inputs, 0, configuration);
    }

    public Net(final int inputs, final float maxInputValue, final IConfiguration configuration) {
        layers.addFirst(new InputLayer(inputs, this));
        this.configuration = configuration;
        this.maxInputValue = maxInputValue;
    }

    @Override
    public void addLayer(int neurons, FunctionType functionType) {
        final Collection<INeuron> inputNeurons = layers.getLast().getNeurons();
        layers.add(new Layer(neurons, inputNeurons, functionType, this));
    }

    @Override
    public float[] runNet(final float[] inputData) {
        if (logger.isTraceEnabled()) {
            logger.trace("runNet(): " + Util.toString(inputData));
        }
        final float[] tempData = inputData.clone();
        Util.normalize(tempData, this.maxInputValue);

        this.setInputData(tempData);

        float[] result = tempData;
        for (final ILayer layer : layers) {
            result = layer.runLayer();
        }

        return result;
    }

    @Override
    public float[] educate(final float[] expectedOutput, final float[] inputData) {
        final float[] result = this.runNet(inputData);

        //  Finding first ("previous") error.
        float[] error = new float[result.length];
        for (int i = 0; i < error.length; i++) {
            error[i] = expectedOutput[i] - result[i];
        }

        if (logger.isTraceEnabled()) {
            logger.trace("educate(): run result: " + Util.toString(result));
            logger.trace("educate(): error: " + Util.toString(error));
        }

        // Running net in the reverse direction.
        // Do not run first layer, because it is an inputLayer (fake).
        final ILayer[] layersArray = layers.toArray(new ILayer[layers.size()]);
        for (int i = layersArray.length - 1; i > 0; i--) {
            error = layersArray[i].educate(layersArray[i - 1].getLastResult(), error);
        }
        return error;
    }

    @Override
    public IConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public Deque<ILayer> getLayers() {
        return this.layers;
    }

    @Override
    public int getInputsAmount() {
        return this.layers.getFirst().getNeurons().size();
    }

    @Override
    public int getOutputsAmount() {
        return this.layers.getLast().getNeurons().size();
    }

    @Override
    public void setEducationSpeed(final float educationSpeed) {
        for (final ILayer layer : getLayers()) {
            layer.setEducationSpeed(educationSpeed);
        }
    }

    public void printStatistic() {
        if (logger.isDebugEnabled()) {
            int edges = 0;
            int nullEdges = 0;
            for (final ILayer iLayer : layers) {
                if (iLayer instanceof Layer) {
                    edges += ((Layer) iLayer).edgesCounter.get();
                    nullEdges += ((Layer) iLayer).nullEdgesCounter.get();
                }
            }
            logger.debug("printStatistic(): edges = " + edges + ", nullEdges = " + nullEdges + ", % = "
                    + Util.toString(nullEdges * 100f / (edges + nullEdges)));
        }
    }

    private void setInputData(final float[] inputData) {
        final Collection<INeuron> inputs = layers.getFirst().getNeurons();
        if (inputData.length != inputs.size()) {
            throw new RuntimeException("Inputs amount is: " + inputs.size() + ", but was: " + inputData.length);
        }

        int i = 0;
        for (final INeuron n : inputs) {
            n.setLastPotential(inputData[i++]);
        }
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
