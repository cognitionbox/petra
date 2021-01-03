package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye3;

import io.cognitionbox.petra.examples.hawkeye.objects.X;
import io.cognitionbox.petra.examples.hawkeye.steps.hawkeye2.FourToEight;
import io.cognitionbox.petra.examples.hawkeye.steps.hawkeye2.OneToTwo;
import io.cognitionbox.petra.examples.hawkeye.steps.hawkeye2.TwoToFour;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.lang.math.BigDecimalUtils.r;
import static io.cognitionbox.petra.util.Petra.seq;

public class HawkeyeGraph3 extends PGraph<X> {
    {
        type(X.class);
        iterations(1);
        kase(x -> x.isOne() || x.isTwo(), x -> x.isTwo() || x.isFour());
        kase(x -> x.isFour() || x.isEight(), x -> x.isEight() || x.isSixteen());
        step(x->x,new B(),seq());
        step(x->x,new A(),seq());
    }
}