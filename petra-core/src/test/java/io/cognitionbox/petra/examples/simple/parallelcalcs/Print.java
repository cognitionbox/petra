package io.cognitionbox.petra.examples.simple.parallelcalcs;

import io.cognitionbox.petra.lang.PEdge;

public class Print extends PEdge<Integer> {
    {
        type(Integer.class);
        pre(x->true);
        func(x->{
            System.out.println(x);
        });
        post(x->true);
    }
}
