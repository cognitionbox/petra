package io.cognitionbox.petra.lang.impls.else_semantics.completewithoutelse;

import io.cognitionbox.petra.lang.PEdge;

public class AtoA1 extends PEdge<A> {
    {
        type(A.class);
        kase(a -> a.value == 1, a -> a.value == 10);
        func(a -> {
            a.value = a.value * 10;
        });
    }
}