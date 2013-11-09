package com.neuronet.api.generator;

public class EducationSample {
    private final float[] inputsSample;
    private final float[] expectedOutputs;

    public EducationSample(final float inputSample, final float expectedOutput) {
        this(new float[]{inputSample}, new float[]{expectedOutput});
    }

    public EducationSample(final float inputSample, final float[] expectedOutputs) {
        this(new float[]{inputSample}, expectedOutputs);
    }

    public EducationSample(final float[] inputsSample, final float expectedOutput) {
        this(inputsSample, new float[]{expectedOutput});
    }

    public EducationSample(final float[] inputsSample, final float[] expectedOutputs) {
        this.inputsSample = inputsSample;
        this.expectedOutputs = expectedOutputs;
    }

    public float[] getInputsSample() {
        return inputsSample;
    }

    public float[] getExpectedOutputs() {
        return expectedOutputs;
    }
}