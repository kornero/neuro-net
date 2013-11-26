package com.neuronet.api.generator;

import com.neuronet.api.IEducationDataSource;
import com.neuronet.api.INetConfiguration;
import com.neuronet.api.INetParameters;
import com.neuronet.view.IVisualizer;

public interface INetInfo {

    public int getMinLayers();

    public int getMaxLayers();

    public int getMinNeurons();

    public int getMaxNeurons();

    public IEducationDataSource getEducationDataSource();

    public INetConfiguration getNetConfiguration();

    public INetParameters getParameters();

    public IVisualizer getVisualizer();
}
