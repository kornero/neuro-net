package com.neuronet.edged.api;

import com.neuronet.util.FunctionType;

import java.io.Serializable;
import java.util.List;

public interface ILayer extends Serializable {

    public float[] runLayer();

    public float[] educate(final float[] inputData, final float[] error);

    public float[] getLastResult();

    public List<INeuron> getNeurons();

    public INet getNet();

    public FunctionType getFunctionType();

    public void setEducationSpeed(final float educationSpeed);
}
