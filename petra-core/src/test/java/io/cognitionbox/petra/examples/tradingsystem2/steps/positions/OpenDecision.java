package io.cognitionbox.petra.examples.tradingsystem2.steps.positions;

import io.cognitionbox.petra.examples.tradingsystem2.objects.decisions.Decision;
import io.cognitionbox.petra.examples.tradingsystem2.objects.decisions.Status;
import io.cognitionbox.petra.lang.PEdge;

public class OpenDecision extends PEdge<Decision> {
    {
        type(Decision.class);
        pre(decision -> decision.getStatus().isNEW());
        func(decision -> {
            decision.setStatus(Status.OPENED);
        });
        post(decision -> decision.getStatus().isOPENED());
    }
}
