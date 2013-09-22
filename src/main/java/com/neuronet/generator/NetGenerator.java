package com.neuronet.generator;

import com.neuronet.edged.Net;
import com.neuronet.edged.api.INet;
import com.neuronet.util.FunctionType;
import com.neuronet.util.Util;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NetGenerator {

    private static final Logger logger = LoggerFactory.getLogger(NetGenerator.class);

    private static final int EDUCATE_ROUNDS = 10;

    public static NavigableMap<Float, INet> generateNet(final NetInfo netInfo, final int threads, final float threshold,
                                                        final long timeout, final TimeUnit timeUnit) {
        final Map<Float, INet> iNets = new ConcurrentHashMap<>();
        final Random random = new Random();
        final int inputs = netInfo.getInputs();
        final int outputs = netInfo.getOutputs();

        final int minNeurons = netInfo.getMinNeurons();
        final int maxNeurons = netInfo.getMaxNeurons();

        final int minLayers = netInfo.getMinLayers();
        final int maxLayers = netInfo.getMaxLayers();

        final float maxValue = (float) (Math.PI * 2);

        final ExecutorService service = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    final Thread me = Thread.currentThread();
                    try {
                        while (!me.isInterrupted()) {
                            final int layers = random.nextInt(maxLayers - 1) + minLayers;
                            final INet net = new Net(inputs, maxValue, netInfo.getConfiguration());
                            for (int i = 1; i <= layers; i++) {
                                final int neurons = random.nextInt(maxNeurons - 1) + minNeurons;
                                net.addLayer(neurons, FunctionType.getRandomFunctionType());
                            }
                            net.addLayer(outputs, FunctionType.getRandomFunctionType());

                            if (checkNet(net)) {
                                for (int i = 0; i < EDUCATE_ROUNDS; i++) {
                                    educateNet(net, netInfo, i);
                                }
                                final float error = examineNet(net, netInfo, 0);
                                if (error < threshold) {
                                    iNets.put(error, net);
                                    logger.debug("Error: {}, net: {}", error, Util.summary(net));
                                }
                            }
                        }
                    } catch (RuntimeException e) {

                    }
                }
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(timeout, timeUnit);
            service.shutdownNow();
        } catch (InterruptedException e) {
            logger.warn("generateNet(): Interrupted.", e);
            Thread.currentThread().interrupt();
        }
        return new TreeMap<>(iNets);
    }

    public static float examineNet(final INet net, final NetInfo netInfo, final int iteration) {
        final Map<Float[], Float[]> testData = netInfo.getTestData(iteration);
        float error = 0.0f;
        for (final Map.Entry<Float[], Float[]> entry : testData.entrySet()) {
            final float[] inputData = ArrayUtils.toPrimitive(entry.getKey());
            final float[] expectedOutputData = ArrayUtils.toPrimitive(entry.getKey());

            final float[] runResult = net.runNet(inputData);

            error += Math.abs(Util.getNorm(runResult) - Util.getNorm(expectedOutputData));
        }
        return error;
    }

    public static void educateNet(final INet net, final NetInfo netInfo, final int iteration) {
        final Map<Float[], Float[]> educationData = netInfo.getEducationData(iteration);
        for (final Map.Entry<Float[], Float[]> entry : educationData.entrySet()) {
            final float[] inputData = ArrayUtils.toPrimitive(entry.getKey());
            final float[] outputData = ArrayUtils.toPrimitive(entry.getKey());

            net.educate(outputData, inputData);
        }
    }

    public static boolean checkNet(final INet net) {
        final int checks = 5;
        final Set<Integer> roundedResults = new HashSet<>();
        final int in = net.getInputsAmount();
        for (int i = 0; i < checks; i++) {
            final float act = net.runNet(Util.randomFloats(in))[0];
            final int round = (int) (1000 * act);
            roundedResults.add(round);
        }
        return roundedResults.size() == checks;
    }
}