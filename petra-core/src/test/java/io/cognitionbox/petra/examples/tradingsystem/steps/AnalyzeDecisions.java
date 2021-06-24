package io.cognitionbox.petra.examples.tradingsystem.steps;

import io.cognitionbox.petra.examples.tradingsystem.objects.DecisionsStore;
import io.cognitionbox.petra.lang.PEdge;

public class AnalyzeDecisions extends PEdge<DecisionsStore> {
    {
        type(DecisionsStore.class);
        kase(decisionsStore -> decisionsStore.hasDecisions(), decisionsStore -> decisionsStore.hasAvgLimitPrice());
        func(decisionsStore -> decisionsStore.analyzeDecisions());
    }
}