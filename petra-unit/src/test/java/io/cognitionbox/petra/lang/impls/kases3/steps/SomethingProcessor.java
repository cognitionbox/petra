package io.cognitionbox.petra.lang.impls.kases3.steps;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.impls.kases3.objects.Something;

import static io.cognitionbox.petra.util.Petra.seq;

public class SomethingProcessor extends PGraph<Something> {
    {
        type(Something.class);
        kase(x -> x.in>0 && x.in<5, x -> x.out==1);
            step(seq(),x->x,new ZeroToFive());
        esak();
        kase(x -> x.in>5 && x.in<10, x -> x.out==2);
            step(seq(),x->x,new FiveToTen());
        esak();
    }
}