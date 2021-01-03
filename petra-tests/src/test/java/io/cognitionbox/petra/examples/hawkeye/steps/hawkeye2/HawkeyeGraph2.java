package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye2;

import io.cognitionbox.petra.examples.hawkeye.objects.X;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.lang.math.BigDecimalUtils.r;
import static io.cognitionbox.petra.util.Petra.seq;

public class HawkeyeGraph2 extends PGraph<X> {
    {
        type(X.class);
        iterations(3);
        kase(x -> x.i.result().equals(r(1)), x -> x.i.result().equals(r(2)));
        kase(x -> x.i.result().equals(r(2)), x -> x.i.result().equals(r(4)));
        kase(x -> x.i.result().equals(r(4)), x -> x.i.result().equals(r(8)));
        step(x->x,new FourToEight(),seq());
        step(x->x,new TwoToFour(),seq());
        step(x->x,new OneToTwo(),seq());
    }
}