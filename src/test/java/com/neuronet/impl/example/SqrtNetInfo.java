package com.neuronet.impl.example;

import com.neuronet.api.INetParameters;
import com.neuronet.api.RandomNetParameters;
import com.neuronet.api.generator.EducationSample;
import com.neuronet.api.generator.SimpleNetInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SqrtNetInfo extends SimpleNetInfo {
    private static final int MIN_NEURONS = 5;
    private static final int MAX_NEURONS = 50;
    private static final int MIN_LAYERS = 1;
    private static final int MAX_LAYERS = 3;
    private static final int INPUTS = 1;
    private static final int OUTPUTS = 1;
    private static final int MIN_INPUT = 4;
    private static final int MAX_INPUT = 25;
    private static final int MIN_OUTPUT = (int) Math.sqrt(MIN_INPUT) - 1;
    private static final int MAX_OUTPUT = (int) Math.sqrt(MAX_INPUT) + 1;
    private static final float MAX_STEP_SIZE = 0.1f;

    public SqrtNetInfo() {
        super(MIN_NEURONS, MAX_NEURONS, MIN_LAYERS, MAX_LAYERS, INPUTS, OUTPUTS,
                MIN_INPUT, MAX_INPUT, MIN_OUTPUT, MAX_OUTPUT);
    }

    @Override
    protected List<EducationSample> loadEducationData() {
        final Random random = new Random();
        final List<EducationSample> list = new ArrayList<>();
        for (float j = MIN_INPUT; j < MAX_INPUT; j += random.nextFloat() * MAX_STEP_SIZE) {
            list.add(new EducationSample(j, (float) Math.sqrt(j)));
        }

        return list;
    }

    @Override
    protected List<EducationSample> loadTestData() {
        final Random random = new Random();
        final List<EducationSample> list = new ArrayList<>();
        for (float j = MIN_INPUT; j < MAX_INPUT; j += random.nextFloat() * MAX_STEP_SIZE * 3) {
            list.add(new EducationSample(j, (float) Math.sqrt(j)));
        }

        return list;
    }

    @Override
    public INetParameters getParameters() {
        return RandomNetParameters.getDefaultConfiguration();
    }
}