package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye6.steps;

import io.cognitionbox.petra.examples.hawkeye.steps.hawkeye6.objects.Outer;
import io.cognitionbox.petra.lang.PEdge;

public class InnerEdge extends PEdge<Outer> {
    {
        type(Outer.class);
        kase(o->o.integers.get(o.j.result()).get()<o.jMin.get(),
                o->o.jMin.get()==o.j.result());
        func(o->{
            o.jMin.set(o.j.result());
        });
    }
}
