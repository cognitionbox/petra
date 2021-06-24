package io.cognitionbox.petra.lang.impls.else_semantics;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.par;
import static io.cognitionbox.petra.util.Petra.seq;

public class AtoB extends PGraph<X> {
    {
        type(X.class);
        kase(x -> x.value1.value == "A" && x.value2.value == "A",
                x -> x.values.stream().allMatch(y -> y.value == "B"));

        step(par(), x -> x.value1, AtoBEdge.class);
        step(par(), x -> x.value2, AtoBEdge.class);
        steps(seq(), x -> x.values, AtoBEdge.class);
        esak();
    }
}
