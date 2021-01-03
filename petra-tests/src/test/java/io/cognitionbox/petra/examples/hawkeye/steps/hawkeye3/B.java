package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye3;

import io.cognitionbox.petra.examples.hawkeye.objects.X;
import io.cognitionbox.petra.examples.hawkeye.steps.BM;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.lang.math.BigDecimalUtils.r;
import static io.cognitionbox.petra.util.Petra.seq;

public class B extends PGraph<X> {
    {
        type(X.class);
        kase(x -> x.isFour(), x -> x.isEight());
        kase(x -> x.isEight(), x -> x.isSixteen());
        step(x->x.i,new BM<>(x->r(2)),seq());
    }
}