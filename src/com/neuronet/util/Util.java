package com.neuronet.util;

import com.neuronet.edged.NullEdge;
import com.neuronet.edged.api.IEdge;
import com.neuronet.edged.api.ILayer;
import com.neuronet.edged.api.INet;
import com.neuronet.edged.api.INeuron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Random;

public class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    private static final Random rnd = new Random(System.currentTimeMillis());

    public static final int UPPER_BOUND = 2 * 1000;

    public static final float MIN_POSITIVE = 0.0001f;

    /**
     * Generates random floats.
     * All floats are from interval: [-1.00; 1.00]
     *
     * @param amount Random floats amount.
     * @return Array of floats from interval: [-1.00; 1.00]
     */
    public static float[] randomFloats(final int amount) {
        final float[] tmp = new float[amount];
        final float offset = UPPER_BOUND / 2;
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = (offset - rnd.nextInt(UPPER_BOUND)) / offset;
        }
        return tmp;
    }

    /**
     * Generates random floats.
     * All floats are from interval: [-1.00 * multiplexer; 1.00 * multiplexer]
     *
     * @param amount      Random floats amount.
     * @param multiplexer Multiply each value.
     * @return Array of floats from interval: [-1.00 * multiplexer; 1.00 * multiplexer]
     */
    public static float[] randomFloats(final int amount, final float multiplexer) {
        final float[] tmp = new float[amount];
        final float offset = UPPER_BOUND / 2;
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = (offset - rnd.nextInt(UPPER_BOUND)) * multiplexer / offset;
        }
        return tmp;
    }

    /**
     * Multiplying input data with neuron weights and it's dx.
     *
     * @param inputData Input data.
     * @param weights   Neuron weights (size = inputData + 1).
     * @param b         Neuron dx.
     * @return Neuron potential.
     */
    public static float multiply(final float[] inputData, final float[] weights, final float b) {
        float temp = 0;

        //  Dx.
        temp += b * weights[0];

        //  Weights.
        for (int i = 0; i < inputData.length; i++) {
            temp += inputData[i] * weights[i + 1];
        }

        return temp;
    }

    /**
     * Norm(x, y, z) = (x^2 + y^2 + z^2) ^ 0.5
     *
     * @param floats Input data.
     * @return Norm for given data.
     */
    public static float getNorm(float... floats) {
        float sum = 0;
        for (final float d : floats) {
            sum += d * d;
        }
        return (float) Math.sqrt(sum);
    }

    /**
     * Normalized x = x / norm.
     * Method normalizing given data.
     *
     * @param inputData Data will be normalized.
     * @see Util#getNorm(float[])
     */
    public static void normalize(final float[] inputData) {

        //  Calculating norm.
        final float norma = getNorm(inputData);

        //  Normalizing.
        for (int i = 0; i < inputData.length; i++) {
            inputData[i] = div(inputData[i], norma);
        }
    }

    /**
     * Returns true if generated random number is zero.
     * 0 == rnd.nextInt(chances);
     *
     * @param chances Must be positive: > 0.
     * @return True or false, depends on given chance.
     */
    public static boolean chance(final int chances) {
        return 0 == rnd.nextInt(chances);
    }

    public static float div(final double a, final double b) {
        return div((float) a, (float) b);
    }

    public static float div(float a, float b) {
        if (Float.isInfinite(a)) {
            a = (a == Float.POSITIVE_INFINITY) ? Float.MAX_VALUE : Float.MIN_VALUE;
        }
        if (Float.isInfinite(b)) {
            b = (b == Float.POSITIVE_INFINITY) ? Float.MAX_VALUE : Float.MIN_VALUE;
        }

        if (Math.abs(b) < MIN_POSITIVE) {
            return a / MIN_POSITIVE;
        } else {
            return a / b;
        }
    }

    /**
     * Formatting as 2 digits float:
     * [-1,00, -1,00, 0,99]
     *
     * @param floats Floats array.
     * @return String representation of array.
     */
    public static String toString(final float... floats) {
        final StringBuilder stringBuilder = new StringBuilder("[");
        for (final float f : floats) {
            stringBuilder.append(toString(f)).append(", ");
        }
        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "]");
        return stringBuilder.toString();
    }

    /**
     * Formatting as 2 digits float: "111,99".
     * <p/>
     * Implementation is:
     * String.format("%.2f", f);
     *
     * @param f Float value.
     * @return String representation of float.
     */
    public static String toString(final float f) {
        return String.format("%.2f", f);
    }

    public static String summary(final INet net) {
        final StringBuilder builder = new StringBuilder();
        builder.append("NeuroNet [").append(net.getInputsAmount()).append(" -->(");

        for (final ILayer iLayer : net.getLayers()) {
            try {
                builder.append(iLayer.getNeurons().size());
                builder.append("{").append(iLayer.getFunctionType()).append("}");
                builder.append("-->");
            } catch (UnsupportedOperationException ignore) {
                // Nothing bad.
            }
        }

        builder.append(")--> ").append(net.getOutputsAmount()).append("]");
        return builder.toString();
    }

    public static String toString(final INet net) {
        final StringBuilder builder = new StringBuilder();
        builder.append("NeuroNet [").append(net.getInputsAmount()).append(" --> ").append(net.getOutputsAmount()).append("]");
        for (final ILayer iLayer : net.getLayers()) {
            builder.append("\n").append(iLayer);
        }
        return builder.toString();
    }

    public static String toString(final ILayer layer) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Layer [").append(layer.getNeurons().size()).append("]");
        for (final INeuron neuron : layer.getNeurons()) {
            try {
                builder.append("\n").append(neuron);
            } catch (UnsupportedOperationException ignore) {
                // Nothing bad.
            }
        }
        return builder.toString();
    }

    public static String toString(final INeuron neuron) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Neuron [").append(neuron.getPosition()).append("], ");
        builder.append(" alfa=").append(neuron.getAlfa()).append(", dx=").append(neuron.getDX());
        for (final IEdge edge : neuron.getInputEdges()) {
            if (!(edge instanceof NullEdge)) {
                builder.append("\n").append(edge);
            }
        }
        return builder.toString();
    }

    public static String toString(final IEdge edge) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Edge [").append(edge.getWeight()).append("]");
        return builder.toString();
    }

    public static void serialize(final Serializable object, final File file) {
        try {
            final FileOutputStream fileOut = new FileOutputStream(file);
            final ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(object);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in: " + file);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static <T extends Serializable> T deserialize(T t, final File file) {
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            t = (T) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Employee class not found");
            c.printStackTrace();
        }
        return t;
    }
}
