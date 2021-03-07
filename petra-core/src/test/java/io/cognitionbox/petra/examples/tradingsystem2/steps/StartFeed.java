package io.cognitionbox.petra.examples.tradingsystem2.steps;

import io.cognitionbox.petra.examples.tradingsystem2.objects.State;
import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.util.Petra.forAll;

public class StartFeed extends PEdge<State> {
    {
        type(State.class);
        pre(state -> state.hasFeed() && forAll(String.class, state.getInstruments(), i -> !state.getHistoricalFeed().isStarted(i)));
        func(state -> {
            state.getInstruments().forEach(i -> {
                state.getHistoricalFeed().start(i);
            });
        });
        post(state -> state.hasFeed() && forAll(String.class, state.getInstruments(), i -> state.getHistoricalFeed().isStarted(i)));
    }
}
