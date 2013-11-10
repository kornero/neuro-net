package com.neuronet.api;

import java.io.Serializable;

public interface INetParameters extends Serializable {
    public float generateDx();

    public float generateEdgeWeight();

    public float getDefaultEducationSpeed();
}
