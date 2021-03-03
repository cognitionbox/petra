package io.cognitionbox.petra.lang.impls.else_semantics.incompletewithoutelse;

import io.cognitionbox.petra.lang.PGraph;

public class AtoA extends PGraph<A> {
    {
        type(A.class);
        pre(a -> a.value == 1 ^ a.value == 2);
        begin();
        step(AtoA1.class);
        step(AtoA2.class);
        end();
        post(a -> a.value == 2 ^ a.value == 3);
    }
}