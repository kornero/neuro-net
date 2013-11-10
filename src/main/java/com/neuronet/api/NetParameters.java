package com.neuronet.api;

public class NetParameters implements INetParameters {

    public static final float DEFAULT_DX = 0.05f;
    public static final float DEFAULT_EDGE_WEIGHT = 0.15f;
    public static final float DEFAULT_EDUCATION_SPEED = 0.1f;

    private static final INetParameters INSTANCE = new NetParameters();

    private final float defaultDX;
    private final float defaultEdgeWeight;
    private final float educationSpeed;

    protected NetParameters() {
        this(DEFAULT_DX, DEFAULT_EDGE_WEIGHT, DEFAULT_EDUCATION_SPEED);
    }

    public NetParameters(float defaultDX, float defaultEdgeWeight, float educationSpeed) {
        this.defaultDX = defaultDX;
        this.defaultEdgeWeight = defaultEdgeWeight;
        this.educationSpeed = educationSpeed;
    }

    public static INetParameters getDefaultParameters() {
        return INSTANCE;
    }

    @Override
    public float generateDx() {
        return this.defaultDX;
    }

    @Override
    public float generateEdgeWeight() {
        return this.defaultEdgeWeight;
    }

    @Override
    public float getDefaultEducationSpeed() {
        return this.educationSpeed;
    }
}
