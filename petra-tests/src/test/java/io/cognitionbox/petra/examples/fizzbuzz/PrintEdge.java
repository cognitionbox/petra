package io.cognitionbox.petra.examples.fizzbuzz;

import io.cognitionbox.petra.lang.PEdge;


public class PrintEdge extends PEdge<Object> {
    {
        type(Object.class);
//        pre(x -> true);
        func(System.out::println);

        post(x -> true);
    }
}
