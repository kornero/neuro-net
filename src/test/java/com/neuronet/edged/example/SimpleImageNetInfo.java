package com.neuronet.edged.example;

import com.neuronet.generator.SimpleNetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    protected List<Map<Float[], Float[]>> loadEducationData() {
        final List<Map<Float[], Float[]>> list = new ArrayList<>();
        final Map<Float[], Float[]> map = new HashMap<>();

        // H
        map.put(new Float[]
                {
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                }, new Float[]{1f});
        map.put(new Float[]
                {
                        0f, 0f, 0f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                }, new Float[]{1f});
        map.put(new Float[]
                {
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        1f, 0f, 1f,
                        0f, 0f, 0f,
                }, new Float[]{1f});

        // 4
        map.put(new Float[]
                {
                        0f, 0f, 0f,
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        1f, 0f, 1f,
                }, new Float[]{0f});
        map.put(new Float[]
                {
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        0f, 0f, 1f,
                        0f, 0f, 1f,
                }, new Float[]{0f});
        map.put(new Float[]
                {
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        0f, 0f, 1f,
                        0f, 0f, 1f,
                        0f, 0f, 0f,
                }, new Float[]{0f});

        list.add(map);
        return list;
    }

    @Override
    protected List<Map<Float[], Float[]>> loadTestData() {
        final List<Map<Float[], Float[]>> list = new ArrayList<>();
        final Map<Float[], Float[]> map = new HashMap<>();

        // H
        map.put(new Float[]
                {
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        1f, 0f, 1f,
                        1f, 0f, 1f,
                        0f, 0f, 0f,
                }, new Float[]{1f});

        // 4
        map.put(new Float[]
                {
                        0f, 0f, 0f,
                        1f, 0f, 1f,
                        1f, 1f, 1f,
                        0f, 0f, 1f,
                        0f, 0f, 0f,
                }, new Float[]{0f});

        list.add(map);
        return list;
    }
}