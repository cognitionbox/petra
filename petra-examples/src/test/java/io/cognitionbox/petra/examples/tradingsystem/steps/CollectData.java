package io.cognitionbox.petra.examples.tradingsystem.steps;

import io.cognitionbox.petra.examples.tradingsystem.objects.State;
import io.cognitionbox.petra.examples.tradingsystem.objects.Trader;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.forAll;
import static io.cognitionbox.petra.util.Petra.seq;

public class CollectData extends PGraph<State> {
    {
        type(State.class);
        pre(state->forAll(Trader.class,state.traders(), trader->trader.hasGtZeroDecisions()));
        step(seq(), state->state,new CollectionDecisions());
        step(seq(), state->state,new CollectExposure());
        post(state->state.hasDecisions() &&
                forAll(Trader.class,state.traders(), trader->trader.hasEqZeroDecisions()) &&
                state.getExposureStore().hasExposures());
    }
}
