package com.neuronet.api;

import java.io.Serializable;

public interface INetConfiguration extends Serializable {

    public int getInputsAmount();

    public int getOutputsAmount();

    public int getMinInput();

    public int getMaxInput();

    public int getMinOutput();

    public int getMaxOutput();
}