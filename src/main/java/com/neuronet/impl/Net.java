package com.neuronet.impl;

import com.neuronet.api.*;
import com.neuronet.api.generator.EducationSample;
import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

public class Net implements INet {

    private static final long serialVersionUID = 202704112013L;
    private static final Logger logger = LoggerFactory.getLogger(Net.class);

    private final Deque<ILayer> layers = new LinkedList<>();
    private final IConfiguration configuration;
    private final float maxInputValue;

    private volatile float educationSpeed;

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
        this.educationSpeed = configuration.getDefaultEducationSpeed();
    }

    @Override
    public void addLayer(final int neurons, IFunction function) {
        final Collection<INeuron> inputNeurons = this.getLayers().getLast().getNeurons();
        layers.add(new Layer(neurons, inputNeurons, function, this));
    }

    @Override
    public float[] run(final float[] inputData) {
        if (logger.isTraceEnabled()) {
            logger.trace("run(): {}", Util.toString(inputData));
        }
        final float[] tempData = inputData.clone();
        Util.normalize(tempData, this.maxInputValue);

        this.setInputData(tempData);

        float[] result = tempData;
        for (final ILayer layer : layers) {
            result = layer.run();
        }

        return result;
    }

    @Override
    public float[] educate(final EducationSample educationSample) {
        return educate(educationSample.getInputsSample(), educationSample.getExpectedOutputs());
    }

    @Override
    public float[] educate(final float[] inputData, final float[] expectedOutput) {
        final float[] result = this.run(inputData);

        //  Finding first ("previous") error.
        float[] error = new float[result.length];
        for (int i = 0; i < error.length; i++) {
            error[i] = expectedOutput[i] - result[i];
        }

        if (logger.isTraceEnabled()) {
            logger.trace("educate(): run result: {}", Util.toString(result));
            logger.trace("educate(): error: {}", Util.toString(error));
        }

        // Running net in the reverse direction.
        // Do not run first layer, because it is an inputLayer (fake).
        final ILayer[] layersArray = layers.toArray(new ILayer[layers.size()]);
        for (int i = layersArray.length - 1; i > 0; i--) {
            error = layersArray[i].educate(layersArray[i - 1].getLastResult(), error, this.educationSpeed);
        }
        return error;
    }

    @Override
    public IConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public Deque<ILayer> getLayers() {
        return new ArrayDeque<>(this.layers);
    }

    @Override
    public int getInputsAmount() {
        return this.getLayers().getFirst().getNeurons().size();
    }

    @Override
    public int getOutputsAmount() {
        return this.getLayers().getLast().getNeurons().size();
    }

    @Override
    public void setEducationSpeed(final float educationSpeed) {
        this.educationSpeed = educationSpeed;
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
            logger.debug("printStatistic(): edges = {}, nullEdges = {}, % = {}",
                    edges, nullEdges, Util.toString(nullEdges * 100f / (edges + nullEdges)));
        }
    }

    private void setInputData(final float[] inputData) {
        final Collection<INeuron> inputs = this.getLayers().getFirst().getNeurons();
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
