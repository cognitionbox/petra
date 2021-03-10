package io.cognitionbox.petra.examples.tradingsystem2.steps.factors;

import io.cognitionbox.petra.examples.tradingsystem2.objects.State;
import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.examples.tradingsystem2.TradingSystem2Test.LONG_INSTRUMENT;

public class ComputeGoLong extends PEdge<State> {
    {
        type(State.class);
        pre(state -> true);
        func(state -> {
            state.getChosenLongShortPair().setToLong(LONG_INSTRUMENT);
        });
        post(state -> state.getChosenLongShortPair().getToLong() != null);
    }
}
