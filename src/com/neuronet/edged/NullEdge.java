package com.neuronet.edged;

import com.neuronet.edged.api.INeuron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NullEdge extends Edge {

    private static final Logger logger = LoggerFactory.getLogger(NullEdge.class);
    private static final Edge INSTANCE = new NullEdge();

    private NullEdge() {
        super(null, null);
    }

    public static Edge getInstance() {
        return INSTANCE;
    }

    public float getWeight() {
        return 0;
    }

    public INeuron getInput() {
        return new INeuron() {
            @Override
            public float getFunction() {
                return 0;
            }

            @Override
            public float getDerived() {
                return 0;
            }

            @Override
            public float[] educate(float error, float[] signal) {
                return new float[0];
            }

            @Override
            public float getLastPotential() {
                return 0;
            }

            @Override
            public void setLastPotential(float signal) {
            }

            @Override
            public Edge createInputEdge(INeuron inputNeuron, float weight) {
                return null;
            }

            @Override
            public void addInputEdge(Edge inputEdge) {
            }

            @Override
            public void addOutputEdge(Edge outputEdge) {
            }
        };
    }

    public void incrementWeight(float incWeight) {
    }
}