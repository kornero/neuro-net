package com.neuronet.impl;

import com.neuronet.api.IFunction;
import com.neuronet.api.ILayer;
import com.neuronet.api.INeuron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NeuronsFactory {

    private static final Logger logger = LoggerFactory.getLogger(NeuronsFactory.class);

    public static INeuron createNeuron(final IFunction function, final float defaultDx, final int position, final ILayer layer) {
        return new Neuron(function, defaultDx, position, layer);
    }
}