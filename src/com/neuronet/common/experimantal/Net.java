package com.neuronet.common.experimantal;

import com.neuronet.common.api.ILayer;
import com.neuronet.common.api.INet;
import com.neuronet.common.api.INeuron;
import com.neuronet.util.FunctionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

public class Net implements INet {

    private static final Logger logger = LoggerFactory.getLogger(Net.class);

    private final Deque<ILayer> layers = new LinkedList<ILayer>();

    public void addLayer(int neurons, FunctionType functionType, float alfa) {
        Collection<INeuron> inputNeurons = layers.getLast().getNeurons();
        layers.add(new Layer(neurons, inputNeurons, functionType, alfa));
    }

    @Override
    public void addLayer(int neurons, int inputNeurons, FunctionType functionType, float alfa) {
        if (layers.isEmpty()) {
            layers.add(new com.neuronet.common.classic.Layer(neurons, inputNeurons, functionType, alfa, new NeuronsFactory()));
        } else {
            addLayer(neurons, functionType, alfa);
        }
    }

    @Override
    public void setLayer(int layerIndex, int neurons, int inputNeurons, FunctionType functionType, float alfa) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float[] runNet(float[] inputData) {
        float[] result = inputData;

        for (final ILayer layer : layers) {
            result = layer.runLayer(result);
        }

        return result;
    }

    @Override
    public float[] educate(float[] expectedOutput, float[] inputData) {
        float[] er = this.runNet(inputData);

        //  нашли первую "пред" ошибку (эпселент)
        for (int i = 0; i < er.length; i++) {
            er[i] = expectedOutput[i] - er[i];
        }

        final ILayer[] layersArray = layers.toArray(new ILayer[layers.size()]);

        //  прогоняем сеть в обратном направлении...
        for (int i = layersArray.length - 1; i > 0; i--) {
            er = layersArray[i].educate(layersArray[i - 1].getLastResult(), er);
        }

        //  нулевой слой тоже
        return layersArray[0].educate(inputData, er);
    }
}
