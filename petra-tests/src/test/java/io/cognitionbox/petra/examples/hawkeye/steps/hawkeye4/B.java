package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye4;

import io.cognitionbox.petra.examples.hawkeye.steps.BM;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.math.BigAdd;

import static io.cognitionbox.petra.lang.math.BigDecimalUtils.r;
import static io.cognitionbox.petra.util.Petra.seq;

public class B extends PGraph<X> {
    {
        type(X.class);
        kase(x -> x.isInRangeB(), x -> x.isInRangeC());
        step(x->x.i,new BigAdd<>(x->r(2)),seq());
    }
}