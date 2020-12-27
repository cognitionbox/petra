package io.cognitionbox.petra.examples.kases2.steps;

import io.cognitionbox.petra.examples.kases2.objects.Foo;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.math.IOp;
import io.cognitionbox.petra.util.function.IFunction;

import static io.cognitionbox.petra.util.Petra.ro;
import static io.cognitionbox.petra.util.Petra.rw;

public class CreateVar extends PEdge<Foo> {
    {
        type(Foo.class);
        kase(x ->true,x->true);
        func(x ->{
           x.bool = ro(false);
        });
    }
}