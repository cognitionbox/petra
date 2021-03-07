package io.cognitionbox.petra.examples.tradingsystem.objects;

import io.cognitionbox.petra.lang.Ref;
import io.cognitionbox.petra.util.impl.PList;

import static io.cognitionbox.petra.util.Petra.ref;

public class DecisionsStore {
    private PList<Decision> allDecisions = new PList<>();
    private Ref<Double> averageDecisionLimitPrice = ref();

    public PList<Decision> getAllDecisions() {
        return allDecisions;
    }

    public Double getAverageDecisionLimitPrice() {
        return averageDecisionLimitPrice.get();
    }

    public void analyzeDecisions() {
        averageDecisionLimitPrice.set(allDecisions.stream().mapToDouble(d -> d.getLimit()).average().orElse(0));
    }

    public boolean hasDecisions() {
        return !allDecisions.isEmpty();
    }

    public boolean hasAvgLimitPrice() {
        return getAverageDecisionLimitPrice() != null;
    }
}
