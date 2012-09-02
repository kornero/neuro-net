package com.neuronet.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    private static final Random rnd = new Random(System.currentTimeMillis());

    public static final int rand = 200;
    public static final float div = 100f;

    public static float[] randomWeights(final int inputsAmount) {
        float[] tmp = new float[inputsAmount];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = ((rand / 2) - rnd.nextInt(rand)) / div;
        }
        return tmp;
    }

    public static float multiply(final float[] inputData, final float[] weights, final float b) {
        float temp = 0;

        //  смещение
        temp += b * weights[0];

        //  все остальное
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
    public static float getNorm(float[] floats) {
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
            inputData[i] = (inputData[i]) / norma;
        }
    }

    public static String toString(final float... floats) {
        final StringBuilder stringBuilder = new StringBuilder("[");
        for (final float f : floats) {
            stringBuilder.append(toString(f)).append(", ");
        }
        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "]");
        return stringBuilder.toString();
    }

    public static String toString(final float f) {
        return String.format("%.2f", f);
    }

    public static boolean chance(final int chances) {
        return 0 == rnd.nextInt(chances);
    }
}
