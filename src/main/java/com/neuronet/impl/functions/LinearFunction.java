package com.neuronet.impl.functions;

import com.neuronet.api.IFunction;
import org.apache.mahout.math.map.OpenFloatObjectHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinearFunction implements IFunction {

    private static final Logger logger = LoggerFactory.getLogger(LinearFunction.class);
    private static final OpenFloatObjectHashMap<IFunction> MAP = new OpenFloatObjectHashMap<>();
    private static final float MIN_OUTPUT = Float.MIN_VALUE;
    private static final float MAX_OUTPUT = Float.MAX_VALUE;
    private static final float DEFAULT_ALFA = 1.0f;
    private final float alfa;

    private LinearFunction(final float alfa) {
        this.alfa = alfa;
    }

    public static synchronized IFunction getInstance() {
        return getInstance(DEFAULT_ALFA);
    }

    public static synchronized IFunction getInstance(final float alfa) {
        IFunction function = MAP.get(alfa);
        if (function == null) {
            function = new LinearFunction(alfa);
            MAP.put(alfa, function);
        }
        return function;
    }

    @Override
    public float getMinOutput() {
        return MIN_OUTPUT;
    }

    @Override
    public float getMaxOutput() {
        return MAX_OUTPUT;
    }

    @Override
    public float executeFunction(final float x) {
        return alfa * x;
    }

    @Override
    public float executeDerived(final float x) {
        return alfa;
    }
}