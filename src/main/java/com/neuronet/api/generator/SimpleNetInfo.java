package com.neuronet.api.generator;

import com.neuronet.api.IEducationDataSource;
import com.neuronet.api.INetConfiguration;
import com.neuronet.impl.ImmutableEducationDataSource;
import com.neuronet.impl.ImmutableNetConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class SimpleNetInfo implements INetInfo {

    private static final Logger logger = LoggerFactory.getLogger(SimpleNetInfo.class);
    private final int minNeurons, maxNeurons;
    private final int minLayers, maxLayers;
    private final INetConfiguration netConfiguration;
    private final IEducationDataSource educationDataSource;

    protected SimpleNetInfo(final int minNeurons, final int maxNeurons,
                            final int minLayers, final int maxLayers,
                            final int inputs, final int outputs,
                            final int minInput, final int maxInput,
                            final int minOutput, final int maxOutput) {
        this.minNeurons = minNeurons;
        this.maxNeurons = maxNeurons;
        this.minLayers = minLayers;
        this.maxLayers = maxLayers;
        this.netConfiguration = new ImmutableNetConfiguration(inputs, outputs, minInput, maxInput, minOutput, maxOutput);
        this.educationDataSource = new ImmutableEducationDataSource(loadEducationData(), loadTestData());
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
    public INetConfiguration getNetConfiguration() {
        return this.netConfiguration;
    }

    @Override
    public IEducationDataSource getEducationDataSource() {
        return this.educationDataSource;
    }

    protected abstract List<EducationSample> loadEducationData();

    protected abstract List<EducationSample> loadTestData();
}