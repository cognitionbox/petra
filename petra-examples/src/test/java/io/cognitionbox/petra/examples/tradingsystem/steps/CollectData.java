package io.cognitionbox.petra.examples.tradingsystem.steps;

import io.cognitionbox.petra.examples.tradingsystem.objects.State;
import io.cognitionbox.petra.examples.tradingsystem.objects.Trader;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.forAll;
import static io.cognitionbox.petra.util.Petra.seq;

public class CollectData extends PGraph<State> {
    {
        type(State.class);
        pre(state->forAll(Trader.class,state.traders(), t->t.hasGtZeroDecisions()) &&
                (state.getExposureStore().hasGtZeroExposures() || state.exposureEqZero()) );
        step(state->state,new CollectionDecisions(),seq());
        step(state->state,new CollectExposure(),seq());
        post(state->forAll(Trader.class,state.traders(), t->t.hasEqZeroDecisions()) &&
                state.getExposureStore().hasGtZeroExposures());
    }
}
