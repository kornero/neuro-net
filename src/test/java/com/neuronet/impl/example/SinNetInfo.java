package com.neuronet.impl.example;

import com.neuronet.api.IConfiguration;
import com.neuronet.api.IFunction;
import com.neuronet.api.RandomConfiguration;
import com.neuronet.api.generator.EducationSample;
import com.neuronet.api.generator.SimpleNetInfo;
import com.neuronet.impl.functions.BipolarSigmaFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * net = sin(x), where x ~ [ 0; 3.1415].
 */
public class SinNetInfo extends SimpleNetInfo {

    private static final Logger logger = LoggerFactory.getLogger(SinNetInfo.class);

    private static final int MIN_NEURONS = 5;
    private static final int MAX_NEURONS = 50;
    private static final int MIN_LAYERS = 1;
    private static final int MAX_LAYERS = 3;
    private static final int INPUTS = 1;
    private static final int OUTPUTS = 1;

    private static final float MIN_X = (float) (Math.PI * (-6));
    private static final float MAX_X = (float) (Math.PI * 6); // (float) (Math.PI * 2)
    private static final float MAX_STEP_SIZE = 0.1f;

    public SinNetInfo() {
        super(MIN_NEURONS, MAX_NEURONS, MIN_LAYERS, MAX_LAYERS, INPUTS, OUTPUTS);
    }

    @Override
    protected List<EducationSample> loadEducationData() {
        final Random random = new Random();
        final List<EducationSample> list = new ArrayList<>();
        for (float j = MIN_X; j < MAX_X; j += random.nextFloat() * MAX_STEP_SIZE) {
            list.add(new EducationSample(j, (float) Math.sin(j)));
        }

        return list;
    }

    @Override
    protected List<EducationSample> loadTestData() {
        final Random random = new Random();
        final List<EducationSample> list = new ArrayList<>();
        for (float j = MIN_X; j < MAX_X; j += random.nextFloat() * MAX_STEP_SIZE * 10) {
            list.add(new EducationSample(j, (float) Math.sin(j)));
        }

        return list;
    }

    @Override
    public float getMaxInputValue() {
        return MAX_X;
    }

    @Override
    public IConfiguration getConfiguration() {
        return RandomConfiguration.getDefaultConfiguration();
    }

    @Override
    public IFunction getOutputFunction() {
        return BipolarSigmaFunction.getInstance();
    }
}