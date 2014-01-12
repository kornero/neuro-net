package com.neuronet.api.generator;

import com.neuronet.api.INet;
import com.neuronet.api.INetParameters;
import com.neuronet.api.RandomWeightNetParameters;
import com.neuronet.view.IVisualizer;
import com.neuronet.view.ManyInputsOneOutputVisualizer;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractFunctionPredictionNetInfo extends AbstractNetInfo {

    private static final Logger logger = LoggerFactory.getLogger(AbstractFunctionPredictionNetInfo.class);
    private static final int OUTPUTS = 1;
    private static final float STEP_SIZE = 0.1f;
    private static final float PREDICTION_TEST_SIZE = 0.3f; // 30%.
    private final List<EducationSample> fullData;

    public AbstractFunctionPredictionNetInfo(final int minNeurons, final int maxNeurons,
                                             final int minLayers, final int maxLayers,
                                             final int predictionWindowSize,
                                             final int minInput, final int maxInput,
                                             final int minOutput, final int maxOutput) {
        super(minNeurons, maxNeurons, minLayers, maxLayers, predictionWindowSize, OUTPUTS,
                minInput, maxInput, minOutput, maxOutput);
        this.fullData = generateData(STEP_SIZE, minInput, maxInput);
    }

    protected abstract float f(final float x);

    @Override
    public IVisualizer getVisualizer() {

        // TODO:!!!!!!!!!!!
        return new ManyInputsOneOutputVisualizer() {

            @Override
            public Chart createChart(final INet net, final INetInfo netInfo) {
                final double[] xData = getX(fullData);
                final double[] educationData = getEducationY();
                final double[] testData = getTestY();

                // Create Chart
                return QuickChart.getChart("Neural Net", "X", "Y",
                        new String[]{"Education", ACTUAL, "Test"},
                        xData, new double[][]{educationData, educationData, testData}
                );
            }

            @Override
            public double[] getX(final List<EducationSample> samples) {
                final float minInput = getNetConfiguration().getMinInput();

                final double[] xData = new double[samples.size()];
                xData[0] = minInput;
                for (int i = 1; i < xData.length; i++) {
                    xData[i] = xData[i - 1] + STEP_SIZE;
                }
                return xData;
            }

            @Override
            public double[] getY(final List<EducationSample> samples, final INet net) {
                final double[] actData = new double[fullData.size()];
                int i = 0;
                for (final EducationSample sample : fullData) {
                    actData[i] = net.run(sample.getInputsSample())[0];
                    i++;
                }
                return actData;
            }

            private double[] getEducationY() {
                final List<EducationSample> samples = fullData.subList(0, getEducationDataSource().getEducationData().size());

                final double[] educationData = new double[fullData.size()];
                int i = 0;
                for (final EducationSample sample : samples) {
                    educationData[i] = sample.getExpectedOutputs()[0];
                    i++;
                }
                for (; i < educationData.length; i++) {
                    educationData[i] = 0;
                }
                return educationData;
            }

            private double[] getTestY() {
                final List<EducationSample> samples = getEducationDataSource().getTestData();
                final double[] testData = new double[fullData.size()];
                int i = 0;
                for (; i < testData.length - samples.size(); i++) {
                    testData[i] = 0;
                }
                for (final EducationSample sample : samples) {
                    testData[i] = sample.getExpectedOutputs()[0];
                    i++;
                }
                return testData;
            }
        };
    }

    @Override
    protected final List<EducationSample> loadEducationData() {
        final float minInput = this.getNetConfiguration().getMinInput();
        final float maxInput = this.getNetConfiguration().getMaxInput();

        final float testDataSize = (maxInput - minInput) * PREDICTION_TEST_SIZE;

        return generateData(STEP_SIZE, minInput, maxInput - testDataSize);
    }

    @Override
    protected final List<EducationSample> loadTestData() {
        final float minInput = this.getNetConfiguration().getMinInput();
        final float maxInput = this.getNetConfiguration().getMaxInput();

        final float testDataSize = (maxInput - minInput) * PREDICTION_TEST_SIZE;

        return generateData(STEP_SIZE, maxInput - testDataSize, maxInput);
    }

    @Override
    public INetParameters getParameters() {
        return RandomWeightNetParameters.getDefaultParameters();
    }

    private List<EducationSample> generateData(final float step, final float minInput, final float maxInput) {
        final int inputsAmount = this.getNetConfiguration().getInputsAmount();

        final List<EducationSample> list = new ArrayList<>();
        for (float x = minInput + step * inputsAmount; x < maxInput; x += step) {
            final float[] input = new float[inputsAmount];
            for (int i = 0; i < inputsAmount; i++) {
                input[i] = f(x - step * (inputsAmount - i));
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