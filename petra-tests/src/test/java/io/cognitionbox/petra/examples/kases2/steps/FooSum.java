package io.cognitionbox.petra.examples.kases2.steps;

import io.cognitionbox.petra.examples.kases2.objects.Foo;
import io.cognitionbox.petra.lang.FGraph;

import java.math.BigDecimal;

import static io.cognitionbox.petra.examples.kases.math.R.isZero;

public class FooSum extends FGraph<Foo, BigDecimal> {
    {
        type(Foo.class);
        iterator(x->x.getRList());
        kase(x -> !x.getRList().isEmpty(), x -> x.getSum()!=null);
        kase(x -> x.getRList().isEmpty(), x -> isZero(x.getSum()));
        step(x->x,new ListSumEdge());
    }
}