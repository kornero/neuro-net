package com.neuronet.view;

public class VisualizerFactory {

    public static IVisualizer getVisualizer() {
//        return new Visualizer();
        return new ManyInputsOneOutputVisualizer();
    }
}