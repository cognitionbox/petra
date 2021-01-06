package io.cognitionbox.petra.examples.kases.steps;

import io.cognitionbox.petra.examples.kases.objects.Foo;
import io.cognitionbox.petra.lang.PGraph;

import java.math.BigDecimal;

public class FooSum extends PGraph<Foo> {
    {
        type(Foo.class);
        kase(x -> x.getRList().isEmpty() ^ !x.getRList().isEmpty(),
                x -> x.getSum().compareTo(BigDecimal.ZERO)==1 ||
                x.getSum().compareTo(BigDecimal.ZERO)==0);
        step(x->x,new ListSumEdge());
    }
}