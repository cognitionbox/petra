package io.cognitionbox.petra.examples.tradingsystem2.steps.positions;

import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import io.cognitionbox.petra.lang.PEdge;

public class RemoveDecisions extends PEdge<Trader> {
    {
        type(Trader.class);
        pre(trader -> true);
        func(trader -> {
            try {
                trader.getDecisions().stream().filter(d -> d != null && d.getStatus().isCLOSED()).forEach(d -> {
                    trader.getDecisions().remove(d);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        post(trader -> trader.getDecisions().stream().allMatch(d -> !d.getStatus().isCLOSED()));
    }
}
