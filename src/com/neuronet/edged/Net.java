package com.neuronet.edged;

import com.neuronet.edged.api.*;
import com.neuronet.util.FunctionType;
import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

public class Net implements INet {

    private static final Logger logger = LoggerFactory.getLogger(Net.class);
    private final Deque<ILayer> layers = new LinkedList<ILayer>();
    private final IConfiguration configuration;

    public Net(final int inputs) {
        this(inputs, Configuration.getDefaultConfiguration());
    }

    public Net(final int inputs, final IConfiguration configuration) {
        layers.addFirst(new InputLayer(inputs, this));
        this.configuration = configuration;
    }

    @Override
    public void addLayer(int neurons, FunctionType functionType) {
        final Collection<INeuron> inputNeurons = layers.getLast().getNeurons();
        layers.add(new Layer(neurons, inputNeurons, functionType, this));
    }

    @Override
    public float[] runNet(float[] inputData) {
        if (logger.isTraceEnabled()) {
            logger.trace("runNet(): " + Util.toString(inputData));
        }
        inputData = inputData.clone();
        Util.normalize(inputData);

        this.setInputData(inputData);

        float[] result = inputData;
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
            throw new RuntimeException(inputData.length + "!=" + inputs.size());
        }

        int i = 0;
        for (final INeuron n : inputs) {
            n.setLastPotential(inputData[i++]);
        }
    }
}
