package io.cognitionbox.petra.lang.impls.kases3.steps;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.impls.kases3.objects.Something;

public class ZeroToFive extends PEdge<Something> {
    {
        type(Something.class);
        kase(x -> x.in>0 && x.in<5, x -> x.out==1);
        func(x ->{
           x.out = 1;
        });
    }
}