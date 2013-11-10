package com.neuronet.impl;

import com.neuronet.api.IEducationDataSource;
import com.neuronet.api.generator.EducationSample;

import java.util.Collections;
import java.util.List;

public class ImmutableEducationDataSource implements IEducationDataSource {

    private final List<EducationSample> educationData;
    private final List<EducationSample> testData;

    public ImmutableEducationDataSource(final List<EducationSample> educationData, final List<EducationSample> testData) {
        this.educationData = Collections.unmodifiableList(educationData);
        this.testData = Collections.unmodifiableList(testData);
    }

    @Override
    public List<EducationSample> getEducationData() {
        return this.educationData;
    }

    @Override
    public List<EducationSample> getTestData() {
        return this.testData;
    }
}