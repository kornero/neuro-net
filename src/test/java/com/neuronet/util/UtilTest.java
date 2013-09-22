package com.neuronet.util;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class UtilTest {

    private static final Logger logger = LoggerFactory.getLogger(UtilTest.class);

    @Test
    public void test_randomFloats() {
        for (int i = 4; i < 16; i++) {
            final float[] floats = Util.randomFloats(i);
            Assert.assertEquals("Wrong floats amount", i, floats.length);

            for (final float f : floats) {
                Assert.assertTrue("Float smaller than -1: " + f, -1.00f <= f);
                Assert.assertTrue("Float bigger than 1: " + f, 1.00f >= f);
            }

            Arrays.sort(floats);
            logger.debug("random floats: {}", floats);

            Assert.assertTrue("Random floats must have negative values.", floats[0] < 0);
            Assert.assertTrue("Random floats must have positive values.", floats[i - 1] > 0);
            Assert.assertTrue("Bad randomisation", Math.abs(floats[0] - floats[i - 1]) > 0.1f);
        }
    }

    @Test
    public void test_randomFloatsNoNaN() {
        for (int i = 0; i < 1000; i++) {
            final float[] floats = Util.randomFloats(1);
            Assert.assertFalse("Random float is NaN!", "NaN".equalsIgnoreCase(Float.toString(floats[0])));
        }
    }

    @Test
    public void test_multiply() {
        final float[] inputs = new float[]{2, 3, 4, 5};
        final float[] weights = new float[]{1, 2, 3, 4, 5};
        final float b = 0f;
        final float expected = 0 + 4 + 9 + 16 + 25;

        final float actual = Util.multiply(inputs, weights, b);

        Assert.assertEquals("Wrong result.", expected, actual, 0.01);
    }

    @Test
    public void test_getNorm() {
        final float[] inputs = new float[]{2, 3, 4, 5};
        final float expected = (float) Math.sqrt(4 + 9 + 16 + 25);

        final float actual = Util.getNorm(inputs);

        Assert.assertEquals("Wrong result.", expected, actual, 0.1);
    }

    @Test
    public void test_normalize() {
        final float[] inputs = new float[]{3, 4, 5, 5, 5}; // norm = 10
        final float[] expected = new float[]{0.3f, 0.4f, 0.5f, 0.5f, 0.5f};

        Util.normalize(inputs);

        Assert.assertEquals("Wrong result.",
                Arrays.toString(expected),
                Arrays.toString(inputs));
    }

    @Test
    public void test_chance() {
        Assert.assertTrue("100% chance wasn't.", Util.chance(1));

        Assert.assertFalse("0.0001% chance was (we are too lucky today?).", Util.chance(100 * 1000));

        int sum = 0;
        for (int i = 0; i < 10 * 1000; i++) {
            sum += Util.chance(2) ? 1 : 0;
        }
        sum /= 100;
        Assert.assertTrue("50% chance, but was: " + sum, 45 <= sum && sum <= 55);

        sum = 0;
        for (int i = 0; i < 10 * 1000; i++) {
            sum += Util.chance(3) ? 1 : 0;
        }
        Assert.assertEquals("33% chance, but was: " + sum / 100, 3, sum / 1000);
    }
}