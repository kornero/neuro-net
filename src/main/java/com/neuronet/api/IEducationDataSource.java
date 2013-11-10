package com.neuronet.api;

import com.neuronet.api.generator.EducationSample;

import java.util.List;

public interface IEducationDataSource {

    public List<EducationSample> getEducationData();

    public List<EducationSample> getTestData();
}