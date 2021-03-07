package io.cognitionbox.petra.examples.tradingsystem2.steps.factors;

import io.cognitionbox.petra.examples.tradingsystem2.objects.State;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.par;

public class SelectStocksToLongAndShort extends PGraph<State> {
    {
        type(State.class);
        pre(state -> true);
        begin();
        step(par(), state -> state, ComputeGoLong.class);
        step(par(), state -> state, ComputeGoShort.class);
        end();
        post(state -> state.getChosenLongShortPair().getToLong() != null &&
                state.getChosenLongShortPair().getToShort() != null);
    }
}
