package io.cognitionbox.petra.lang.impls.steptest.init;

import io.cognitionbox.petra.lang.PGraph;

public class IncrementMachine extends PGraph<Foo> {
    {
        type(Foo.class);
        iterations(i->10);
        kase(foo -> foo.result == 0, foo -> foo.result==1);

        init(foo -> foo, Increment.class);
        esak();
    }
}
