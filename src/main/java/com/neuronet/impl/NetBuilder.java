package com.neuronet.impl;

import com.neuronet.api.*;

import java.util.Deque;
import java.util.LinkedList;

public class NetBuilder implements INetBuilder {

    private final Deque<ILayerConfiguration> layers = new LinkedList<>();
    private INetParameters netParameters;
    private INetConfiguration netConfiguration;

    @Override
    public INetBuilder setNetParameters(final INetParameters netParameters) {
        this.netParameters = netParameters;
        return this;
    }

    @Override
    public INetBuilder setNetConfiguration(final INetConfiguration netConfiguration) {
        this.netConfiguration = netConfiguration;
        return this;
    }

    @Override
    public INetBuilder addLayer(final int neurons, final IFunction function) {
        if (function == null) {
            throw new NullPointerException("Function can't be null.");
        }
        layers.add(new ImmutableLayerConfiguration(neurons, function));
        return this;
    }

    @Override
    public INet build() {
        layers.addFirst(new ImmutableLayerConfiguration(this.netConfiguration.getInputsAmount(), null)); // Input layer.
        return new Net(netConfiguration, netParameters, layers);
    }

    private static class ImmutableLayerConfiguration implements ILayerConfiguration {
        private final int neurons;
        private final IFunction function;

        private ImmutableLayerConfiguration(final int neurons, final IFunction function) {
            this.neurons = neurons;
            this.function = function;
        }

        @Override
        public int getNeurons() {
            return this.neurons;
        }

        @Override
        public IFunction getFunction() {
            return this.function;
        }
    }
}