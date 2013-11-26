package com.neuronet.view;

public class VisualizerFactory {

    public static IVisualizer getVisualizer() {
        return new ManyInputsOneOutputVisualizer();
    }

    public static IVisualizer getSimpleMathFunctionVisualizer() {
        return new SimpleMathFunctionVisualizer();
    }
}