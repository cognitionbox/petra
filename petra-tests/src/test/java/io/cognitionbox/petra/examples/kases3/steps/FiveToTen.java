package io.cognitionbox.petra.examples.kases3.steps;

import io.cognitionbox.petra.examples.kases3.objects.Something;
import io.cognitionbox.petra.lang.PEdge;

public class FiveToTen extends PEdge<Something> {
    {
        type(Something.class);
        kase(x -> x.in>5 && x.in<10, x -> x.out==2);
        func(x ->{
           x.out = 2;
        });
    }
}