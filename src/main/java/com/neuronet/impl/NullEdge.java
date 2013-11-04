package com.neuronet.impl;

import com.neuronet.api.IEdge;
import com.neuronet.api.INeuron;
import com.neuronet.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NullEdge implements IEdge {

    private static final Logger logger = LoggerFactory.getLogger(NullEdge.class);
    private static final IEdge INSTANCE = new NullEdge();

    private NullEdge() {
    }

    public static IEdge getInstance() {
        return INSTANCE;
    }

    @Override
    public float run() {
        return 0;
    }

    public float getWeight() {
        return 0;
    }

    public void incrementWeight(float incWeight) {
    }

    @Override
    public INeuron getInput() {
        throw new UnsupportedOperationException();
    }

    @Override
    public INeuron getOutput() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWeight(float weight) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}