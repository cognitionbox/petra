package io.cognitionbox.petra.lang.impls.else_semantics;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.par;
import static io.cognitionbox.petra.util.Petra.seq;

public class AtoB extends PGraph<X> {
    {
        type(X.class);
        pre(x->x.value1.value =="A" && x.value2.value =="A");
        step(par(),x->x.value1, AtoBEdge.class);
        step(par(),x->x.value2, AtoBEdge.class);
        stepForall(seq(),x->x.values, AtoBEdge.class);
        elseStep(x->x.value2,Skip.class);
        post(x->x.values.stream().allMatch(y->y.value=="B"));
    }
}
