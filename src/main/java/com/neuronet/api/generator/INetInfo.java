package com.neuronet.api.generator;

import com.neuronet.api.IEducationDataSource;
import com.neuronet.api.INetConfiguration;
import com.neuronet.api.INetParameters;

public interface INetInfo {

    public int getMinLayers();

    public int getMaxLayers();

    public int getMinNeurons();

    public int getMaxNeurons();

    public IEducationDataSource getEducationDataSource();

    public INetConfiguration getNetConfiguration();

    public INetParameters getParameters();

    // TODO: public IVisualizer getVisualizer();
}
