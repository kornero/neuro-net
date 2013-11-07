package com.neuronet.impl.functions;

import com.neuronet.api.IFunction;
import org.apache.mahout.math.map.OpenFloatObjectHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.neuronet.util.Util.div;

public class BinarySigmaFunction implements IFunction {

    private static final Logger logger = LoggerFactory.getLogger(BinarySigmaFunction.class);
    private static final OpenFloatObjectHashMap<IFunction> MAP = new OpenFloatObjectHashMap<>();
    private static final float DEFAULT_ALFA = 1.0f;

    private final float alfa;

    private BinarySigmaFunction(float alfa) {
        this.alfa = alfa;
    }

    public static synchronized IFunction getInstance() {
        return getInstance(DEFAULT_ALFA);
    }

    public static synchronized IFunction getInstance(final float alfa) {
        IFunction function = MAP.get(alfa);
        if (function == null) {
            function = new BinarySigmaFunction(alfa);
            MAP.put(alfa, function);
        }
        return function;
    }

    /**
     * f(x) = 1 / (1 + e^(-ax))
     */
    @Override
    public float executeFunction(final float x) {
        return div(1, 1 + Math.exp(-1 * alfa * x));
    }

    /**
     * f(x) = 1 / (1 + e^(-ax))
     * f'(x) = a * f(x) * (1 + f(x))
     */
    @Override
    public float executeDerived(final float x) {
        final float fx = executeFunction(x);
        return alfa * fx * (1 - fx);
    }
}