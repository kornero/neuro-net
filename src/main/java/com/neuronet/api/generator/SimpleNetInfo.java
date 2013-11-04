package com.neuronet.api.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public abstract class SimpleNetInfo implements NetInfo {

    private static final Logger logger = LoggerFactory.getLogger(SimpleNetInfo.class);

    private final int minNeurons, maxNeurons;
    private final int minLayers, maxLayers;
    private final int inputs, outputs;

    private final List<EductionSample> educationData;
    private final List<EductionSample> testData;

    protected SimpleNetInfo(final int minNeurons, final int maxNeurons,
                            final int minLayers, final int maxLayers,
                            final int inputs, final int outputs) {
        this.minNeurons = minNeurons;
        this.maxNeurons = maxNeurons;
        this.minLayers = minLayers;
        this.maxLayers = maxLayers;
        this.inputs = inputs;
        this.outputs = outputs;

        this.educationData = Collections.unmodifiableList(loadEducationData());
        this.testData = Collections.unmodifiableList(loadTestData());
    }

    @Override
    public int getMinNeurons() {
        return this.minNeurons;
    }

    @Override
    public int getMaxNeurons() {
        return this.maxNeurons;
    }

    @Override
    public int getMinLayers() {
        return this.minLayers;
    }

    @Override
    public int getMaxLayers() {
        return this.maxLayers;
    }

    @Override
    public int getInputs() {
        return this.inputs;
    }

    @Override
    public int getOutputs() {
        return this.outputs;
    }

    @Override
    public List<EductionSample> getEducationData() {
        return this.educationData;
    }

    @Override
    public List<EductionSample> getTestData() {
        return this.testData;
    }

    protected abstract List<EductionSample> loadEducationData();

    protected abstract List<EductionSample> loadTestData();
}