package io.cognitionbox.petra.examples.simple2.g_loop_iteration;

import io.cognitionbox.petra.lang.PGraph;

public class SomeLoop extends PGraph<Foo> {
    {
        type(Foo.class);
        iterations(x->x.getValues().size());
        pre(x->x.getValues()!=null);
        post(x->x.getValues().get(loopIteration()).equals("lastone"));
        step(x->x, new Print());
    }
}