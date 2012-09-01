package com.neuronet.edged;

import com.neuronet.edged.api.INeuron;
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

    @Override
    public Edge createInputEdge(final INeuron inputNeuron, final float weight) {
        final Edge edge = new Edge(inputNeuron, this, weight);
        this.addInputEdge(edge);
        return edge;
    }

    @Override
    public void addInputEdge(final Edge inputEdge) {
        this.inputEdgeList.add(inputEdge);
    }

    @Override
    public void addOutputEdge(final Edge outputEdge) {
        this.outputEdgeList.add(outputEdge);
    }

    @Override
    public float getFunction() {
        float potential = b;
        for (Edge edge : inputEdgeList) {
            potential += edge.getWeight() * edge.getInput().getLastPotential();
        }
        this.lastPotential = potential;
        //this.lastPotential = Functions.multiply(inputData, getWeights(), b);
        return Functions.getFunction(this.getLastPotential(), this.functionType, this.alfa);
    }

    @Override
    public float getDerived() {
        return Functions.getDerived(this.getLastPotential(), this.functionType, this.alfa);
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

    @Override
    public float getLastPotential() {
        return lastPotential;
    }

    @Override
    public void setLastPotential(float signal) {
        this.lastPotential = signal;
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

    private float[] getWeights() {
        final float[] weights = new float[inputEdgeList.size() + 1];
        weights[0] = this.b;
        for (int i = 0; i < inputEdgeList.size(); i++) {
            weights[i + 1] = inputEdgeList.get(i).getWeight();
        }
        return weights;
    }
}
