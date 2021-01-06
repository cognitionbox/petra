package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye6.steps;

import io.cognitionbox.petra.examples.hawkeye.steps.hawkeye6.objects.Outer;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.math.Assign;
import io.cognitionbox.petra.lang.math.IntAdd;

import static io.cognitionbox.petra.util.Petra.seq;

public class SortOuterLoop extends PGraph<Outer> {
    {
        type(Outer.class);
        iterations(o->o.integers.size()-1);
        kase(o->!o.integers.isEmpty(),x->true);
        step(o->o.i,new IntAdd(i->1),seq());
        step(o->o.lengthMinusI,new Assign<>(o->o.a()),seq());
        step(o->o.lengthMinusI,new IntAdd<>(u->u.a()),seq());
        step(o->o,new AssumeMinEdge(),seq());
        step(o->o,new SortInnerLoop(),seq());
        step(o->o,new SwapEdge(),seq());
    }
}
