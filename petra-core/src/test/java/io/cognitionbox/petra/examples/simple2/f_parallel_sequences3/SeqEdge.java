package io.cognitionbox.petra.examples.simple2.f_parallel_sequences3;

import io.cognitionbox.petra.lang.PEdge;

public class SeqEdge extends PEdge<Y> {
    {
        type(Y.class);
        kase(
                y -> y.isA() ^ y.isB(),
                y -> y.isB() ^ y.isC());
        func(y -> {
            y.state(State.values()[y.state().ordinal() + 1]);
        });
    }
}