package io.cognitionbox.petra.lang.impls.steptest.iteration;

import io.cognitionbox.petra.lang.PGraph;

public class IterationMachine extends PGraph<Foo> {
    {
        type(Foo.class);
        iterations(i->3);
        kase(foo -> foo.result == 0, foo -> foo.result==3);

        step(foo -> foo, Increment.class);
        esak();
    }
}
