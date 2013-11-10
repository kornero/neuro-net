package com.neuronet.util;

import com.neuronet.api.*;
import com.neuronet.impl.InputLayer;
import com.neuronet.impl.NullEdge;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Random;

public class Util {

    public static final int UPPER_BOUND = 2 * 1000;
    public static final float MIN_POSITIVE = 0.0001f;
    private static final Logger logger = LoggerFactory.getLogger(Util.class);
    private static final Random rnd = new Random(System.currentTimeMillis());

    /**
     * Generates random floats.
     * All floats are from interval: [-1.00; 1.00]
     *
     * @param amount Random floats amount.
     * @return Array of floats from interval: [-1.00; 1.00]
     */
    public static float[] randomFloats(final int amount) {
        return randomFloats(amount, 1);
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
    public static float getNorm(final float... floats) {
        return getNorm(0, floats);
    }

    /**
     * Norm(x, y, z) = (x^2 + y^2 + z^2) ^ 0.5
     *
     * @param floats   Input data.
     * @param maxValue Special value - max of normalization.
     * @return Norm for given data.
     */
    public static float getNorm(final float maxValue, final float... floats) {
        float sum = 0;
        for (final float d : floats) {
            sum += d * d;
        }
        return (float) Math.sqrt(sum + maxValue * maxValue);
    }

    /**
     * Normalized x = x / norm.
     * Method normalizing given data.
     *
     * @param data Data will be normalized.
     * @see Util#getNorm(float[])
     */
    public static void normalize(final float[] data) {
        normalize(data, 0);
    }

    /**
     * Normalized x = x / norm.
     * Method normalizing given data.
     *
     * @param data     Data will be normalized.
     * @param maxValue Special value - max of normalization.
     * @see Util#getNorm(float[])
     */
    public static void normalize(final float[] data, final float maxValue) {

        //  Calculating norm.
        final float norma = getNorm(maxValue, data);

        //  Normalizing.
        for (int i = 0; i < data.length; i++) {
            data[i] = div(data[i], norma);
        }
    }

    /**
     * Normalized x = (x - xMin) / (xMax - xMin).
     * Method do not change input data.
     *
     * @param data     Data for normalization.
     * @param minValue Min of normalization.
     * @param maxValue Max of normalization.
     * @return New normalized array.
     */
    public static float[] normalize(final float[] data, final float minValue, final float maxValue) {
        if (ArrayUtils.isEmpty(data)) {
            throw new IllegalArgumentException("Data can't be empty or null.");
        }
        if (minValue >= maxValue) {
            throw new IllegalArgumentException("Min value >= max value: min=" + minValue + ", max=" + maxValue);
        }

        //  Calculating norm.
        final float norm = maxValue - minValue;

        //  Normalizing.
        final float[] normalizedData = new float[data.length];
        for (int i = 0; i < data.length; i++) {
            normalizedData[i] = (data[i] - minValue) / norm;
        }
        return normalizedData;
    }

    /**
     * Method do not change input data.
     *
     * @param inputData        Data for normalization.
     * @param netConfiguration Required for min and max input values parameters.
     * @return New normalized array.
     * @see Util#normalize(float[], float, float)
     */
    public static float[] normalizeInputs(final float[] inputData, final INetConfiguration netConfiguration) {
        if (netConfiguration == null) {
            throw new NullPointerException("NetConfiguration can't be null");
        }

        return normalize(inputData, netConfiguration.getMinInput(), netConfiguration.getMaxInput());
    }

    /**
     * Method do not change input data.
     *
     * @param outputData       Data for normalization.
     * @param netConfiguration Required for min and max input values parameters.
     * @return New normalized array.
     * @see Util#normalize(float[], float, float)
     */
    public static float[] normalizeOutputs(final float[] outputData, final INetConfiguration netConfiguration) {
        if (netConfiguration == null) {
            throw new NullPointerException("NetConfiguration can't be null");
        }

        return normalize(outputData, netConfiguration.getMinOutput(), netConfiguration.getMaxOutput());
    }

    /**
     * Normalized x = (x - xMin) / (xMax - xMin).
     * Method do not change input data.
     *
     * @param data     Data for normalization.
     * @param minValue Min of normalization.
     * @param maxValue Max of normalization.
     * @return New normalized array.
     */
    public static double[] normalize(final double[] data, final double minValue, final double maxValue) {
        if (ArrayUtils.isEmpty(data)) {
            throw new IllegalArgumentException("Data can't be empty or null.");
        }
        if (minValue >= maxValue) {
            throw new IllegalArgumentException("Min value >= max value: min=" + minValue + ", max=" + maxValue);
        }

        //  Calculating norm.
        final double norm = maxValue - minValue;

        //  Normalizing.
        final double[] normalizedData = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            normalizedData[i] = (data[i] - minValue) / norm;
        }
        return normalizedData;
    }

    /**
     * Method do not change input data.
     *
     * @param inputData        Data for normalization.
     * @param netConfiguration Required for min and max input values parameters.
     * @return New normalized array.
     * @see Util#normalize(double[], double, double)
     */
    public static double[] normalizeInputs(final double[] inputData, final INetConfiguration netConfiguration) {
        if (netConfiguration == null) {
            throw new NullPointerException("NetConfiguration can't be null");
        }

        return normalize(inputData, netConfiguration.getMinInput(), netConfiguration.getMaxInput());
    }

    /**
     * Method do not change input data.
     *
     * @param outputData       Data for normalization.
     * @param netConfiguration Required for min and max input values parameters.
     * @return New normalized array.
     * @see Util#normalize(double[], double, double)
     */
    public static double[] normalizeOutputs(final double[] outputData, final INetConfiguration netConfiguration) {
        if (netConfiguration == null) {
            throw new NullPointerException("NetConfiguration can't be null");
        }

        return normalize(outputData, netConfiguration.getMinOutput(), netConfiguration.getMaxOutput());
    }

    /**
     * Normalized xN = (x - xMin) / (xMax - xMin).
     * De normalization: x = xN * (xMax - xMin) + xMin.
     * Method do not change input normalizedData.
     *
     * @param normalizedData Data for de normalization.
     * @param minValue       Min of normalization.
     * @param maxValue       Max of normalization.
     * @return New de normalized array.
     */
    public static float[] denormalize(final float[] normalizedData, final float minValue, final float maxValue) {
        if (ArrayUtils.isEmpty(normalizedData)) {
            throw new IllegalArgumentException("Data can't be empty or null.");
        }
        if (minValue >= maxValue) {
            throw new IllegalArgumentException("Min value >= max value: min=" + minValue + ", max=" + maxValue);
        }

        //  Calculating norm.
        final float norm = maxValue - minValue;

        //  Normalizing.
        final float[] denormalizedData = new float[normalizedData.length];
        for (int i = 0; i < normalizedData.length; i++) {
            denormalizedData[i] = normalizedData[i] * norm + minValue;
        }
        return denormalizedData;
    }

    /**
     * Method do not change input data.
     *
     * @param inputData        Data for normalization.
     * @param netConfiguration Required for min and max input values parameters.
     * @return New normalized array.
     * @see Util#denormalize(float[], float, float)
     */
    public static float[] denormalizeInputs(final float[] inputData, final INetConfiguration netConfiguration) {
        if (netConfiguration == null) {
            throw new NullPointerException("NetConfiguration can't be null");
        }

        return denormalize(inputData, netConfiguration.getMinInput(), netConfiguration.getMaxInput());
    }

    /**
     * Method do not change input data.
     *
     * @param outputData       Data for normalization.
     * @param netConfiguration Required for min and max input values parameters.
     * @return New normalized array.
     * @see Util#denormalize(float[], float, float)
     */
    public static float[] denormalizeOutputs(final float[] outputData, final INetConfiguration netConfiguration) {
        if (netConfiguration == null) {
            throw new NullPointerException("NetConfiguration can't be null");
        }

        return denormalize(outputData, netConfiguration.getMinOutput(), netConfiguration.getMaxOutput());
    }

    /**
     * Normalized xN = (x - xMin) / (xMax - xMin).
     * De normalization: x = xN * (xMax - xMin) + xMin.
     * Method do not change input normalizedData.
     *
     * @param normalizedData Data for de normalization.
     * @param minValue       Min of normalization.
     * @param maxValue       Max of normalization.
     * @return New de normalized array.
     */
    public static double[] denormalize(final double[] normalizedData, final double minValue, final double maxValue) {
        if (ArrayUtils.isEmpty(normalizedData)) {
            throw new IllegalArgumentException("Data can't be empty or null.");
        }
        if (minValue >= maxValue) {
            throw new IllegalArgumentException("Min value >= max value: min=" + minValue + ", max=" + maxValue);
        }

        //  Calculating norm.
        final double norm = maxValue - minValue;

        //  Normalizing.
        final double[] denormalizedData = new double[normalizedData.length];
        for (int i = 0; i < normalizedData.length; i++) {
            denormalizedData[i] = normalizedData[i] * norm + minValue;
        }
        return denormalizedData;
    }

    /**
     * Method do not change input data.
     *
     * @param inputData        Data for normalization.
     * @param netConfiguration Required for min and max input values parameters.
     * @return New normalized array.
     * @see Util#denormalize(double[], double, double)
     */
    public static double[] denormalizeInputs(final double[] inputData, final INetConfiguration netConfiguration) {
        if (netConfiguration == null) {
            throw new NullPointerException("NetConfiguration can't be null");
        }

        return denormalize(inputData, netConfiguration.getMinInput(), netConfiguration.getMaxInput());
    }

    /**
     * Method do not change input data.
     *
     * @param outputData       Data for normalization.
     * @param netConfiguration Required for min and max input values parameters.
     * @return New normalized array.
     * @see Util#denormalize(double[], double, double)
     */
    public static double[] denormalizeOutputs(final double[] outputData, final INetConfiguration netConfiguration) {
        if (netConfiguration == null) {
            throw new NullPointerException("NetConfiguration can't be null");
        }

        return denormalize(outputData, netConfiguration.getMinOutput(), netConfiguration.getMaxOutput());
    }

    public static double[] convertFloatsToDoubles(final float[] input) {
        if (input == null) {
            return null;
        }
        final double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i];
        }
        return output;
    }

    public static float absMeanDifference(final float[] sample1, final float[] sample2) throws IllegalArgumentException {
        return absSumDifference(sample1, sample2) / (float) sample1.length;
    }

    public static float absSumDifference(final float[] sample1, final float[] sample2) throws IllegalArgumentException {
        final int n = sample1.length;
        if (n != sample2.length || n < 1) {
            throw new IllegalArgumentException("Input arrays must have the same (positive) length.");
        }
        float result = 0.0f;
        for (int i = 0; i < n; i++) {
            result += Math.abs(sample1[i] - sample2[i]);
        }
        return result;
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

    /**
     * Returns true if generated random number is bigger than given.
     * chances >= rnd.nextFloat();
     *
     * @param chances Must be positive: > 0.
     * @return True or false, depends on given chance.
     */
    public static boolean chance(final float chances) {
        return chances >= rnd.nextFloat();
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

    public static boolean equals(final float a, final float b) {
        return equals(a, b, MIN_POSITIVE);
    }

    public static boolean equals(final float a, final float b, final float delta) {
        return Math.abs(a - b) < delta;
    }

    public static String summary(final INet net) {
        final StringBuilder builder = new StringBuilder();
        builder.append("NeuroNet [").append(net.getNetConfiguration().getInputsAmount());

        for (final ILayer iLayer : net.getLayers()) {
            if (!(iLayer instanceof InputLayer)) {
                builder.append("-->");
                builder.append("{").append(iLayer.getLayerConfiguration().
                        getFunction().getClass().getSimpleName()).append("}");
                builder.append("-->");
                builder.append(iLayer.getNeurons().size());
            }
        }

        builder.append("]");
        return builder.toString();
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

    public static String toString(final INet net) {
        final StringBuilder builder = new StringBuilder();
        builder.append("NeuroNet [").append(net.getNetConfiguration().getInputsAmount()).append(" --> ").append(net.getNetConfiguration().getOutputsAmount()).append("]");
        int i = 0;
        for (final ILayer iLayer : net.getLayers()) {
            builder.append("\n\t").append("#").append(i++).append(" ");
            builder.append(iLayer.toString().replace("\n", "\n\t"));
        }
        return builder.toString();
    }

    public static String toString(final ILayer layer) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Layer [").append(layer.getNeurons().size()).append("]");
        int i = 0;
        for (final INeuron neuron : layer.getNeurons()) {
            builder.append("\n\t").append("#").append(i++).append(" ");
            builder.append(neuron.toString().replace("\n", "\n\t"));
        }
        return builder.toString();
    }

    public static String toString(final INeuron neuron) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Neuron [").append(neuron.getPosition()).append("], ");
        builder.append("dx=").append(neuron.getDx());
        builder.append("\n\tInputs");
        int i = 0;
        for (final IEdge edge : neuron.getInputEdges()) {
            if (edge != NullEdge.getInstance()) {
                builder.append("\n\t\t").append("#").append(i++).append(" --> ");
                builder.append(edge.toString().replace("\n", "\n\t"));
            }
        }
        builder.append("\n\tOutputs");
        i = 0;
        for (final IEdge edge : neuron.getOutputEdges()) {
            if (edge != NullEdge.getInstance()) {
                builder.append("\n\t\t").append("#").append(i++).append(" <-- ");
                builder.append(edge.toString().replace("\n", "\n\t"));
            }
        }
        return builder.toString();
    }

    public static String toString(final IEdge edge) {
        return "Edge [" + edge.getWeight() + "]";
    }

    public static void serialize(final Serializable object, final File file) {
        try {
            final FileOutputStream fileOut = new FileOutputStream(file);
            final ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(object);
            out.close();
            fileOut.close();
            logger.info("Serialized object {} is saved in: {}", object.getClass().getSimpleName(), file);
        } catch (IOException i) {
            logger.error("serialize(): File=" + file, i);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deserialize(final File file) {
        try {
            final FileInputStream fileIn = new FileInputStream(file);
            final ObjectInputStream in = new ObjectInputStream(fileIn);
            final T t = (T) in.readObject(); // Unchecked cast.
            in.close();
            fileIn.close();
            logger.info("Deserialized object {} is loaded from: {}", t.getClass().getSimpleName(), file);
            return t;
        } catch (IOException i) {
            logger.error("deserialize(): File=" + file, i);
        } catch (ClassNotFoundException c) {
            logger.error("deserialize(): Class not found, file=" + file, c);
        }
        return null;
    }
}
