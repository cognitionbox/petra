package io.cognitionbox.petra.examples.kases.steps;

import io.cognitionbox.petra.examples.kases.objects.Foo;
import io.cognitionbox.petra.examples.kases.math.R;
import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.examples.kases.math.R.isZero;

public class ListSumEdge extends PEdge<Foo> {
    {
        type(Foo.class);
        kase(x -> !x.getRList().isEmpty(), x -> x.getSum()!=null);
        kase(x -> x.getRList().isEmpty(), x -> isZero(x.getSum()));
        func(x->{
           x.setSum(x.iterationValue().add(x.getSum()));
        });
    }
}