package petra.lang.impls.generics;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.choice;

public class AtoA extends PGraph<A> {
    {
        type(A.class);
        pre(a -> a.value == 0);
        begin();
        step(choice(), BtoB.class);
        step(choice(), CtoC.class);
        end();
        post(a -> a.value == 1);
    }
}