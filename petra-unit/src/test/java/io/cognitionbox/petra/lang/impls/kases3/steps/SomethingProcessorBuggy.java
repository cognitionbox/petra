package io.cognitionbox.petra.lang.impls.kases3.steps;

import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.impls.kases3.objects.Something;

import static io.cognitionbox.petra.util.Petra.seq;

public class SomethingProcessorBuggy extends PGraph<Something> {
    {
        type(Something.class);
        kase(x -> x.in>0 && x.in<5, x -> x.out==1);
        kase(x -> x.in>5 && x.in<10, x -> x.out==2);
        kase(x -> x.in>10 && x.in<15, x -> x.out==3);
        step(seq(),x->x,new ZeroToFive());
        step(seq(),x->x,new FiveToTen());
        step(seq(),x->x,new TenToFifthteen());
    }
}