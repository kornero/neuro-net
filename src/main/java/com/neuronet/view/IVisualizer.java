package com.neuronet.view;

import com.neuronet.api.INet;
import com.neuronet.api.generator.EducationSample;
import com.neuronet.api.generator.INetInfo;
import com.xeiam.xchart.Chart;

import java.util.List;

public interface IVisualizer {

    public static final String EXPECTED = "exp(x)";
    public static final String ACTUAL = "act(x)";

    public Chart createChart(final INet net, final INetInfo netInfo);

    public double[] getX(final List<EducationSample> samples);

    public double[] getY(final List<EducationSample> samples);

    public double[] getY(final List<EducationSample> samples, final INet net);
}