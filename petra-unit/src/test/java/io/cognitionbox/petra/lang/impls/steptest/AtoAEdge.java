package io.cognitionbox.petra.lang.impls.steptest;

import io.cognitionbox.petra.lang.PEdge;

public class AtoAEdge extends PEdge<A> {
    {
        type(A.class);
        kase(a -> a.value == 1, a -> a.value == 222);
        func(a -> {
            a.value = 222;
        });
    }
}