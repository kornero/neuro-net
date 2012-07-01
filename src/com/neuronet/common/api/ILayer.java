package com.neuronet.common.api;

public interface ILayer {

    public float[] runLayer(final float[] inputData);

    public float[] educate(final float[] inputData, final float[] error);

    public float[] getLastResult();
}
