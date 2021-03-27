package io.cognitionbox.petra.lang.impls.steptest.iteration;

import io.cognitionbox.petra.lang.PGraph;

public class IterationMachine extends PGraph<Foo> {
    {
        type(Foo.class);
        iterations(i->3);
        pre(foo -> foo.result == 0);
        begin();
        step(foo -> foo, Increment.class);
        end();
        post(foo -> foo.result==3);
    }
}
