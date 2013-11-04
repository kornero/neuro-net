package com.neuronet.api;

import com.neuronet.util.FunctionType;

public class Configuration implements IConfiguration {

    public static final float DEFAULT_ALFA = 0.00001f;
    public static final float DEFAULT_DX = 0.25f;
    public static final float DEFAULT_EDGE_WEIGHT = 0.25f;

    public static final float EDUCATION_SPEED = 0.0001f;

    private static final IConfiguration INSTANCE = new Configuration();

    private final float defaultAlfa;
    private final float defaultDX;
    private final float defaultEdgeWeight;
    private final float educationSpeed;

    private Configuration() {
        this(DEFAULT_ALFA, DEFAULT_DX, DEFAULT_EDGE_WEIGHT, EDUCATION_SPEED);
    }

    public Configuration(float defaultAlfa, float defaultDX, float defaultEdgeWeight, float educationSpeed) {
        this.defaultAlfa = defaultAlfa;
        this.defaultDX = defaultDX;
        this.defaultEdgeWeight = defaultEdgeWeight;
        this.educationSpeed = educationSpeed;
    }

    public static IConfiguration getDefaultConfiguration() {
        return INSTANCE;
    }

    @Override
    public float getDefaultAlfa(final FunctionType type) {
        return defaultAlfa;
    }

    @Override
    public float getDefaultDX() {
        return defaultDX;
    }

    @Override
    public float getDefaultEdgeWeight() {
        return defaultEdgeWeight;
    }

    @Override
    public float getEducationSpeed() {
        return educationSpeed;
    }
}
