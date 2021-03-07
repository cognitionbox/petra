package io.cognitionbox.petra.examples.tradingsystem2.steps.positions;

import io.cognitionbox.petra.examples.tradingsystem2.objects.decisions.Decision;
import io.cognitionbox.petra.examples.tradingsystem2.objects.decisions.Status;
import io.cognitionbox.petra.lang.PEdge;

public class CloseSellDecisionEOD extends PEdge<Decision> {
    {
        type(Decision.class);
        pre(decision -> decision.getStatus().isOPENED() && decision.getDirection().SHORT());
        func(decision -> {
            decision.setClosedPnl(decision.getEntry().minus(decision.getCurrentQuote().getMid())
                    .multipliedBy(decision.getQuantity()));
            decision.setStatus(Status.CLOSED);
        });
        post(decision -> decision.getStatus().isCLOSED());
    }
}
