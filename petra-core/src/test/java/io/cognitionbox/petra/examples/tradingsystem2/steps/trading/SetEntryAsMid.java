package io.cognitionbox.petra.examples.tradingsystem2.steps.trading;

import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import io.cognitionbox.petra.lang.PEdge;

public class SetEntryAsMid extends PEdge<Trader> {
    {
        type(Trader.class);
        pre(trader -> trader.hasQuote());
        func(trader -> {
            trader.setEntry(trader.getCurrentQuote().getMid());
        });
        post(trader -> trader.getEntry() != null);
    }
}
