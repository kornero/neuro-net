package com.neuronet.edged.api;

import java.util.Collection;

public interface ILayer {

    public float[] runLayer();

    public float[] educate(final float[] inputData, final float[] error);

    public float[] getLastResult();

    public Collection<INeuron> getNeurons();

    public INet getNet();
}
