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

    private static final long serialVersionUID = 163210112013L;
    private static final Logger logger = LoggerFactory.getLogger(Net.class);
    private final Deque<ILayer> layers = new LinkedList<>();
    private final INetParameters netParameters;
    private final INetConfiguration netConfiguration;
    private volatile float educationSpeed;

    public Net(final INetConfiguration netConfiguration, final Deque<ILayerConfiguration> layers) {
        this(netConfiguration, NetParameters.getDefaultParameters(), layers);
    }

    public Net(final INetConfiguration netConfiguration, INetParameters netParameters, final Deque<ILayerConfiguration> layerConfigurations) {
        if (netConfiguration == null) {
            throw new NullPointerException("NetConfiguration can't be null");
        }
        if (netParameters == null) {
            netParameters = NetParameters.getDefaultParameters();
        }
        if (layerConfigurations == null) {
            throw new NullPointerException("Layers can't be null");
        }
        if (layerConfigurations.getFirst().getNeurons() != netConfiguration.getInputsAmount()) {
            throw new IllegalArgumentException("Inputs amount in configuration and neurons amount in first layer is different: " +
                    "inputs=" + netConfiguration.getInputsAmount() + ", neurons=" + layerConfigurations.getFirst().getNeurons());
        }
        if (layerConfigurations.getLast().getNeurons() != netConfiguration.getInputsAmount()) {
            throw new IllegalArgumentException("Outputs amount in configuration and neurons amount in last layer is different: " +
                    "outputs=" + netConfiguration.getInputsAmount() + ", neurons=" + layerConfigurations.getLast().getNeurons());
        }

        this.netConfiguration = netConfiguration;
        this.netParameters = netParameters;
        this.educationSpeed = netParameters.getDefaultEducationSpeed();

        final ILayerConfiguration inputLayer = layerConfigurations.removeFirst();
        this.layers.addFirst(new InputLayer(inputLayer.getNeurons(), this));

        for (final ILayerConfiguration layerConfiguration : layerConfigurations) {
            final Collection<INeuron> inputNeurons = this.layers.getLast().getNeurons();
            this.layers.add(new Layer(layerConfiguration, inputNeurons, netParameters, this));
        }
    }

    @Override
    public float[] run(final float[] inputData) {
        if (logger.isTraceEnabled()) {
            logger.trace("run(): {}", Util.toString(inputData));
        }
        final float[] tempData = Util.normalizeInputs(inputData, this.getNetConfiguration());

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

        // Normalize expected outputs;
        final float[] tempData = Util.normalizeOutputs(expectedOutput, this.getNetConfiguration());

        //  Finding first ("previous") error.
        float[] error = new float[result.length];
        for (int i = 0; i < error.length; i++) {
            error[i] = tempData[i] - result[i];
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
    public INetParameters getNetParameters() {
        return this.netParameters;
    }

    @Override
    public INetConfiguration getNetConfiguration() {
        return this.netConfiguration;
    }

    @Override
    public Deque<ILayer> getLayers() {
        return new ArrayDeque<>(this.layers);
    }

    @Override
    public void setEducationSpeed(final float educationSpeed) {
        this.educationSpeed = educationSpeed;
    }

    private void setInputData(final float[] inputData) {
        final Collection<INeuron> inputs = this.getLayers().getFirst().getNeurons();
        if (inputData.length != this.getNetConfiguration().getInputsAmount()) {
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
