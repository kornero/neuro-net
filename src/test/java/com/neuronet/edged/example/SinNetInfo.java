package com.neuronet.edged.example;

import com.neuronet.generator.SimpleNetInfo;
import com.neuronet.util.FunctionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * net = sin(x), where x ~ [ 0; 3.1415].
 */
public class SinNetInfo extends SimpleNetInfo {

    private static final Logger logger = LoggerFactory.getLogger(SinNetInfo.class);

    private static final int MIN_NEURONS = 5;
    private static final int MAX_NEURONS = 100;
    private static final int MIN_LAYERS = 1;
    private static final int MAX_LAYERS = 3;
    private static final int INPUTS = 1;
    private static final int OUTPUTS = 1;

    private static final float MIN_X = 0; //(float) (Math.PI * (-2))
    private static final float MAX_X = (float) (Math.PI * 2); // (float) (Math.PI * 2)
    private static final float MAX_STEP_SIZE = 0.01f;

    public SinNetInfo() {
        super(MIN_NEURONS, MAX_NEURONS, MIN_LAYERS, MAX_LAYERS, INPUTS, OUTPUTS);
    }

    @Override
    protected List<Map<Float[], Float[]>> loadEducationData() {
        final Random random = new Random();
        final List<Map<Float[], Float[]>> list = new ArrayList<>();
        for (int i = 0; i < 1000 * MAX_STEP_SIZE; i++) {
            final Map<Float[], Float[]> map = new HashMap<>();
            for (float j = MIN_X; j < MAX_X; j += random.nextFloat() * MAX_STEP_SIZE) {
                map.put(new Float[]{j}, new Float[]{(float) Math.sin(j)});
            }
            list.add(map);
        }
        return list;
    }

    @Override
    protected List<Map<Float[], Float[]>> loadTestData() {
        return loadEducationData(); // Generate test data in the same way.
    }

    @Override
    public float getMaxInputValue() {
        return MAX_X;
    }

    @Override
    public FunctionType getOutputFunctionType() {
        return FunctionType.BIPOLAR_SIGMA;
    }
}