package com.neuronet.api;

import java.io.Serializable;

public interface IFunction extends Serializable {

    public float getMinOutput();

    public float getMaxOutput();

    public float executeFunction(final float x);

    public float executeDerived(final float x);
}