package com.neuronet.impl.functions;

import com.neuronet.api.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinearFunction implements IFunction {

    private static final Logger logger = LoggerFactory.getLogger(LinearFunction.class);

    private static final IFunction INSTANCE = new LinearFunction();

    private LinearFunction() {
    }

    public static IFunction getInstance() {
        return INSTANCE;
    }

    @Override
    public float executeFunction(float x) {
        return x;
    }

    @Override
    public float executeDerived(float x) {
        return x;
    }
}