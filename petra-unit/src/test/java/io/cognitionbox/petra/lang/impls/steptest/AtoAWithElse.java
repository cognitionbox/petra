package io.cognitionbox.petra.lang.impls.steptest;

import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.impls.else_semantics.Skip;

import static io.cognitionbox.petra.util.Petra.par;

public class AtoAWithElse extends PGraph<A> {
    {
        type(A.class);
        pre(a -> a.value == 1);
        post(a -> a.value == 1);
        step(par(), x -> x, AtoAEdgeFalse.class);
        elseStep(x -> x, Skip.class);
    }
}