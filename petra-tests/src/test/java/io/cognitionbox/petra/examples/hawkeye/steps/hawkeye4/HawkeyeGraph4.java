package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye4;

import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.math.BigAdd;

import static io.cognitionbox.petra.util.Petra.ignore;
import static io.cognitionbox.petra.util.Petra.seq;

public class HawkeyeGraph4 extends PGraph<X> {
    {
        type(X.class);
        kase(x -> x.isInRangeA(), x -> x.isInRangeB(),ignore(BigAdd.class,1,2,3,4,5));
        kase(x -> x.isInRangeB(), x -> x.isInRangeC(),ignore(BigAdd.class,1,2,3,4,5));
        step(x->x,new A(),seq());
        step(x->x,new B(),seq());
    }
}