package com.neuronet.impl.example;

import com.neuronet.api.IConfiguration;
import com.neuronet.api.IFunction;
import com.neuronet.api.RandomConfiguration;
import com.neuronet.api.generator.EductionSample;
import com.neuronet.api.generator.SimpleNetInfo;
import com.neuronet.impl.functions.BinaryFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SimpleImageNetInfo extends SimpleNetInfo {

    private static final Logger logger = LoggerFactory.getLogger(SinNetInfo.class);

    private static final int MIN_NEURONS = 10;
    private static final int MAX_NEURONS = 500;
    private static final int MIN_LAYERS = 2;
    private static final int MAX_LAYERS = 5;
    private static final int INPUTS = 15;
    private static final int OUTPUTS = 1;

    public SimpleImageNetInfo() {
        super(MIN_NEURONS, MAX_NEURONS, MIN_LAYERS, MAX_LAYERS, INPUTS, OUTPUTS);
    }

    @Override
    protected List<EductionSample> loadEducationData() {
        final List<EductionSample> list = new ArrayList<>();

        // H
        list.add(new EductionSample(new float[]
                {
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                }, new float[]{1f}));
        list.add(new EductionSample(new float[]
                {
                        0f, 0f, 0f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                }, new float[]{1f}));
        list.add(new EductionSample(new float[]
                {
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        1f, 0f, 1f,
                        0f, 0f, 0f,
                }, new float[]{1f}));

        // 4
        list.add(new EductionSample(new float[]
                {
                        0f, 0f, 0f,
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        1f, 0f, 1f,
                }, new float[]{0f}));
        list.add(new EductionSample(new float[]
                {
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        0f, 0f, 1f,
                        0f, 0f, 1f,
                }, new float[]{0f}));
        list.add(new EductionSample(new float[]
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
    protected List<EductionSample> loadTestData() {
        final List<EductionSample> list = new ArrayList<>();

        // H
        list.add(new EductionSample(new float[]
                {
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                        0f, 0f, 0f,
                }, new float[]{1f}));

        // 4
        list.add(new EductionSample(new float[]
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
    public IFunction getOutputFunction() {
        return BinaryFunction.getInstance();
    }

    @Override
    public float getMaxInputValue() {
        return 1.0f;
    }

    @Override
    public IConfiguration getConfiguration() {
        return RandomConfiguration.getDefaultConfiguration();
    }
}