package com.neuronet.impl;

import com.neuronet.api.*;
import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Layer implements ILayer {

    private static final long serialVersionUID = 202204112013L;
    private static final Logger logger = LoggerFactory.getLogger(Layer.class);
    public final AtomicInteger edgesCounter = new AtomicInteger();
    public final AtomicInteger nullEdgesCounter = new AtomicInteger();
    private final List<INeuron> neurons;
    private final INet net;
    private final ILayerConfiguration layerConfiguration;
    private volatile float[] lastResult;

    public Layer(final ILayerConfiguration layerConfiguration, final Collection<INeuron> inputNeurons,
                 final INetParameters netParameters, final INet net) {
        this.net = net;
        this.layerConfiguration = layerConfiguration;
        this.neurons = new ArrayList<>(layerConfiguration.getNeurons());

        for (int i = 1; i <= layerConfiguration.getNeurons(); i++) {
            final INeuron neuron = NeuronsFactory.createNeuron(
                    layerConfiguration.getFunction(),
                    netParameters.generateDx(),
                    i, this
            );
            for (INeuron inputNeuron : inputNeurons) {
                final IEdge edge = createEdge(inputNeuron, neuron);
                neuron.addInputEdge(edge);
                if (inputNeuron instanceof Neuron) {
                    inputNeuron.addOutputEdge(edge);
                }
            }
            this.getNeurons().add(neuron);
        }
    }

    @Override
    public float[] run() {
        final float[] result = new float[this.getNeurons().size()];

        int i = 0;
        for (final INeuron neuron : this.getNeurons()) {
            result[i] = neuron.run();
            i++;
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Result: {}", Arrays.toString(result));
        }
        return this.lastResult = result;
    }

    @Override
    public float[] educate(final float[] inputData, final float[] error, final float educationSpeed) {
        final float[][] tmp = new float[this.getNeurons().size()][];
        for (int i = 0; i < this.getNeurons().size(); i++) {
            tmp[i] = this.getNeurons().get(i).educate(error[i], inputData, educationSpeed);
        }

        final float[] result = new float[inputData.length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < this.getNeurons().size(); j++) {
                result[i] += tmp[j][i];
            }
        }

        return result;
    }

    @Override
    public float[] getLastResult() {
        return this.lastResult;
    }

    @Override
    public List<INeuron> getNeurons() {
        return this.neurons;
    }

    @Override
    public ILayerConfiguration getLayerConfiguration() {
        return this.layerConfiguration;
    }

    @Override
    public INet getNet() {
        return this.net;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }

    private IEdge createEdge(final INeuron inputNeuron, final INeuron outputNeuron) {
        final IEdge edge;
        if (inputNeuron.isAccessible(outputNeuron)) {
            edge = new Edge(inputNeuron, outputNeuron);
            edgesCounter.incrementAndGet();
        } else {
            edge = NullEdge.getInstance();
            nullEdgesCounter.incrementAndGet();
        }
        return edge;
    }
}
