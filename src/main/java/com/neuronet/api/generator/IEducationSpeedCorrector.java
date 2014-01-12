package com.neuronet.api.generator;

public interface IEducationSpeedCorrector {

    public float correctEducationSpeed(final int learnRound, final float error, final float currentEducationSpeed);
}