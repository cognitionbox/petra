package io.cognitionbox.petra.lang.impls.kases3.steps;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.impls.kases3.objects.Something;

public class TenToFifthteen extends PEdge<Something> {
    {
        type(Something.class);
        kase(x -> x.in>5 && x.in<10, x -> x.out==2);
        func(x ->{
           x.out = 2;
        });
    }
}