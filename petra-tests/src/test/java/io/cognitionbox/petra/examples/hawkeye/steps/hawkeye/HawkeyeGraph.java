package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye;

import io.cognitionbox.petra.examples.hawkeye.objects.X;
import io.cognitionbox.petra.examples.hawkeye.steps.BM;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.math.Assign;
import io.cognitionbox.petra.lang.math.BigMult;

import static io.cognitionbox.petra.lang.math.BigDecimalUtils.r;
import static io.cognitionbox.petra.util.Petra.seq;

public class HawkeyeGraph extends PGraph<X> {
    {
        type(X.class);
        iterations(3);
        kase(x -> x.i.result().equals(r(1)), x -> x.i.result().equals(r(2)));
        kase(x -> x.i.result().equals(r(2)), x -> x.i.result().equals(r(4)));
        kase(x -> x.i.result().equals(r(4)), x -> x.i.result().equals(r(8)));
        step(x->x.i,new BM<>(x->r(2)),seq());
    }
}