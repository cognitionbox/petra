package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye6.steps;

import io.cognitionbox.petra.examples.hawkeye.steps.hawkeye6.objects.Outer;
import io.cognitionbox.petra.lang.PEdge;

public class LengthTakeI extends PEdge<Outer> {
    {
        type(Outer.class);
        kase(o->true,o->true);
        func(o->{
            o.i.result(getParent().loopIteration());
            o.j.result(getParent().loopIteration()+1);
            o.jMin.set(o.i.result());
            o.lengthMinusI.result(o.integers.size());
        });
    }
}
