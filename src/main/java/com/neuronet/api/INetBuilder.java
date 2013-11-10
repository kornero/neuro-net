package com.neuronet.api;

public interface INetBuilder {

    public INetBuilder setNetParameters(final INetParameters netParameters);

    public INetBuilder setNetConfiguration(final INetConfiguration netConfiguration);

    public INetBuilder addLayer(final int neurons, final IFunction function);

    public INet build();
}