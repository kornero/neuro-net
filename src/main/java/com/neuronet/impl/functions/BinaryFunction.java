package com.neuronet.impl.functions;

import com.neuronet.api.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinaryFunction extends AbstractBinaryFunction {

    private static final Logger logger = LoggerFactory.getLogger(BinaryFunction.class);
    private static final IFunction INSTANCE = new BinaryFunction();

    private BinaryFunction() {
    }

    public static IFunction getInstance() {
        return INSTANCE;
    }

    @Override
    public float executeFunction(final float x) {
        if (x > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public float executeDerived(final float x) {
        if (x > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}