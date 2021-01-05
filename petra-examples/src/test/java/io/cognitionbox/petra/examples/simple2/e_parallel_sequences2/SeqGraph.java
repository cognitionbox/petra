package io.cognitionbox.petra.examples.simple2.e_parallel_sequences2;

import io.cognitionbox.petra.lang.PGraph;

public class SeqGraph extends PGraph<X> {
    {
        type(X.class);
        iterations(x->2);
        pre(x -> x.y1().isAB() && x.y2().isAB());
        step(new SeqEdge2());
        step(new SeqEdge1());
        post(x -> x.y1().isC() && x.y2().isC());
    }
}