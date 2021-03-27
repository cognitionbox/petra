package io.cognitionbox.petra.lang.impls.steptest.init;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.choice;

public class IncrementMachine extends PGraph<Foo> {
    {
        type(Foo.class);
        iterations(i->10);
        pre(foo -> foo.result == 0);
        begin();
        init(foo -> foo, Increment.class);
        end();
        post(foo -> foo.result==1);
    }
}
