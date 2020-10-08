package io.cognitionbox.petra.examples.tradingsystem.steps;

import io.cognitionbox.petra.examples.tradingsystem.objects.DecisionsStore;
import io.cognitionbox.petra.lang.PEdge;

public class AnalyzeDecisions extends PEdge<DecisionsStore> {
    {
        type(DecisionsStore.class);
        pre(x->x.hasDecisions());
        func(x->x.analyzeDecisions());
        post(x->x.hasAvgLimitPrice());
    }
}