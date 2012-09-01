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

    private final List<INeuron> neurons = new ArrayList<INeuron>();

    private float[] lastResult;

    public Layer(final int neurons, final Collection<INeuron> inputNeurons, final FunctionType functionType, final float alfa) {
        for (int i = 0; i < neurons; i++) {
            final Neuron neuron = new Neuron(0.5f, functionType, alfa);
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
        this.lastResult = new float[this.getNeurons().size()];

        for (int i = 0; i < this.getNeurons().size(); i++) {
            this.lastResult[i] = this.getNeurons().get(i).getFunction();
        }

        return this.lastResult;
    }

    @Override
    public float[] educate(float[] inputData, float[] error) {
        float[] errors = new float[this.getNeurons().size()];

        float[] rez = new float[inputData.length];

        float[][] tmp = new float[this.getNeurons().size()][];

        for (int i = 0; i < this.getNeurons().size(); i++) {
            errors[i] = this.getNeurons().get(i).getDerived() * error[i];
            tmp[i] = this.getNeurons().get(i).educate(errors[i], inputData);
        }

        for (int j = 0; j < rez.length; j++) {
            for (int i = 0; i < this.getNeurons().size(); i++) {
                rez[j] += tmp[i][j];
            }
        }

        return rez;
    }

    @Override
    public float[] getLastResult() {
        return this.lastResult;
    }

    public List<INeuron> getNeurons() {
        return neurons;
    }
}
