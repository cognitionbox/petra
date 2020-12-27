package io.cognitionbox.petra.examples.simple2.b_sequence1;

import io.cognitionbox.petra.lang.PGraph;

public class SeqGraph extends PGraph<X> {
    {
        type(X.class);
        pre(x -> x.isAB());
        step(new SeqEdge());
        post(x -> x.isC());
    }
}