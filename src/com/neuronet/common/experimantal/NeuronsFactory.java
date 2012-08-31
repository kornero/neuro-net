package com.neuronet.common.experimantal;

import com.neuronet.common.api.INeuron;
import com.neuronet.common.api.INeuronsFactory;
import com.neuronet.util.FunctionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeuronsFactory implements INeuronsFactory {

    private static final Logger logger = LoggerFactory.getLogger(NeuronsFactory.class);

    @Override
    public INeuron newNeuron(float[] weights, float b, FunctionType functionType, float alfa) {
        final Neuron neuron = new Neuron(b, functionType, alfa);
        for (float f : weights) {
            neuron.createInputEdge(null, f);
        }
        return neuron;
    }
}
