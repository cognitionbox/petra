package io.cognitionbox.petra.lang.impls.generics;

import io.cognitionbox.petra.lang.PGraph;

public class XtoX extends PGraph<X> {
    {
        type(X.class);
        kase(
                x -> x.aList.stream().allMatch(a -> a.value == 0),
                x -> x.aList.stream().allMatch(a -> a.value == 1));
            steps(x -> x.aList, AtoAGraph.class);
        esak();
    }
}