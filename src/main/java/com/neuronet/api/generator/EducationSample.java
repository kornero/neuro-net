package com.neuronet.api.generator;

import java.util.Arrays;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final EducationSample sample = (EducationSample) o;

        if (!Arrays.equals(expectedOutputs, sample.expectedOutputs)) return false;
        if (!Arrays.equals(inputsSample, sample.inputsSample)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(inputsSample);
        result = 31 * result + Arrays.hashCode(expectedOutputs);
        return result;
    }

    @Override
    public String toString() {
        return "EducationSample{" +
                "inputsSample=" + Arrays.toString(inputsSample) +
                ", expectedOutputs=" + Arrays.toString(expectedOutputs) +
                '}';
    }
}