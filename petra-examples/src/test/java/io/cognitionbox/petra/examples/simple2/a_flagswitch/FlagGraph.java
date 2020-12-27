package io.cognitionbox.petra.examples.simple2.a_flagswitch;

import io.cognitionbox.petra.lang.PGraph;

public class FlagGraph extends PGraph<X> {
    {
        type(X.class);
        //invariant(x -> x.value==true ^ x.value==false);
        pre(x -> x.value == false);
        step(new FlagEdge());
        post(x -> x.value == true);
    }
}