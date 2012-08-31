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
    private final float alfa;

    private float b;

    private float lastPotential = 0;

    public Neuron(final float dx, final FunctionType functionType, final float alfa) {
        this.b = dx;
        this.alfa = alfa;
        this.functionType = functionType;
    }

    public Edge createInputEdge(final INeuron inputNeuron, final float weight) {
        final Edge edge = new Edge(inputNeuron, this, weight);
        this.inputEdgeList.add(edge);
        return edge;
    }

    public void addInputEdge(final Edge inputEdge) {
        this.inputEdgeList.add(inputEdge);
    }

    public void addOutputEdge(final Edge outputEdge) {
        this.outputEdgeList.add(outputEdge);
    }

    @Override
    public float[] getWeights() {
        final float[] weights = new float[inputEdgeList.size() + 1];
        weights[0] = this.b;
        for (int i = 0; i < inputEdgeList.size(); i++) {
            weights[i + 1] = inputEdgeList.get(i).getWeight();
        }
        return weights;
    }

    @Override
    public float getFunction(float[] inputData) {
        this.lastPotential = Functions.multiply(inputData, getWeights(), b);
        return Functions.getFunction(this.lastPotential, this.functionType, this.alfa);
    }

    @Override
    public float getDerived() {
        return Functions.getDerived(this.lastPotential, this.functionType, this.alfa);
    }

    @Override
    public float[] educate(float error, float[] _s) {
        final float[] weights = this.getWeights();
        float[] tmp = new float[weights.length - 1];

        //  подсчитали ошибkу
        for (int i = 1; i < weights.length; i++) {
            tmp[i - 1] = weights[i] * error;
        }

        //  изменили синапсы
        this.educateWeights(error, _s);

        return tmp;
    }

    /**
     * Synapse educating.
     */
    private void educateWeights(float error, float[] _s) {
        this.b += error * educationSpeed;

        for (int i = 0; i < _s.length; i++) {
            this.inputEdgeList.get(i).incrementWeight(_s[i] * error * educationSpeed);
        }
    }
}
