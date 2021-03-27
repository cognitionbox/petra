package io.cognitionbox.petra.lang.impls.kases3.steps;

import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.impls.kases3.objects.Something2;

import static io.cognitionbox.petra.util.Petra.seq;

public class SomethingProcessorWithVarCov extends PGraph<Something2> {
    {
        type(Something2.class);
        kase(x -> x.in.get()==0, x -> x.out.get()==1);
        kase(x -> x.in.get()>0 && x.in.get()<3, x -> x.out.get()==1);
        kase(x -> x.in.get()==3, x -> x.out.get()==2);
        kase(x -> x.in.get()>3 && x.in.get()<6, x -> x.out.get()==2);
        begin();
        step(seq(),x->x,new IntegerToInteger());
        end();
    }
}