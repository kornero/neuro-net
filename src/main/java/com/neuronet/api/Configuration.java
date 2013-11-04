package com.neuronet.api;

public class Configuration implements IConfiguration {

    public static final float DEFAULT_DX = 0.25f;
    public static final float DEFAULT_EDGE_WEIGHT = 0.25f;
    public static final float DEFAULT_EDUCATION_SPEED = 0.001f;

    private static final IConfiguration INSTANCE = new Configuration();

    private final float defaultDX;
    private final float defaultEdgeWeight;
    private final float educationSpeed;

    protected Configuration() {
        this(DEFAULT_DX, DEFAULT_EDGE_WEIGHT, DEFAULT_EDUCATION_SPEED);
    }

    public Configuration(float defaultDX, float defaultEdgeWeight, float educationSpeed) {
        this.defaultDX = defaultDX;
        this.defaultEdgeWeight = defaultEdgeWeight;
        this.educationSpeed = educationSpeed;
    }

    public static IConfiguration getDefaultConfiguration() {
        return INSTANCE;
    }

    @Override
    public float getDefaultDX() {
        return this.defaultDX;
    }

    @Override
    public float getDefaultEdgeWeight() {
        return this.defaultEdgeWeight;
    }

    @Override
    public float getDefaultEducationSpeed() {
        return this.educationSpeed;
    }
}
