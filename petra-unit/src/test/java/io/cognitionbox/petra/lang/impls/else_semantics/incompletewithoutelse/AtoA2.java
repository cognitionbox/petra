package io.cognitionbox.petra.lang.impls.else_semantics.incompletewithoutelse;

import io.cognitionbox.petra.lang.PEdge;

public class AtoA2 extends PEdge<A> {
    {
        type(A.class);
        kase(a -> a.value == 2, a -> a.value == 3);
        func(a -> {
            a.value++;
        });
    }
}