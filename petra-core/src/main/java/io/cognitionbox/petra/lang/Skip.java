package io.cognitionbox.petra.lang;

public class Skip extends PEdge<Object> {

    public Skip() {
        type(Object.class);
        pre(o -> false);
        func(o -> {
        });
        post(o -> false);
    }

}
