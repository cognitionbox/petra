package io.cognitionbox.petra.lang.impls.hawkeye.steps.hawkeye5;

import io.cognitionbox.petra.lang.PEdge;

public class Print extends PEdge<Foo> {
        {
            type(Foo.class);
            pre(foo->true);
            func(foo->{
                System.out.println(foo.getValues().get(getParent().loopIteration()));
            });
            post(y ->true);
        }
    }