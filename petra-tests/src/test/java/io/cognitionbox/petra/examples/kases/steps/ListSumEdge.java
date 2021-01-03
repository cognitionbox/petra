package io.cognitionbox.petra.examples.kases.steps;

import io.cognitionbox.petra.examples.kases.objects.Foo;
import io.cognitionbox.petra.examples.kases.math.R;
import io.cognitionbox.petra.lang.PCollectionEdge;
import io.cognitionbox.petra.lang.PEdge;

import java.math.BigDecimal;

import static io.cognitionbox.petra.examples.kases.math.R.isZero;

public class ListSumEdge extends PCollectionEdge<Foo, BigDecimal> {
    {
        type(Foo.class);
        collection(x->x.getRList());
        kase(x -> !x.getRList().isEmpty(), x -> x.getSum()!=null);
        func((x,y)->{
           x.setSum(y.add(x.getSum()));
        });
    }
}