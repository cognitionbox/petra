package io.cognitionbox.petra.examples.simple2.d_parallel_sequences1;

import io.cognitionbox.petra.lang.PGraph;

public class SeqGraph extends PGraph<X> {
    {
        type(X.class);
        iterations(x->2);
        pre(x -> (x.y1().isA() ^ x.y1().isB()) && (x.y2().isA() ^ x.y2().isB()));
        step(x -> x.y1(), new SeqEdge());
        step(x -> x.y2(), new SeqEdge());
        post(x -> x.y1().isC() && x.y2().isC());
    }
}