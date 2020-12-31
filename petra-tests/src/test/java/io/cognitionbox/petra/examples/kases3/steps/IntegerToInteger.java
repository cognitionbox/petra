package io.cognitionbox.petra.examples.kases3.steps;

import io.cognitionbox.petra.examples.kases3.objects.Something2;
import io.cognitionbox.petra.lang.PEdge;

public class IntegerToInteger extends PEdge<Something2> {
    {
        type(Something2.class);
        kase(x -> x.in.get()>0 && x.in.get()<5, x -> x.out.get()==1);
        kase(x -> x.in.get()>5 && x.in.get()<10, x -> x.out.get()==2);
        func(x ->{
           x.out.set(x.lookup.get(x.in.get()).get());
        });
    }
}