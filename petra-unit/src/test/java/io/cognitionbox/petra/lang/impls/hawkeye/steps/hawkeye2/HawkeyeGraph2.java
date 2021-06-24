package io.cognitionbox.petra.lang.impls.hawkeye.steps.hawkeye2;

import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.impls.hawkeye.objects.X;

import static io.cognitionbox.petra.util.Petra.seq;

public class HawkeyeGraph2 extends PGraph<X> {
    {
        type(X.class);
        kase(x -> x.isOne(), x ->  x.isEight());

        step(seq(),x->x,new OneToTwo());
        step(seq(),x->x,new TwoToFour());
        step(seq(),x->x,new FourToEight());
        esak();
    }
}