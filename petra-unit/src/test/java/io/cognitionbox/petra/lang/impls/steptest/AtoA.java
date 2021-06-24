package io.cognitionbox.petra.lang.impls.steptest;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.par;

public class AtoA extends PGraph<A> {
    {
        type(A.class);
        kase(a -> a.value == 1, a -> a.value == 222);

        step(par(), x -> x, AtoAEdge.class);
        esak();
    }
}