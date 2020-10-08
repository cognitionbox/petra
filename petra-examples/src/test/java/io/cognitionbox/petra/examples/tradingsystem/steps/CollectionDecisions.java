package io.cognitionbox.petra.examples.tradingsystem.steps;

import io.cognitionbox.petra.examples.tradingsystem.objects.State;
import io.cognitionbox.petra.examples.tradingsystem.objects.Trader;
import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.util.Petra.forAll;

public class CollectionDecisions extends PEdge<State> {
    {
        type(State.class);
        pre(state->forAll(Trader.class,state.traders(), t->t.hasGtZeroDecisions()));
        func(state->state.collectDecisions());
        post(state->forAll(Trader.class,state.traders(), t->t.hasEqZeroDecisions()));
    }
}
