package io.cognitionbox.petra.lang.impls.else_semantics.completewithoutelse;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.seq;

public class AtoA extends PGraph<A> {
    {
        type(A.class);
        kase(a -> a.value == 1, a -> a.value == 10);
            step(seq(), AtoA1.class);
        esak();
        kase(a -> a.value == 2, a -> a.value == 4);
            step(seq(), AtoA2.class);
        esak();
    }
}