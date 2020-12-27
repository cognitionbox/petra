package io.cognitionbox.petra.examples.simple2.f_parallel_sequences3;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.forAll;

public class SeqGraph extends PGraph<X> {
    {
        type(X.class);
        pre(x -> forAll(Y.class, x.ys(), y -> y.isAB()));
        stepForall(x -> x.ys(), new SeqEdge());
        post(x -> forAll(Y.class, x.ys(), y -> y.isC()));
    }
}