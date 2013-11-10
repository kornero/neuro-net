package com.neuronet.impl.example;

import com.neuronet.api.INetParameters;
import com.neuronet.api.RandomNetParameters;
import com.neuronet.api.generator.EducationSample;
import com.neuronet.api.generator.SimpleNetInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * net = sin(x), where x ~ [ 0; 3.1415].
 */
public class SinNetInfo extends SimpleNetInfo {
    private static final int MIN_NEURONS = 5;
    private static final int MAX_NEURONS = 50;
    private static final int MIN_LAYERS = 1;
    private static final int MAX_LAYERS = 3;
    private static final int INPUTS = 1;
    private static final int OUTPUTS = 1;
    private static final int MIN_INPUT = (int) (Math.PI * (-2));
    private static final int MAX_INPUT = (int) (Math.PI * 2); // (float) (Math.PI * 2)
    private static final int MIN_OUTPUT = -1;
    private static final int MAX_OUTPUT = 1;
    private static final float MAX_STEP_SIZE = 0.1f;

    public SinNetInfo() {
        super(MIN_NEURONS, MAX_NEURONS, MIN_LAYERS, MAX_LAYERS, INPUTS, OUTPUTS,
                MIN_INPUT, MAX_INPUT, MIN_OUTPUT, MAX_OUTPUT);
    }

    @Override
    protected List<EducationSample> loadEducationData() {
        final Random random = new Random();
        final List<EducationSample> list = new ArrayList<>();
        for (float j = MIN_INPUT; j < MAX_INPUT; j += random.nextFloat() * MAX_STEP_SIZE) {
            list.add(new EducationSample(j, (float) Math.sin(j)));
        }

        return list;
    }

    @Override
    protected List<EducationSample> loadTestData() {
        final Random random = new Random();
        final List<EducationSample> list = new ArrayList<>();
        for (float j = MIN_INPUT; j < MAX_INPUT; j += random.nextFloat() * MAX_STEP_SIZE * 3) {
            list.add(new EducationSample(j, (float) Math.sin(j)));
        }

        return list;
    }

    @Override
    public INetParameters getParameters() {
        return RandomNetParameters.getDefaultConfiguration();
    }
}