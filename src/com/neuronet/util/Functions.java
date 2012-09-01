package com.neuronet.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Functions {

    private static final Logger logger = LoggerFactory.getLogger(Functions.class);

    public static float getFunction(final float x, final FunctionType functionType, final float alfa) {
        final float result;
        switch (functionType) {
            case LINEAR: {
                result = x;
                break;
            }
            case BIPOLAR: {
                if (x > 0) {
                    result = 1;
                } else {
                    result = -1;
                }
                break;
            }
            case BINARY: {
                if (x > 0) {
                    result = 1;
                } else {
                    result = 0;
                }
                break;
            }
            case BIPOLAR_SIGMA: {
                result = x / (alfa + Math.abs(x));
                break;
            }
            case BINARY_SIGMA: {
                result = (float) (x / (1 + Math.exp(-1 * alfa * x)));
                break;
            }
            case GAUSS: {
                result = (float) (Math.exp(-0.5 * x * x));
                break;
            }
            default: {
                result = 0;
                break;
            }
        }
        return result;
    }

    public static float getDerived(final float x, final FunctionType functionType, final float alfa) {
        final float derived;
        switch (functionType) {
            case LINEAR: {
                derived = x;
                break;
            }
            case BIPOLAR: {
                if (x > 0) {
                    derived = 1;
                } else {
                    derived = -1;
                }
                break;
            }
            case BINARY: {
                if (x > 0) {
                    derived = 1;
                } else {
                    derived = 0;
                }
                break;
            }
            case BIPOLAR_SIGMA: {
                derived = (alfa - x * Math.abs(x) + Math.abs(x)) / ((alfa + Math.abs(x)) * (alfa + Math.abs(x)));
                break;
            }
            case BINARY_SIGMA: {
                derived = (float) ((1 + Math.exp(-1 * alfa * x) + alfa * x * Math.exp(-1 * alfa * x)) /
                        ((1 + Math.exp(-1 * alfa * x)) * (1 + Math.exp(-1 * alfa * x))));
                break;
            }
            case GAUSS: {
                derived = (float) (-1 * x * Math.exp(-0.5 * x * x));
                break;
            }
            default: {
                derived = 0;
                break;
            }
        }
        return derived;
    }
}
