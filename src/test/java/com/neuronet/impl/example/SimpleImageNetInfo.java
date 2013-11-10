package com.neuronet.impl.example;

import com.neuronet.api.INetParameters;
import com.neuronet.api.RandomNetParameters;
import com.neuronet.api.generator.EducationSample;
import com.neuronet.api.generator.SimpleNetInfo;

import java.util.ArrayList;
import java.util.List;

public class SimpleImageNetInfo extends SimpleNetInfo {
    private static final int MIN_NEURONS = 10;
    private static final int MAX_NEURONS = 500;
    private static final int MIN_LAYERS = 2;
    private static final int MAX_LAYERS = 5;
    private static final int INPUTS = 15;
    private static final int OUTPUTS = 1;
    private static final int MIN_INPUT = 0;
    private static final int MAX_INPUT = 1;
    private static final int MIN_OUTPUT = 0;
    private static final int MAX_OUTPUT = 1;

    public SimpleImageNetInfo() {
        super(MIN_NEURONS, MAX_NEURONS, MIN_LAYERS, MAX_LAYERS, INPUTS, OUTPUTS,
                MIN_INPUT, MAX_INPUT, MIN_OUTPUT, MAX_OUTPUT);
    }

    @Override
    protected List<EducationSample> loadEducationData() {
        final List<EducationSample> list = new ArrayList<>();

        // H
        list.add(new EducationSample(new float[]
                {
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                }, new float[]{1f}));
        list.add(new EducationSample(new float[]
                {
                        0f, 0f, 0f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                }, new float[]{1f}));
        list.add(new EducationSample(new float[]
                {
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        1f, 0f, 1f,
                        0f, 0f, 0f,
                }, new float[]{1f}));

        // 4
        list.add(new EducationSample(new float[]
                {
                        0f, 0f, 0f,
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        1f, 0f, 1f,
                }, new float[]{0f}));
        list.add(new EducationSample(new float[]
                {
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        0f, 0f, 1f,
                        0f, 0f, 1f,
                }, new float[]{0f}));
        list.add(new EducationSample(new float[]
                {
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        0f, 0f, 1f,
                        0f, 0f, 1f,
                        0f, 0f, 0f,
                }, new float[]{0f}));

        return list;
    }

    @Override
    protected List<EducationSample> loadTestData() {
        final List<EducationSample> list = new ArrayList<>();

        // H
        list.add(new EducationSample(new float[]
                {
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                        0f, 0f, 0f,
                }, new float[]{1f}));

        // 4
        list.add(new EducationSample(new float[]
                {
                        0f, 0f, 0f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        0f, 0f, 1f,
                        0f, 0f, 0f,
                }, new float[]{0f}));

        return list;
    }

    @Override
    public INetParameters getParameters() {
        return RandomNetParameters.getDefaultConfiguration();
    }
}