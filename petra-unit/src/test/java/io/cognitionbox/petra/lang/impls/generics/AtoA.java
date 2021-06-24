package io.cognitionbox.petra.lang.impls.generics;

import io.cognitionbox.petra.lang.PEdge;

public class AtoA extends PEdge<A> {
    {
        type(A.class);
        kase(a -> a.value == 0, a -> a.value == 1);
        func(a -> {
            a.value++;
        });
    }
}