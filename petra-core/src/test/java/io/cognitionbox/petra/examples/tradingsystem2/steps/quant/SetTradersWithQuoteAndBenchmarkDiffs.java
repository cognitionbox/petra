package io.cognitionbox.petra.examples.tradingsystem2.steps.quant;

import io.cognitionbox.petra.examples.tradingsystem2.objects.State;
import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.util.Petra.forAll;

public class SetTradersWithQuoteAndBenchmarkDiffs extends PEdge<State> {
    {
        type(State.class);
        pre(state -> forAll(Trader.class, state.getTraders(), t -> t.getQuoteDiffs() == null));
        func(state -> {
            state.getTraders().forEach(t -> {
                t.setQuoteDiffs(state.getHistoricalFeed().getQuoteDiffsForInstrument(t.getInstrument()));
                t.setBenchmarkDiffs(state.getHistoricalFeed().getQuoteDiffsForInstrument("USA500.IDXUSD"));
            });
        });
        post(state -> forAll(Trader.class, state.getTraders(),
                t -> t.getQuoteDiffs() != null && t.getBenchmarkDiffs() != null));
    }
}
