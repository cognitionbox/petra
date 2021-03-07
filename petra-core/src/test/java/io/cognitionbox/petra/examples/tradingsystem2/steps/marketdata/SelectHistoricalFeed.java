package io.cognitionbox.petra.examples.tradingsystem2.steps.marketdata;

import io.cognitionbox.petra.examples.tradingsystem2.objects.State;
import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.historical.HistoricalFeed;
import io.cognitionbox.petra.lang.PEdge;

public class SelectHistoricalFeed extends PEdge<State> {
    {
        type(State.class);
        pre(state -> state.getMode().isHISTORICAL() && state.getHistoricalFeed() == null);
        func(state -> {
            state.setHistoricalFeed(new HistoricalFeed());
        });
        post(state -> state.getHistoricalFeed() != null);
    }
}
