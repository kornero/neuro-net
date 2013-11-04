package com.neuronet.api;

import java.io.Serializable;

public interface IConfiguration extends Serializable {
    public float getDefaultDX();

    public float getDefaultEdgeWeight();

    public float getDefaultEducationSpeed();
}
