package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye6.steps;

import io.cognitionbox.petra.examples.hawkeye.steps.hawkeye6.objects.Outer;
import io.cognitionbox.petra.lang.PEdge;

public class SwapEdge extends PEdge<Outer> {
    {
        type(Outer.class);
        kase(o->o.jMin.get()!=o.i.result(), o->true);
        func(o->{
            o.swap(o.integers,o.i.result(),o.jMin.get());
        });
    }
}
