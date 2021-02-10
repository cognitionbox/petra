package io.cognitionbox.petra.examples.fizzbuzz;

import io.cognitionbox.petra.lang.PEdge;

public class IncrementX extends PEdge<X> {
    {
        type(X.class);
        pre(x -> true);
        func(x -> {
           x.i++;
           System.out.println(String.valueOf("incr: " + x.i));
        });
        post(x -> true);
    }
}