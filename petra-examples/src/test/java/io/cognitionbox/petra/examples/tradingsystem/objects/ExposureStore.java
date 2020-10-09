package io.cognitionbox.petra.examples.tradingsystem.objects;

import io.cognitionbox.petra.lang.Ref;
import io.cognitionbox.petra.util.impl.PList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.cognitionbox.petra.util.Petra.ref;

public class ExposureStore {

    static final Logger LOG = LoggerFactory.getLogger(ExposureStore.class);

    private PList<Double> exposures = new PList<>();
    private Ref<Double> averageExposure = ref();

    public void addExposure(Double exp){
        exposures.add(exp);
    }

    public Double getExposure(){
        return exposures.stream().mapToDouble(e->e).max().orElse(0);
    }

    public void analyzeExposure(){
        averageExposure.set(exposures.stream().mapToDouble(e->e).average().orElse(0));
        LOG.info("Exposure: "+getExposure());
    }

    public boolean hasExposures() {
        return !exposures.isEmpty();
    }

    public boolean hasAvgExposure() {
        return averageExposure.get()!=null;
    }
}
