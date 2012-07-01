package com.neuronet.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Net {

    private static final Logger logger = LoggerFactory.getLogger(Net.class);

    private final List<Layer> layers = new ArrayList<Layer>();

    private final List<Neuron> neurons = new ArrayList<Neuron>();
    private final List<Neuron> inputNeurons = new ArrayList<Neuron>();
    private final List<Neuron> outputNeurons = new ArrayList<Neuron>();

    public void addLayer(final int neurons, final int inputNeurons, final int functionType, final float alfa) {
        layers.add(new Layer(neurons, inputNeurons, functionType, alfa));
    }

    public void setLayer(final int layerIndex, final int neurons, final int inputNeurons, final int functionType, final float alfa) {
        layers.set(layerIndex, new Layer(neurons, inputNeurons, functionType, alfa));
    }

    public float[] runNet(final float[] inputData) {
        float[] result = inputData;

        for (final Layer layer : layers) {
            result = layer.runLayer(result);
        }

        return result;
    }

    public float[] educate(final float[] expectedOutput, final float[] inputData) {
        float[] er = this.runNet(inputData);

        //  нашли первую "пред" ошибку (эпселент)
        for (int i = 0; i < er.length; i++) {
            er[i] = expectedOutput[i] - er[i];
        }

        final Layer[] layersArray = layers.toArray(new Layer[layers.size()]);

        //  прогоняем сеть в обратном направлении...
        for (int i = layersArray.length - 1; i > 0; i--) {
            er = layersArray[i].educate(layersArray[i - 1].getLastResult(), er);
        }

        //  нулевой слой тоже
        return layersArray[0].educate(inputData, er);
    }

    public void loadNet(final String pathName) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(pathName + "\\net.txt"));

        final int layersAmount = Integer.valueOf(file.readLine());

        layers.clear();
        for (int i = 0; i < layersAmount; i++) {
            layers.add(new Layer(pathName + "\\Layer" + i + ".txt"));
        }
    }

    public void saveNet(final String pathName) throws IOException {
        File f = new File(pathName + "\\net.txt");
        f.delete();

        BufferedWriter file = new BufferedWriter(new FileWriter(f));
        file.write(layers.size());
        file.write("\n");

        for (int i = 0, layersSize = layers.size(); i < layersSize; i++) {
            final Layer layer = layers.get(i);
            layer.saveLayer(pathName + "\\Layer" + i + ".txt");
        }
    }
}
