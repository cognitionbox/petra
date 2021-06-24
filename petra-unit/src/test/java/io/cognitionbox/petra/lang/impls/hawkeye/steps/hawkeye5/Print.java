package io.cognitionbox.petra.lang.impls.hawkeye.steps.hawkeye5;

import io.cognitionbox.petra.lang.PEdge;

public class Print extends PEdge<Foo> {
        {
            type(Foo.class);
            kase(foo->true, y ->true);
            func(foo->{
                System.out.println(foo.getValues().get(getParent().loopIteration()));
            });
        }
    }