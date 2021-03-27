package io.cognitionbox.petra.lang.impls.else_semantics.completewithoutelse;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.choice;
import static io.cognitionbox.petra.util.Petra.seq;

public class AtoA extends PGraph<A> {
    {
        type(A.class);
        pre(a -> a.value == 1 ^ a.value == 2);
        begin();
        step(choice(), x -> x, AtoA1.class);
        step(choice(), x -> x, AtoA2.class);
        end();
        post(a -> a.value == 10 ^ a.value == 4);
    }
}