package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye3;

import io.cognitionbox.petra.examples.hawkeye.objects.X;
import io.cognitionbox.petra.examples.hawkeye.steps.BM;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.lang.math.BigDecimalUtils.r;
import static io.cognitionbox.petra.util.Petra.seq;

public class A extends PGraph<X> {
    {
        type(X.class);
        kase(x -> x.isOne(), x -> x.isTwo());
        kase(x -> x.isTwo(), x -> x.isFour());
        step(x->x.i,new BM<>(x->r(2)),seq());
    }
}