package com.neuronet.impl.example;

import com.neuronet.api.INetParameters;
import com.neuronet.api.RandomNetParameters;
import com.neuronet.api.generator.AbstractNetInfo;
import com.neuronet.api.generator.EducationSample;
import com.neuronet.view.IVisualizer;
import com.neuronet.view.VisualizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CosSinNetInfo extends AbstractNetInfo {

    private static final Logger logger = LoggerFactory.getLogger(CosSinNetInfo.class);
    private static final int INPUTS = 5;
    private static final int OUTPUTS = 1;
    private static final int MIN_INPUT = (int) (Math.PI * (-4)) + 1;
    private static final int MAX_INPUT = (int) (Math.PI * 4) + 1; // (float) (Math.PI * 2)
    private static final int MIN_OUTPUT = -1;
    private static final int MAX_OUTPUT = 1;
    private static final int MIN_NEURONS = 5;
    private static final int MAX_NEURONS = 50;
    private static final int MIN_LAYERS = 1;
    private static final int MAX_LAYERS = 3;
    private static final float STEP_SIZE = 0.1f;
    private static final float TEST_PART = 3f;

    public CosSinNetInfo() {
        super(MIN_NEURONS, MAX_NEURONS, MIN_LAYERS, MAX_LAYERS, INPUTS, OUTPUTS,
                MIN_INPUT, MAX_INPUT, MIN_OUTPUT, MAX_OUTPUT);
    }

    private static float f(final float x) {
        return (float) (Math.sin(3 * x) + Math.cos(x));
    }

    @Override
    public IVisualizer getVisualizer() {
        return VisualizerFactory.getVisualizer();
    }

    @Override
    protected final List<EducationSample> loadEducationData() {
        return generateTestData(STEP_SIZE);
    }

    @Override
    protected final List<EducationSample> loadTestData() {
        return generateTestData(STEP_SIZE);
    }

    @Override
    public INetParameters getParameters() {
        return RandomNetParameters.getDefaultConfiguration();
    }

    private List<EducationSample> generateTestData(final float step) {
        final float minInput = this.getNetConfiguration().getMinInput();
        final float maxInput = this.getNetConfiguration().getMaxInput();

        final List<EducationSample> list = new ArrayList<>();
        for (float x = minInput + step * INPUTS; x < maxInput; x += step) {
            final float[] input = new float[INPUTS];
            for (int i = 0; i < INPUTS; i++) {
                input[i] = f(x - step * (INPUTS - i));
            }

            list.add(new EducationSample(input, f(x)));
        }

        if (logger.isTraceEnabled()) {
            for (final EducationSample sample : list) {
                logger.trace("f(x):{}-->{}", Arrays.toString(sample.getInputsSample()), sample.getExpectedOutputs()[0]);
            }
        }

        return list;
    }
}