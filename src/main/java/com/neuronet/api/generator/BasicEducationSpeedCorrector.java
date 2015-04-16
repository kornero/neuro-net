package com.neuronet.api.generator;

import com.neuronet.util.Util;
import org.apache.commons.collections15.BoundedCollection;
import org.apache.commons.collections15.buffer.CircularFifoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicEducationSpeedCorrector implements IEducationSpeedCorrector {

    private static final Logger logger = LoggerFactory.getLogger(BasicEducationSpeedCorrector.class);

    private static final int CORRECTION_ERRORS_AMOUNT = 50;
    private static final float SMALL_ERROR_DIF = 0.01f * CORRECTION_ERRORS_AMOUNT;
    private static final float HUGE_ERROR_DIF = 0.05f * CORRECTION_ERRORS_AMOUNT;

    private final BoundedCollection<Float> errors = new CircularFifoBuffer<>(CORRECTION_ERRORS_AMOUNT);

    private volatile double averageError;

    @Override
    public float correctEducationSpeed(final int learnRound, final float error, final float currentEducationSpeed) {
        if (errors.size() + 1 == CORRECTION_ERRORS_AMOUNT) {
            this.averageError = error; // Initial value.
            System.out.println(error);
        }
        errors.add(error);
        if (learnRound % CORRECTION_ERRORS_AMOUNT == 0) {
            final double newAverageError = Util.sum(errors) / errors.size();
            final float errorDx = (float) (this.averageError - newAverageError);
            final float absErrorDx = Math.abs(errorDx);
            final float educationSpeedDx = Math.abs(Util.div(absErrorDx, Math.sqrt(error) + absErrorDx));

            final float correctedEducationSpeed;
            if (0 <= errorDx && errorDx < SMALL_ERROR_DIF) {                              // 1. If error is decreasing, but very slow.
                correctedEducationSpeed = currentEducationSpeed * 2;                      // 1. Increase education speed.
            } else if (0 < errorDx && errorDx < HUGE_ERROR_DIF) {                         // 2. If error is decreasing, but not very fast.
                correctedEducationSpeed = currentEducationSpeed * (1 + educationSpeedDx); // 2. Increase education speed.
            } else if (errorDx < 0 || absErrorDx > 1) {                                   // 3. If error is increasing, or decreasing too fast.
                correctedEducationSpeed = currentEducationSpeed / 5;                      // 3. Decrease education speed.
            } else {
                correctedEducationSpeed = currentEducationSpeed;
            }

            this.averageError = newAverageError;
            if (0.00001 < correctedEducationSpeed && correctedEducationSpeed < 0.5) {
                return correctedEducationSpeed;
            }
        }
        return currentEducationSpeed;
    }
}