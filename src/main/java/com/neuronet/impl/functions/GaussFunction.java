package com.neuronet.impl.functions;

import com.neuronet.api.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GaussFunction implements IFunction {

    private static final Logger logger = LoggerFactory.getLogger(GaussFunction.class);

    private static final IFunction INSTANCE = new GaussFunction();

    private GaussFunction() {
    }

    public static IFunction getInstance() {
        return INSTANCE;
    }

    @Override
    public float executeFunction(float x) {
        return (float) Math.exp(-0.5 * x * x);
    }

    @Override
    public float executeDerived(float x) {
        return (float) (-1 * x * Math.exp(-0.5 * x * x));
    }
}