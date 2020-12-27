package io.cognitionbox.petra.examples.simple2.e_parallel_sequences2;

import io.cognitionbox.petra.lang.PEdge;

public class SeqEdge1 extends PEdge<X> {
    {
        type(X.class);
        pre(x -> x.y1().isA() ^ x.y1().isB());
        func(x -> {
            x.y1().state(State.values()[x.y1().state().ordinal() + 1]);
        });
        post(x -> x.y1().isB() ^ x.y1().isC());
    }
}