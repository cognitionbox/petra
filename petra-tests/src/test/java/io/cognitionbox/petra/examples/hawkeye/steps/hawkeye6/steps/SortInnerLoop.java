package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye6.steps;

import io.cognitionbox.petra.examples.hawkeye.steps.hawkeye6.objects.Outer;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.math.Assign;
import io.cognitionbox.petra.lang.math.IntAdd;

import static io.cognitionbox.petra.util.Petra.seq;

public class SortInnerLoop extends PGraph<Outer> {
    {
        type(Outer.class);
        iterations(o->o.integers.size());
        kase(o->!o.integers.isEmpty(),inner->true);
        step(o->o.i,new Assign<>(i->i.result()),seq());
        step(o->o.i,new IntAdd(i->1),seq());
        step(o->o,new InnerEdge());
    }
}
