package com.neuronet.impl.functions;

import com.neuronet.api.IFunction;
import org.apache.mahout.math.map.OpenFloatObjectHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.neuronet.util.Util.div;

public class BipolarSigmaFunction implements IFunction {

    private static final Logger logger = LoggerFactory.getLogger(BipolarSigmaFunction.class);
    private static final OpenFloatObjectHashMap<IFunction> MAP = new OpenFloatObjectHashMap<>();
    private static final float DEFAULT_ALFA = 1.0f;

    private final float alfa;

    private BipolarSigmaFunction(float alfa) {
        this.alfa = alfa;
    }

    public static synchronized IFunction getInstance() {
        return getInstance(DEFAULT_ALFA);
    }

    public static synchronized IFunction getInstance(final float alfa) {
        IFunction function = MAP.get(alfa);
        if (function == null) {
            function = new BipolarSigmaFunction(alfa);
            MAP.put(alfa, function);
        }
        return function;
    }

    @Override
    public float executeFunction(float x) {
//        return x / (alfa + Math.abs(x));
        return div(x, alfa + Math.abs(x));
    }

    @Override
    public float executeDerived(float x) {
//        return (alfa - x * Math.abs(x) + Math.abs(x)) / ((alfa + Math.abs(x)) * (alfa + Math.abs(x)));
        return div(alfa - x * Math.abs(x) + Math.abs(x), (alfa + Math.abs(x)) * (alfa + Math.abs(x)));
    }
}