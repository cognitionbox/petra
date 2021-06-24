package io.cognitionbox.petra.lang.impls.else_semantics.withelse;

import io.cognitionbox.petra.lang.PEdge;

public class AtoA1 extends PEdge<A> {
    {
        type(A.class);
        kase(a -> a.value == 1, a -> a.value == 2);
        func(a -> {
            a.value++;
        });
    }
}