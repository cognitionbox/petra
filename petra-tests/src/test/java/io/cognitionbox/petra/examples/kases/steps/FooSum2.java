package io.cognitionbox.petra.examples.kases.steps;

import io.cognitionbox.petra.examples.kases.math.Mult;
import io.cognitionbox.petra.examples.kases.math.R;
import io.cognitionbox.petra.examples.kases.math.AssignUn;
import io.cognitionbox.petra.examples.kases.objects.Foo;
import io.cognitionbox.petra.lang.PGraph;

import java.math.BigDecimal;

import static io.cognitionbox.petra.util.Petra.seq;

public class FooSum2 extends PGraph<Foo> {
    {
        type(Foo.class);
        kase(x ->x.getAbc().toUn().a().equals(R.TWO), x -> x.getAbc().toUn().result().equals(new BigDecimal(12)));
        kase(x ->x.getAbc().toUn().a().equals(R.TWO), x -> x.getAbc().toUn().result().equals(new BigDecimal(12)));
        step(x->x.getAbc().toUn(),new Mult(R.ONE),seq());
        step(x->x.getAbc().toUn(),new AssignUn(),seq());
        step(x->x.getAbc().toUn(),new Mult(R.TWO),seq());
        step(x->x.getAbc().toUn(),new AssignUn(),seq());
        step(x->x.getAbc().toUn(),new Mult(R.THREE),seq());
    }
}