package io.cognitionbox.petra.examples.tradingsystem2.steps.factors;

import io.cognitionbox.petra.examples.tradingsystem2.objects.State;
import io.cognitionbox.petra.lang.PGraph;

public class Reshuffle extends PGraph<State> {
    {
        type(State.class);
        pre(state -> true);
        begin();
        step(state -> state, SelectStocksToLongAndShort.class);
        end();
        post(state -> true);
    }
}
