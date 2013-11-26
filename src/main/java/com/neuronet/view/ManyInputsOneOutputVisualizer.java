package com.neuronet.view;

import com.neuronet.api.INet;
import com.neuronet.api.generator.EducationSample;

import java.util.List;

public class ManyInputsOneOutputVisualizer extends SimpleMathFunctionVisualizer {

    @Override
    public double[] getX(final List<EducationSample> samples) {
        final double[] xData = new double[samples.size()];
        for (int i = 0; i < xData.length; i++) {
            xData[i] = i;
        }
        return xData;
    }

    @Override
    public double[] getY(final List<EducationSample> samples) {
        final double[] expData = new double[samples.size()];
        int i = 0;
        for (final EducationSample sample : samples) {
            expData[i] = sample.getExpectedOutputs()[0];
            i++;
        }
        return expData;
    }

    @Override
    public double[] getY(final List<EducationSample> samples, final INet net) {
        final double[] actData = new double[samples.size()];
        int i = 0;
        for (final EducationSample sample : samples) {
            actData[i] = net.run(sample.getInputsSample())[0];
            i++;
        }
        return actData;
    }
}