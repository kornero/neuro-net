package com.neuronet.edged;

import com.neuronet.edged.api.ILayer;
import com.neuronet.edged.api.INeuron;
import com.neuronet.util.FunctionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Layer implements ILayer {

    private static final Logger logger = LoggerFactory.getLogger(Layer.class);

    private final List<INeuron> neurons;

    private float[] lastResult;

    public Layer(final int neurons, final Collection<INeuron> inputNeurons, final FunctionType functionType, final float alfa) {
        this.neurons = new ArrayList<INeuron>(neurons);
        for (int i = 0; i < neurons; i++) {
            final INeuron neuron = new Neuron(0.5f, functionType, alfa);
            for (INeuron n : inputNeurons) {
                final Edge edge = neuron.createInputEdge(n, 0.5f);
                if (n instanceof Neuron) {
                    n.addOutputEdge(edge);
                }
            }
            this.getNeurons().add(neuron);
        }
    }

    @Override
    public float[] runLayer() {
        final float[] result = new float[this.getNeurons().size()];

        int i = 0;
        for (final INeuron neuron : this.neurons) {
            result[i] = neuron.getFunction();
            i++;
        }

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
}
