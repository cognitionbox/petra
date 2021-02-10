package io.cognitionbox.petra.examples.fizzbuzz2;

import io.cognitionbox.petra.lang.PEdge;

import java.util.Arrays;


public class PrintEdge extends PEdge<X> {
    {
        type(X.class);
        pre(x -> x.getLines() != null);
        func(x -> System.out.println(Arrays.toString(x.getLines())));
        post(x -> true);
    }
}
