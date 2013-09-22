package com.neuronet.util;

import java.util.Random;

public enum FunctionType {
    LINEAR,
    BIPOLAR,
    BINARY,
    BIPOLAR_SIGMA,
    BINARY_SIGMA,
    GAUSS;

    private static final Random RANDOM = new Random();

    public static FunctionType getRandomFunctionType() {
        return values()[RANDOM.nextInt(values().length)];
    }
}
