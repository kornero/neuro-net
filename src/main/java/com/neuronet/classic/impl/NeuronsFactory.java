package com.neuronet.classic.impl;

import com.neuronet.classic.api.INeuron;
import com.neuronet.classic.api.INeuronsFactory;
import com.neuronet.util.FunctionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeuronsFactory implements INeuronsFactory {

    private static final Logger logger = LoggerFactory.getLogger(NeuronsFactory.class);

    @Override
    public INeuron newNeuron(float[] weights, float b, FunctionType functionType, float alfa) {
        return new Neuron(weights, b, functionType, alfa);
    }
}
