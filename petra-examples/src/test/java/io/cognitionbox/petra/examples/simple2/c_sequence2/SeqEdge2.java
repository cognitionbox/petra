package io.cognitionbox.petra.examples.simple2.c_sequence2;

import io.cognitionbox.petra.lang.PEdge;

public class SeqEdge2 extends PEdge<X> {
    {
        type(X.class);
        pre(x -> x.isB());
        func(x -> {
            x.state(State.C);
        });
        post(x -> x.isC());
    }
}