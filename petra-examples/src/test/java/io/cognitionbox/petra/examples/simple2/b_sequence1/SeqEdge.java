package io.cognitionbox.petra.examples.simple2.b_sequence1;

import io.cognitionbox.petra.lang.PEdge;

public class SeqEdge extends PEdge<X> {
    {
        type(X.class);
        pre(x -> x.isA() ^ x.isB());
        func(x -> {
            x.state(State.values()[x.state().ordinal() + 1]);
        });
        post(x -> x.isB() ^ x.isC());
    }
}