package io.cognitionbox.petra.examples.tradingsystem2.steps.factors;

import io.cognitionbox.petra.examples.tradingsystem2.objects.State;
import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.examples.tradingsystem2.TradingSystem2Main.SHORT_INSTRUMENT;

public class ComputeGoShort extends PEdge<State> {
    {
        type(State.class);
        pre(state -> true);
        func(state -> {
            state.getChosenLongShortPair().setToShort(SHORT_INSTRUMENT);
        });
        post(state -> state.getChosenLongShortPair().getToShort() != null);
    }
}
