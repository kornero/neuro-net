package com.neuronet.impl;

import com.neuronet.api.IEdge;
import com.neuronet.api.ILayer;
import com.neuronet.api.INet;
import com.neuronet.api.INeuron;
import com.neuronet.util.FunctionType;
import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Layer implements ILayer {

    private static final long serialVersionUID = 4801505622279837569L;

    private static final Logger logger = LoggerFactory.getLogger(Layer.class);

    private static final int threads = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService service = Executors.newFixedThreadPool(threads);

    public final AtomicInteger edgesCounter = new AtomicInteger();
    public final AtomicInteger nullEdgesCounter = new AtomicInteger();

    private final List<INeuron> neurons;
    private final INet net;
    private final FunctionType functionType;

    private float[] lastResult;

    public Layer(final int neurons, final Collection<INeuron> inputNeurons, final FunctionType functionType, final INet net) {
        this.neurons = new ArrayList<>(neurons);
        this.net = net;
        this.functionType = functionType;

        for (int i = 1; i <= neurons; i++) {
            final INeuron neuron = new Neuron(functionType, this, (short) i);
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
    public float[] runLayer() {
        final float[] result = new float[this.getNeurons().size()];
/*   */
        int i = 0;
        for (final INeuron neuron : this.neurons) {
            result[i] = neuron.runNeuron();
            i++;
        }
/*
        final int neuronsSize = this.neurons.size();
        final int step = neuronsSize / threads + neuronsSize % threads;

        final CountDownLatch countDownLatch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            final int start = i * step;
//            service.submit(new Runnable() {
//                @Override
//                public void run() {
                    for (int j = start; j < start + step && j < neuronsSize; j++) {
                        result[j] = neurons.get(j).runNeuron();
                    }
                    countDownLatch.countDown();
//                }
//            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.warn("runLayer(): Interrupted.", e);
            Thread.currentThread().interrupt();
        }

 /**/
//        System.out.println(this.neurons.size());
//        System.out.println("--------------");
        return this.lastResult = result;
    }

    @Override
    public float[] educate(float[] inputData, float[] error) {

        final float[][] tmp = new float[this.getNeurons().size()][];
        for (int i = 0; i < this.getNeurons().size(); i++) {
            tmp[i] = this.getNeurons().get(i).educate(error[i], inputData);
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
    public INet getNet() {
        return this.net;
    }

    @Override
    public FunctionType getFunctionType() {
        return this.functionType;
    }

    @Override
    public void setEducationSpeed(final float educationSpeed) {
        for (final INeuron neuron : getNeurons()) {
            neuron.setEducationSpeed(educationSpeed);
        }
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
