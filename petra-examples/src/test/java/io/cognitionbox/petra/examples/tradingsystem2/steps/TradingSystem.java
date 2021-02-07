package io.cognitionbox.petra.examples.tradingsystem2.steps;

import io.cognitionbox.petra.examples.tradingsystem2.objects.State;
import io.cognitionbox.petra.lang.PGraph;

public class TradingSystem extends PGraph<State> {
    {
        type(State.class);
        pre(state ->true);

        post(state->false);
    }
}