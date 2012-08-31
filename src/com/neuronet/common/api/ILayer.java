package com.neuronet.common.api;

import java.util.Collection;

public interface ILayer {

    public float[] runLayer(final float[] inputData);

    public float[] educate(final float[] inputData, final float[] error);

    public float[] getLastResult();

    public Collection<INeuron> getNeurons();
}
