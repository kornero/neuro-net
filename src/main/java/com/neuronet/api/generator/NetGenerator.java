package com.neuronet.api.generator;

import com.neuronet.api.*;
import com.neuronet.impl.NetBuilder;
import com.neuronet.impl.NetLearner;
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
    private static final int DEFAULT_EDUCATION_ROUNDS = 50;
    private final int educationRounds;
    private final INetInfo netInfo;

    public NetGenerator(final INetInfo netInfo) {
        this(netInfo, DEFAULT_EDUCATION_ROUNDS);
    }

    public NetGenerator(final INetInfo netInfo, final int educationRounds) {
        this.netInfo = netInfo;
        this.educationRounds = educationRounds;
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

    public NavigableMap<Float, INet> generateNet(final float threshold,
                                                 final long timeout, final TimeUnit timeUnit) {
        final int threads = Runtime.getRuntime().availableProcessors();
        return generateNet(threads, threshold, timeout, timeUnit);
    }

    public NavigableMap<Float, INet> generateNet(final int threads, final float threshold,
                                                 final long timeout, final TimeUnit timeUnit) {
        final Map<Float, INet> iNets = new ConcurrentHashMap<>();
        final Random random = new Random();

        final INetConfiguration netConfiguration = netInfo.getNetConfiguration();
        final INetParameters netParameters = netInfo.getParameters();

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
                            for (int i = 1; i <= layers; i++) {
                                final int neurons = random.nextInt(maxNeurons - 1) + minNeurons;
                                netBuilder.addLayer(neurons, FunctionsFactory.getRandomFunction());
                            }
                            netBuilder.addLayer(outputs, FunctionsFactory.getRandomFunction()); // Output layer.

                            final INet net = netBuilder.build();
                            if (checkNet(net)) {
                                educateNet(net, threshold);
                                final float error = examineNet(net);
                                if (error < threshold) {
                                    iNets.put(error, net);
                                    logger.debug("Error: {}, net: {}", error, Util.summary(net));
                                }
                            }
                        }
                    } catch (final RuntimeException e) {
                        logger.error("Thread " + me.getName(), e);
                    }
                }
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(timeout, timeUnit);
            service.shutdownNow();
        } catch (final InterruptedException e) {
            logger.warn("generateNet(): Interrupted.", e);
            Thread.currentThread().interrupt();
        }
        return new TreeMap<>(iNets);
    }

    public float examineNet(final INet net) {
        final IEducationDataSource educationDataSource = netInfo.getEducationDataSource();
        final INetConfiguration netConfiguration = netInfo.getNetConfiguration();

        float error = 0.0f;
        for (final EducationSample sample : educationDataSource.getTestData()) {
            final float[] runResult = Util.denormalizeOutputs(net.run(sample.getInputsSample()), netConfiguration);

            error += Util.absMeanDifference(runResult, sample.getExpectedOutputs());
        }
        return error;// / (float) educationDataSource.getTestData().size();
    }

    public void educateNet(final INet net, final float errorThreshold) {
        final NetLearner learner = new NetLearner(net, netInfo);
        learner.learn(educationRounds, errorThreshold);
    }
}