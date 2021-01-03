package io.cognitionbox.petra.examples.kases.steps;

import io.cognitionbox.petra.examples.kases.objects.Foo;
import io.cognitionbox.petra.lang.PGraph;

public class FooSum extends PGraph<Foo> {
    {
        type(Foo.class);
        kase(x -> !x.getRList().isEmpty(), x -> x.getSum()!=null);
        step(x->x,new ListSumEdge());
    }
}