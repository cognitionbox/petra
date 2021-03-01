package petra.lang.impls.steptest;

import io.cognitionbox.petra.lang.PEdge;

public class AtoAEdgeFalse extends PEdge<A> {
    {
        type(A.class);
        pre(a -> false);
        func(a -> {
            a.value = 222;
        });
        post(a -> a.value == 222);
    }
}