package io.cognitionbox.petra.examples.simple2.c_sequence2;

import io.cognitionbox.petra.lang.PGraph;

public class SeqGraph extends PGraph<X> {
    {
        type(X.class);
        iterations(2);
        pre(x -> x.isAB());
        step(new SeqEdge2());
        step(new SeqEdge1());
        post(x -> x.isC());
    }
}