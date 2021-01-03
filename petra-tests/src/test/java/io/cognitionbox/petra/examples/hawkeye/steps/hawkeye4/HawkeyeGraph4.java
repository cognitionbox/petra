package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye4;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.seq;

public class HawkeyeGraph4 extends PGraph<X> {
    {
        type(X.class);
        iterations(2);
        kase(x -> x.isInRangeA(), x -> x.isInRangeB());
        kase(x -> x.isInRangeB(), x -> x.isInRangeC());
        step(x->x,new A(),seq());
        step(x->x,new B(),seq());
    }
}