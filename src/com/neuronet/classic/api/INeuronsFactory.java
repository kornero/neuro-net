package com.neuronet.classic.api;

import com.neuronet.util.FunctionType;

public interface INeuronsFactory {

    public INeuron newNeuron(final float[] weights, final float b, final FunctionType functionType, final float alfa);
}
