package com.neuronet.impl.functions;

import com.neuronet.api.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public abstract class FunctionsFactory {

    private static final Logger logger = LoggerFactory.getLogger(FunctionsFactory.class);
    private static final Random RANDOM = new Random();

    private FunctionsFactory() {
    }

    public static IFunction getRandomFunction() {
        switch (RANDOM.nextInt(6)) {
            case 0:
                return BinaryFunction.getInstance();
            case 1:
                return BinarySigmaFunction.getInstance();
            case 2:
                return BipolarFunction.getInstance();
            case 3:
                return BipolarSigmaFunction.getInstance();
            case 4:
                return GaussFunction.getInstance();
            case 5:
                return LinearFunction.getInstance();
        }
        throw new IllegalStateException();
    }
}