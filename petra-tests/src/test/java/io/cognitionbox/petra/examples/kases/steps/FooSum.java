package io.cognitionbox.petra.examples.kases.steps;

import io.cognitionbox.petra.examples.kases.objects.Foo;
import io.cognitionbox.petra.lang.CGraph;

import java.math.BigDecimal;

import static io.cognitionbox.petra.examples.kases.math.R.isZero;

public class FooSum extends CGraph<Foo, BigDecimal> {
    {
        type(Foo.class);
        collection(x->x.getRList());
        kase(x -> !x.getRList().isEmpty(), x -> x.getSum()!=null);
        kase(x -> x.getRList().isEmpty(), x -> isZero(x.getSum()));
        step(x->x,new ListSumEdge());
    }
}