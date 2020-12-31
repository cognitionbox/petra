package io.cognitionbox.petra.examples.kases.steps;

import io.cognitionbox.petra.examples.kases.objects.Foo;
import io.cognitionbox.petra.lang.PCollectionGraph;

import java.math.BigDecimal;

import static io.cognitionbox.petra.examples.kases.math.R.isZero;

public class FooSum extends PCollectionGraph<Foo, BigDecimal> {
    {
        type(Foo.class);
        kase(x -> !x.getRList().isEmpty(), x -> x.getSum()!=null);
        kase(x -> x.getRList().isEmpty(), x -> isZero(x.getSum()));
        collection(x->x.getRList());
        step(x->x,new ListSumEdge());
    }
}