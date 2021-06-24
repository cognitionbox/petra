package io.cognitionbox.petra.lang.impls.steptest;

import io.cognitionbox.petra.lang.PEdge;

public class AtoAEdgeFalse extends PEdge<A> {
    {
        type(A.class);
        kase(
                a -> false,
                a -> a.value == 222);
        func(a -> {
            a.value = 222;
        });
    }
}