package io.cognitionbox.petra.examples.simple2.e_parallel_sequences2;

import io.cognitionbox.petra.lang.PEdge;

public class SeqEdge2 extends PEdge<X> {
    {
        type(X.class);
        pre(x -> x.y2().isA() ^ x.y2().isB());
        func(x -> {
            x.y2().state(State.values()[x.y2().state().ordinal() + 1]);
        });
        post(x -> x.y2().isB() ^ x.y2().isC());
    }
}