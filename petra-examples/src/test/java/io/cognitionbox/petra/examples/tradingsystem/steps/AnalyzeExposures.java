package io.cognitionbox.petra.examples.tradingsystem.steps;

import io.cognitionbox.petra.examples.tradingsystem.objects.ExposureStore;
import io.cognitionbox.petra.lang.PEdge;

public class AnalyzeExposures extends PEdge<ExposureStore> {
    {
        type(ExposureStore.class);
        pre(exposureStore->exposureStore.hasExposures());
        func(exposureStore->exposureStore.analyzeExposure());
        post(exposureStore->exposureStore.hasAvgExposure());
    }
}
