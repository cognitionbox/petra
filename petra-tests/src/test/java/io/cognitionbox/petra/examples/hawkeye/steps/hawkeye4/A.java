package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye4;

import io.cognitionbox.petra.examples.hawkeye.steps.BM;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.math.BigAdd;

import static io.cognitionbox.petra.lang.math.BigDecimalUtils.r;
import static io.cognitionbox.petra.util.Petra.seq;

public class A extends PGraph<X> {
    {
        type(X.class);
        kase(x -> x.isInRangeA(), x -> x.isInRangeB());
        step(x->x.i,new BigAdd<>(x->r(1)),seq());
    }
}