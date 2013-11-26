package com.neuronet.impl;

import com.neuronet.api.*;
import com.neuronet.api.generator.EducationSample;
import com.neuronet.api.generator.INetInfo;
import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;

public class NetLearner {

    private static final Logger logger = LoggerFactory.getLogger(NetLearner.class);
    protected final INet net;
    protected final INetInfo netInfo;
    protected final float educationSpeed;

    public NetLearner(final INet net, final INetInfo netInfo) {
        this(net, netInfo, netInfo.getParameters().getDefaultEducationSpeed());
    }

    public NetLearner(final INet net, final INetInfo netInfo, final float educationSpeed) {
        this.net = net;
        this.netInfo = netInfo;
        this.educationSpeed = educationSpeed;
    }

    public void learn(final int learnRoundsThreshold, final float stopLearnError) {
        final IEducationDataSource educationDataSource = this.netInfo.getEducationDataSource();
        for (int i = 0; i < learnRoundsThreshold; i++) {
            for (final EducationSample sample : educationDataSource.getEducationData()) {
                educate(sample);
            }

            float error = 0;
            for (final EducationSample sample : educationDataSource.getTestData()) {
                final float[] result = Util.denormalizeOutputs(this.net.run(sample.getInputsSample()), netInfo.getNetConfiguration());
                error += Util.absMeanDifference(result, sample.getExpectedOutputs());
            }

            learnRoundCallback(i, error);

            if (error < stopLearnError) {
                return;
            }

            if (logger.isTraceEnabled()) {
                logger.trace("Current test error: {}", Util.toString(error));
            }
        }
    }

    protected void learnRoundCallback(final int learnRound, final float error) {

    }

    protected void educate(final EducationSample sample) {
        educate(sample.getInputsSample(), sample.getExpectedOutputs());
    }

    private void educate(final float[] inputData, final float[] expectedOutput) {
        float[] error = getNetError(inputData, Util.normalizeOutputs(expectedOutput, netInfo.getNetConfiguration()));
        final Deque<ILayer> layers = this.net.getLayers();

        // Do not run first layer, because it is an inputLayer (fake).
//        layers.removeFirst();

        // Running net in the reverse direction.
        ILayer currentLayer = layers.removeLast();
        for (final Iterator<ILayer> iterator = layers.descendingIterator(); iterator.hasNext(); ) {
            final ILayer previousLayer = iterator.next();
            error = educate(currentLayer, previousLayer.getLastResult(), error);

            currentLayer = previousLayer;
        }
    }

    public float[] educate(final ILayer layer, final float[] inputs, final float[] error) {
        final float[][] neuronErrors = new float[layer.getNeurons().size()][];
        for (int i = 0; i < neuronErrors.length; i++) {
            final INeuron neuron = layer.getNeurons().get(i);
            neuronErrors[i] = educate(neuron, inputs, error[i], this.educationSpeed);
        }

        final float[] layerError = new float[inputs.length];
        for (int i = 0; i < layerError.length; i++) {
            for (final float[] neuronError : neuronErrors) {
                layerError[i] += neuronError[i];
            }
        }

        return layerError;
    }

    public float[] educate(final INeuron neuron, final float[] inputs, final float error, final float educationSpeed) {
        if (inputs.length != neuron.getInputEdges().size()) {
            throw new RuntimeException();
        }
        final float[] commonNeuronError = new float[inputs.length];

        final float commonError = error * neuron.getDerived();
        final float errorCoefficient = commonError * educationSpeed;

        // Educating neuron dx.
//        this.setDx(this.getDx() + errorCoefficient); // Do we really need this?

        int i = 0;
        for (final IEdge edge : neuron.getInputEdges()) {

            //  Calculating common error.
            commonNeuronError[i] = edge.getWeight() * commonError;

            //  Changing synapses.
            edge.incrementWeight(inputs[i] * errorCoefficient);
            i++;
        }

        return commonNeuronError;
    }

    private float[] getNetError(final float[] inputData, final float[] expectedOutput) {
        final float[] result = this.net.run(inputData);

        //  Finding first ("previous") error.
        final float[] error = getError(result, expectedOutput);

        if (logger.isTraceEnabled()) {
            logger.trace("educate(): in: {}, exp: {}, act: {}", Arrays.toString(inputData), Arrays.toString(expectedOutput), Arrays.toString(result));
            logger.trace("educate(): run result: {}", Util.toString(result));
            logger.trace("educate(): error: {}", Util.toString(error));
        }

        return error;
    }

    private float[] getError(final float[] actualValues, final float[] expectedValues) {
        final float[] error = new float[actualValues.length];
        for (int i = 0; i < error.length; i++) {
            error[i] = expectedValues[i] - actualValues[i];
        }
        return error;
    }
}