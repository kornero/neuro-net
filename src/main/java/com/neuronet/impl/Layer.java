package com.neuronet.impl;

import com.neuronet.api.*;
import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Layer implements ILayer {

    private static final long serialVersionUID = 202204112013L;
    private static final Logger logger = LoggerFactory.getLogger(Layer.class);

    private static final int threads = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService service = Executors.newFixedThreadPool(threads);

    public final AtomicInteger edgesCounter = new AtomicInteger();
    public final AtomicInteger nullEdgesCounter = new AtomicInteger();

    private final List<INeuron> neurons;
    private final INet net;
    private final IFunction function;

    private volatile float[] lastResult;

    public Layer(final int neurons, final Collection<INeuron> inputNeurons, final IFunction function, final INet net) {
        this.neurons = new ArrayList<>(neurons);
        this.net = net;
        this.function = function;

        for (int i = 1; i <= neurons; i++) {
            final INeuron neuron = NeuronsFactory.createNeuron(function, net.getConfiguration().getDefaultDX(), i, this);
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
    public IFunction getFunction() {
        return this.function;
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
