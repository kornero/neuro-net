package com.neuronet.edged.api;

import com.neuronet.util.FunctionType;

import java.io.Serializable;

public interface IConfiguration extends Serializable {
    public float getDefaultAlfa(final FunctionType type);

    public float getDefaultDX();

    public float getDefaultEdgeWeight();

    public float getEducationSpeed();
}
