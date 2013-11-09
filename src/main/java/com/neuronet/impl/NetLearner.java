package com.neuronet.impl;

import com.neuronet.api.IEdge;
import com.neuronet.api.ILayer;
import com.neuronet.api.INet;
import com.neuronet.api.INeuron;
import com.neuronet.api.generator.EducationSample;
import com.neuronet.api.generator.NetInfo;
import com.neuronet.util.Util;
import com.neuronet.view.NetGraphPanel;
import com.neuronet.view.Visualizer;
import com.xeiam.xchart.Chart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.Iterator;

public class NetLearner {

    private static final Logger logger = LoggerFactory.getLogger(NetLearner.class);

    private final INet net;
    private final NetInfo netInfo;
    private final float educationSpeed;

    public NetLearner(INet net, NetInfo netInfo, float educationSpeed) {
        this.net = net;
        this.netInfo = netInfo;
        this.educationSpeed = educationSpeed;
    }

    public void learn(final int i) {
        final NetGraphPanel panel = new NetGraphPanel(net, netInfo);
        final Chart chart = panel.getChart();
        chart.setChartTitle("Iteration = 0");

        Visualizer.createFrame(panel);

        for (int j = 0; j < i; j++) {
            for (final EducationSample sample : netInfo.getEducationData()) {
                educate(sample);
            }

            float error = 0;
            for (final EducationSample sample : netInfo.getTestData()) {
                final float[] result = this.net.run(sample.getInputsSample());
                error += Util.absMeanDifference(result, sample.getExpectedOutputs());
            }

            if (i % 500 == 0) {
                chart.setChartTitle("Iteration = " + j + ", test error = " + error);
                panel.repaint();
            }

            if (logger.isTraceEnabled()) {
                logger.trace("Current test error: {}", Util.toString(error));
            }
        }
    }

    private void educate(final EducationSample sample) {
        educate(sample.getInputsSample(), sample.getExpectedOutputs());
    }

    private void educate(final float[] inputData, final float[] expectedOutput) {
        float[] error = getNetError(inputData, expectedOutput);
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