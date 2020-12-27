package io.cognitionbox.petra.examples.simple2.c_sequence2;

import io.cognitionbox.petra.lang.PEdge;

public class SeqEdge1 extends PEdge<X> {
    {
        type(X.class);
        pre(x -> x.isA());
        func(x -> {
            x.state(State.B);
        });
        post(x -> x.isB());
    }
}