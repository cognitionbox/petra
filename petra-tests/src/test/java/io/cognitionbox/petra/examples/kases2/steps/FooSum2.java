package io.cognitionbox.petra.examples.kases2.steps;

import io.cognitionbox.petra.examples.kases2.objects.Foo;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.math.Assign;

import static io.cognitionbox.petra.lang.math.BigDecimalUtils.r;
import static io.cognitionbox.petra.util.Petra.seq;

public class FooSum2 extends PGraph<Foo> {
    {
        iterations(4);
        type(Foo.class);
        kase(x -> x.getAbc1().a().equals(r(2)), x -> x.getAbc1().result().equals(r(12)));
        //step(i->i,new ZeroToFive(),seq());
        step(x->x.getAbc1(),new Assign<>(x->x.a()),seq());
        step(x->x.getAbc1(),new BigMult2<>(x->r(1)),seq());
        step(x->x.getAbc1(),new BigMult2<>(x->r(2)),seq());
        step(x->x.getAbc1(),new BigMult2<>(x->r(3)),seq());
    }
}