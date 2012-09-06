package com.neuronet.classic.impl;

import com.neuronet.classic.api.ILayer;
import com.neuronet.classic.api.INeuron;
import com.neuronet.classic.api.INeuronsFactory;
import com.neuronet.util.FunctionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.neuronet.util.Util.randomFloats;

public class Layer implements ILayer {

    private static final Logger logger = LoggerFactory.getLogger(Layer.class);

    private final List<INeuron> neurons = new ArrayList<INeuron>();

    private final FunctionType functionType;
    private final int inputNeurons;
    private final float alfa;

    private float[] lastResult;

    public Layer(final int neurons, final int inputNeurons, final FunctionType functionType, final float alfa, final INeuronsFactory factory) {
        this.inputNeurons = inputNeurons;
        this.functionType = functionType;
        this.alfa = alfa;
        for (int i = 0; i < neurons; i++) {
            float[] w = randomFloats(inputNeurons + 1);
            this.neurons.add(factory.newNeuron(w, 1, functionType, alfa));
        }
    }

    public Layer(final String filename) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(filename));
        final int neuronsAmount = Integer.valueOf(file.readLine());

        this.inputNeurons = Integer.valueOf(file.readLine());
        this.functionType = FunctionType.valueOf(file.readLine());
        this.alfa = Float.valueOf(file.readLine());

        this.neurons.clear();

        String ws;
        String temp = "";

        for (int i = 0; i < neuronsAmount; i++) {
            float[] w = new float[this.inputNeurons + 1];

            int j = 0;
            ws = file.readLine();

            for (int k = 0; k < ws.length(); k++) {
                if (ws.charAt(k) != ' ') {
                    temp += ws.charAt(k);
                } else {
                    w[j] = Float.valueOf(temp);
                    j++;
                    temp = "";
                }
            }
            if (!temp.isEmpty()) {
                w[neuronsAmount - 1] = Float.valueOf(temp);
            }

            this.neurons.add(new Neuron(w, 1, this.functionType, this.alfa));
        }
    }

    @Override
    public float[] runLayer(final float[] inputData) {
        logger.debug("runLayer: " + Arrays.toString(inputData));
        this.lastResult = new float[this.neurons.size()];

        for (int i = 0; i < this.neurons.size(); i++) {
            this.lastResult[i] = this.neurons.get(i).getFunction(inputData);
        }

        return this.lastResult;
    }

    @Override
    public float[] educate(final float[] inputData, final float[] error) {
        float[] errors = new float[this.neurons.size()];

        float[] rez = new float[this.inputNeurons];

        float[][] tmp = new float[this.neurons.size()][];

        for (int i = 0; i < this.neurons.size(); i++) {
            errors[i] = this.neurons.get(i).getDerived() * error[i];
            tmp[i] = this.neurons.get(i).educate(errors[i], inputData);
        }

        for (int j = 0; j < this.inputNeurons; j++) {
            for (int i = 0; i < this.neurons.size(); i++) {
                rez[j] += tmp[i][j];
            }
        }

        return rez;
    }

    @Override
    public float[] getLastResult() {
        return this.lastResult;
    }

    @Override
    public Collection<INeuron> getNeurons() {
        return this.neurons;
    }

    public void saveLayer(final String filename) throws IOException {
        File f = new File(filename);
        f.delete();

        BufferedWriter file = new BufferedWriter(new FileWriter(f));

        file.write(this.neurons.size());
        file.write("\n");

        file.write(this.inputNeurons);
        file.write("\n");

        file.write(this.functionType.name());
        file.write("\n");

        file.write(this.alfa + "");
        file.write("\n");

        for (final INeuron neuron : this.neurons) {
            for (int j = 0; j < neuron.getWeights().length; j++) {
                file.write(neuron.getWeights()[j] + "");
                file.write(" ");
            }
            file.write("\n");
        }
    }
}
