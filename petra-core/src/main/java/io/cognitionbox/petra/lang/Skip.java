package io.cognitionbox.petra.lang;

public class Skip extends PEdge<Object> {
    {
        pre(o -> true);
        func(o -> {});
        post(o -> true);
    }
}
