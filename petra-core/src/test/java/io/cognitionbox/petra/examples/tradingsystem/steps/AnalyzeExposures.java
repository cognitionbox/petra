package io.cognitionbox.petra.examples.tradingsystem.steps;

import io.cognitionbox.petra.examples.tradingsystem.objects.ExposureStore;
import io.cognitionbox.petra.lang.PEdge;

public class AnalyzeExposures extends PEdge<ExposureStore> {
    {
        type(ExposureStore.class);
        kase(exposureStore -> exposureStore.hasExposures(), exposureStore -> exposureStore.hasAvgExposure());
        func(exposureStore -> exposureStore.analyzeExposure());
    }
}
