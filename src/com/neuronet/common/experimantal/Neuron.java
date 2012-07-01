package com.neuronet.common.experimantal;

import com.neuronet.common.api.INeuron;
import com.neuronet.util.FunctionType;
import com.neuronet.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Neuron implements INeuron {

    private static final Logger logger = LoggerFactory.getLogger(Neuron.class);

    private static final float educationSpeed = 0.2f;

    private final List<Edge> inputEdgeList = new ArrayList<Edge>();
    private final List<Edge> outputEdgeList = new ArrayList<Edge>();

    private final FunctionType functionType;
    private final float[] weights;
    private final float alfa;
    private final float b;

    private float lastPotential = 0;

    public Neuron(final float[] weights, final float b, final FunctionType functionType, final float alfa) {
        this.weights = weights;
        this.b = b;
        this.alfa = alfa;
        this.functionType = functionType;
    }

    @Override
    public float[] getWeights() {
        /*
        final float[] weights = new float[inputEdgeList.size()];
        for (int i = 0, wLength = weights.length; i < wLength; i++) {
            weights[i] = inputEdgeList.get(i).getWeight();
        }
        return weights;
        */
        return weights;
    }

    @Override
    public float getFunction(float[] inputData) {
        this.lastPotential = Functions.multiply(inputData, weights, b);
        return Functions.getFunction(this.lastPotential, this.functionType, this.alfa);
    }

    @Override
    public float getDerived() {
        return Functions.getDerived(this.lastPotential, this.functionType, this.alfa);
    }

    @Override
    public float[] educate(float error, float[] _s) {
        float[] tmp = new float[this.weights.length - 1];

        //  подсчитали ошибkу
        for (int i = 1; i < this.weights.length; i++) {
            tmp[i - 1] = this.weights[i] * error;
        }

        //  изменили синапсы
        this.educateWeights(error, _s);

        return tmp;
    }

    /**
     * Synapse educating.
     */
    private void educateWeights(float error, float[] _s) {
        this.weights[0] += error * educationSpeed;

        for (int i = 1; i < this.weights.length; i++) {
            this.weights[i] += _s[i - 1] * error * educationSpeed;
        }
    }
}
