package com.neuronet.edged.api;

import java.io.Serializable;

public interface IConfiguration extends Serializable {
    public float getDefaultAlfa();

    public float getDefaultDX();

    public float getDefaultEdgeWeight();

    public float getEducationSpeed();
}
