package io.cognitionbox.petra.lang.impls.else_semantics.incompletewithoutelse;

import io.cognitionbox.petra.lang.PGraph;

public class AtoA extends PGraph<A> {
    {
        type(A.class);
        kase(a -> a.value == 1,
             a -> a.value == 2);
            step(AtoA1.class);
        esak();
        kase(a -> a.value == 2,
             a -> a.value == 3);
            step(AtoA2.class);
        esak();
    }
}