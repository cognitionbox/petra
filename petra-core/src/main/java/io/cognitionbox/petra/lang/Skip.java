package io.cognitionbox.petra.lang;

public class Skip extends PEdge<Object> {
    {
        type(Object.class);
        pre(o -> false);
        func(o -> {});
        post(o -> false);
    }
}
