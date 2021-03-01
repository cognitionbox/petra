package io.cognitionbox.petra.examples.tradingsystem.steps;

import io.cognitionbox.petra.examples.tradingsystem.objects.DecisionsStore;
import io.cognitionbox.petra.lang.PEdge;

public class AnalyzeDecisions extends PEdge<DecisionsStore> {
    {
        type(DecisionsStore.class);
        pre(decisionsStore -> decisionsStore.hasDecisions());
        func(decisionsStore -> decisionsStore.analyzeDecisions());
        post(decisionsStore -> decisionsStore.hasAvgLimitPrice());
    }
}