package io.cognitionbox.petra.lang.impls.generics;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.seq;

public class AtoAGraph extends PGraph<A> {
    {
        type(A.class);
        kase(a -> a.value == 0, a -> a.value == 1);
            step(seq(), AtoA.class);
        esak();
    }
}