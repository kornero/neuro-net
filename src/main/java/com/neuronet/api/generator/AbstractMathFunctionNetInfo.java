package com.neuronet.api.generator;

import com.neuronet.view.IVisualizer;
import com.neuronet.view.VisualizerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractMathFunctionNetInfo extends AbstractNetInfo {

    private static final int INPUTS = 1;
    private static final int OUTPUTS = 1;
    private static final float MAX_STEP_SIZE = 0.1f;
    private static final float TEST_PART = 3f;

    protected AbstractMathFunctionNetInfo(final int minNeurons, final int maxNeurons,
                                          final int minLayers, final int maxLayers,
                                          final int minInput, final int maxInput,
                                          final int minOutput, final int maxOutput) {
        super(minNeurons, maxNeurons, minLayers, maxLayers, INPUTS, OUTPUTS, minInput, maxInput, minOutput, maxOutput);
    }

    @Override
    public IVisualizer getVisualizer() {
        return VisualizerFactory.getSimpleMathFunctionVisualizer();
    }

    @Override
    protected final List<EducationSample> loadEducationData() {
        final float minInput = this.getNetConfiguration().getMinInput();
        final float maxInput = this.getNetConfiguration().getMaxInput();

        final Random random = new Random();
        final List<EducationSample> list = new ArrayList<>();
        for (float x = minInput; x < maxInput; x += random.nextFloat() * MAX_STEP_SIZE) {
            list.add(new EducationSample(x, f(x)));
        }
        return list;
    }

    @Override
    protected final List<EducationSample> loadTestData() {
        final float minInput = this.getNetConfiguration().getMinInput();
        final float maxInput = this.getNetConfiguration().getMaxInput();

        final Random random = new Random();
        final List<EducationSample> list = new ArrayList<>();
        for (float x = minInput; x < maxInput; x += random.nextFloat() * MAX_STEP_SIZE * TEST_PART) {
            list.add(new EducationSample(x, f(x)));
        }

        return list;
    }

    protected abstract float f(final float x);
}