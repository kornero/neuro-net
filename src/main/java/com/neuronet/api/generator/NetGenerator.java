package com.neuronet.api.generator;

import com.neuronet.api.*;
import com.neuronet.impl.NetBuilder;
import com.neuronet.impl.functions.FunctionsFactory;
import com.neuronet.util.Util;
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

    public static NavigableMap<Float, INet> generateNet(final INetInfo netInfo, final float threshold,
                                                        final long timeout, final TimeUnit timeUnit) {
        final int threads = Runtime.getRuntime().availableProcessors();
        return generateNet(netInfo, threads, threshold, timeout, timeUnit);
    }

    public static NavigableMap<Float, INet> generateNet(final INetInfo netInfo, final int threads, final float threshold,
                                                        final long timeout, final TimeUnit timeUnit) {
        final Map<Float, INet> iNets = new ConcurrentHashMap<>();
        final Random random = new Random();

        final INetConfiguration netConfiguration = netInfo.getNetConfiguration();
        final INetParameters netParameters = netInfo.getParameters();
        final IEducationDataSource educationDataSource = netInfo.getEducationDataSource();

        final int inputs = netConfiguration.getInputsAmount();
        final int outputs = netConfiguration.getOutputsAmount();

        final int minNeurons = netInfo.getMinNeurons();
        final int maxNeurons = netInfo.getMaxNeurons();

        final int minLayers = netInfo.getMinLayers();
        final int maxLayers = netInfo.getMaxLayers();

        final ExecutorService service = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    final Thread me = Thread.currentThread();
                    try {
                        while (!me.isInterrupted()) {
                            logger.trace("Thread {}", me.getName());

                            final int layers = random.nextInt(maxLayers - 1) + minLayers;
                            final INetBuilder netBuilder = new NetBuilder();
                            netBuilder.setNetConfiguration(netConfiguration);
                            netBuilder.setNetParameters(netParameters);
                            netBuilder.addLayer(inputs, null); // Input layer.
                            for (int i = 1; i <= layers; i++) {
                                final int neurons = random.nextInt(maxNeurons - 1) + minNeurons;
                                netBuilder.addLayer(neurons, FunctionsFactory.getRandomFunction());
                            }
                            netBuilder.addLayer(outputs, FunctionsFactory.getRandomFunction()); // Output layer.

                            final INet net = netBuilder.build();
                            if (checkNet(net)) {
                                for (int i = 0; i < EDUCATE_ROUNDS; i++) {
                                    educateNet(net, educationDataSource);
                                }
                                final float error = examineNet(net, educationDataSource);
                                if (error < threshold) {
                                    iNets.put(error, net);
                                    logger.debug("Error: {}, net: {}", error, Util.summary(net));
                                }
                            }
                        }
                    } catch (RuntimeException e) {
                        logger.error("Thread " + me.getName(), e);
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

    public static float examineNet(final INet net, final IEducationDataSource educationDataSource) {
        float error = 0.0f;
        for (final EducationSample sample : educationDataSource.getTestData()) {
            final float[] runResult = net.run(sample.getInputsSample());

            error += Util.absMeanDifference(runResult, sample.getExpectedOutputs());
        }
        return error / (float) educationDataSource.getTestData().size();
    }

    public static void educateNet(final INet net, final IEducationDataSource educationDataSource) {
        for (final EducationSample sample : educationDataSource.getEducationData()) {
            net.educate(sample.getInputsSample(), sample.getExpectedOutputs());
        }
    }

    public static boolean checkNet(final INet net) {
        final int checks = 10;
        final Set<Integer> roundedResults = new HashSet<>();
        final int in = net.getNetConfiguration().getInputsAmount();
        float sum = 0;
        for (int i = 0; i < checks; i++) {
            final float act = net.run(Util.randomFloats(in))[0];
            final int round = (int) (1000 * act);
            roundedResults.add(round);
            sum += Math.abs(act);
        }
        return sum > 1.0f && roundedResults.size() == checks;
    }
}