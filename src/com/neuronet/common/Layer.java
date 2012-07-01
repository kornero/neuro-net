package com.neuronet.common;

import com.neuronet.NeuroNet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Layer {

    private static final Logger logger = LoggerFactory.getLogger(Layer.class);

    private final List<Neuron> neurons = new ArrayList<Neuron>();

    private Neuron[] n;
    private int inputNeurons;
    private int functionType;
    private float alfa;

    private float[] lastResult;

    public Layer(int neurons, int inputNeurons, int functionType, float alfa) {
        this.inputNeurons = inputNeurons;
        this.functionType = functionType;
        this.alfa = alfa;
        n = new Neuron[neurons];
        for (int i = 0; i < n.length; i++) {
            float[] w = this.randomW(this.inputNeurons + 1);
            n[i] = new Neuron(w, 1, this.functionType, this.alfa);
            this.neurons.add(n[i]);
        }
    }

    public Layer(String filename) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(filename));
        final int neuronsAmount = Integer.valueOf(file.readLine());
        this.inputNeurons = Integer.valueOf(file.readLine());
        this.functionType = Integer.valueOf(file.readLine());
        this.alfa = Float.valueOf(file.readLine());

        n = new Neuron[neuronsAmount];

        String ws;
        String temp = "";

        for (int i = 0; i < this.n.length; i++) {
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

            n[i] = new Neuron(w, 1, this.functionType, this.alfa);
        }
    }

    public float[] runLayer(float[] inputData) {
        this.lastResult = new float[this.neurons.size()];

        for (int i = 0; i < this.lastResult.length; i++) {
            this.lastResult[i] = this.neurons.get(i).getS(inputData);
        }

        return this.lastResult;
    }

    public float[] educate(float[] inputData, float[] error) {
        float[] errors = new float[this.neurons.size()];

        float[] rez = new float[this.inputNeurons];

        float[][] tmp = new float[this.neurons.size()][];

        for (int i = 0; i < this.n.length; i++) {
            errors[i] = this.n[i].getF() * error[i];
            tmp[i] = this.n[i].educate(errors[i], inputData);
        }

        for (int j = 0; j < this.inputNeurons; j++) {
            for (int i = 0; i < this.n.length; i++) {
                rez[j] += tmp[i][j];
            }
        }

        return rez;
    }

    public float[] getLastResult() {
        return this.lastResult;
    }

    private float[] randomW(final int inputsAmount) {
        final Random rnd = new Random(System.currentTimeMillis());
        float[] tmp = new float[inputsAmount];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = ((NeuroNet.rand / 2) - rnd.nextInt(NeuroNet.rand)) / NeuroNet.div;
        }
        return tmp;
    }

    public void saveLayer(String filename) throws IOException {
        File f = new File(filename);
        f.delete();

        BufferedWriter file = new BufferedWriter(new FileWriter(f));

        file.write(this.neurons.size());
        file.write("\n");

        file.write(this.inputNeurons);
        file.write("\n");

        file.write(this.functionType);
        file.write("\n");

        file.write(this.alfa + "");
        file.write("\n");

        for (final Neuron neuron : this.neurons) {
            for (int j = 0; j < neuron.getW().length; j++) {
                file.write(neuron.getW()[j] + "");
                file.write(" ");
            }
            file.write("\n");
        }
    }
}
