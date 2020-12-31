package io.cognitionbox.petra.examples.kases3.steps;

import io.cognitionbox.petra.examples.kases3.objects.Something;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.seq;

public class SomethingProcessorBuggy extends PGraph<Something> {
    {
        finite();
        type(Something.class);
        kase(x -> x.in>0 && x.in<5, x -> x.out==1);
        kase(x -> x.in>5 && x.in<10, x -> x.out==2);
        kase(x -> x.in>10 && x.in<15, x -> x.out==3);
        step(x->x,new ZeroToFive(),seq());
        step(x->x,new FiveToTen(),seq());
        step(x->x,new TenToFifthteen(),seq());
    }
}