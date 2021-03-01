package petra.lang.impls.else_semantics.withelse;

import io.cognitionbox.petra.lang.PGraph;

public class AtoA extends PGraph<A> {
    {
        type(A.class);
        pre(a -> a.value == 1 ^ a.value == 2);
        begin();
        step(AtoA1.class);
        elseStep(x -> x, AtoA2.class);
        end();
        post(a -> a.value == 2 ^ a.value == 3);
    }
}