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
        kase(x ->x.getRList().size()==0, x -> x.getSum()==BigDecimal.ZERO);
        kase(x ->x.getRList().size()==1, x -> x.getSum()!=BigDecimal.ZERO);
        kase(x ->x.getRList().size()>1, x -> x.getSum()!=BigDecimal.ZERO);
        collection(x->x.getRList());
        func((x,y)->{
           x.setSum(y.add(x.getSum()));
        });
    }
}