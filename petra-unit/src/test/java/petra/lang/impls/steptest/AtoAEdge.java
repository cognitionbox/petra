package petra.lang.impls.steptest;

import io.cognitionbox.petra.lang.PEdge;

public class AtoAEdge extends PEdge<A> {
    {
        type(A.class);
        pre(a -> a.value == 1);
        func(a -> {
            a.value = 222;
        });
        post(a -> a.value == 222);
    }
}