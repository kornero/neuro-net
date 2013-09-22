package com.neuronet.example;

import com.neuronet.generator.SimpleNetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SinNetInfo extends SimpleNetInfo {

    private static final Logger logger = LoggerFactory.getLogger(SinNetInfo.class);

    private static final int MIN_NEURONS = 10;
    private static final int MAX_NEURONS = 500;
    private static final int MIN_LAYERS = 2;
    private static final int MAX_LAYERS = 5;
    private static final int INPUTS = 1;
    private static final int OUTPUTS = 1;

    public SinNetInfo() {
        super(MIN_NEURONS, MAX_NEURONS, MIN_LAYERS, MAX_LAYERS, INPUTS, OUTPUTS);
    }

    @Override
    protected List<Map<Float[], Float[]>> loadEducationData() {
        final Random random = new Random();
        final List<Map<Float[], Float[]>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final Map<Float[], Float[]> map = new HashMap<>();
            for (int j = 0; j < 10; j++) {
                final float x = random.nextFloat();
                map.put(new Float[]{x}, new Float[]{(float) Math.sin(x)});
            }
            list.add(map);
        }
        return list;
    }

    @Override
    protected List<Map<Float[], Float[]>> loadTestData() {
        return loadEducationData(); // Generate test data in the same way.
    }
}