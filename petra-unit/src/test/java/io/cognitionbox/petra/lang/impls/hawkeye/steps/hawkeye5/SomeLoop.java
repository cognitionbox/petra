package io.cognitionbox.petra.lang.impls.hawkeye.steps.hawkeye5;

import io.cognitionbox.petra.lang.PGraph;

public class SomeLoop extends PGraph<Foo> {
    {
        type(Foo.class);
        iterations(x->x.getValues().size());
        kase(x->x.getValues().get(loopIteration()).equals("b"), x->x.getValues().get(loopIteration()+1).equals("lastone"));
        kase(x->!x.getValues().get(loopIteration()).equals("b"), x->true);

        step(x->x, new Print());
        esak();
    }
}