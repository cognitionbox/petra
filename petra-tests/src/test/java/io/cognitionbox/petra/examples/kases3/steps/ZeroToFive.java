package io.cognitionbox.petra.examples.kases3.steps;

import io.cognitionbox.petra.examples.kases3.objects.Something;
import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.util.Petra.ro;

public class ZeroToFive extends PEdge<Something> {
    {
        type(Something.class);
        kase(x -> x.in>0 && x.in<5, x -> x.out==1);
        func(x ->{
           x.out = 1;
        });
    }
}