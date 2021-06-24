package io.cognitionbox.petra.lang.impls.else_semantics.completewithoutelse;

import io.cognitionbox.petra.lang.PEdge;

public class AtoA2 extends PEdge<A> {
    {
        type(A.class);
        kase(a -> a.value == 2, a -> a.value == 4);
        func(a -> {
            a.value = a.value * 2;
        });
    }

    // All conditions must be in disjunctive normal form AND
    // All the positive conditions must be covered for pre/post conditions AND
    // there should be no exceptions caused by failed post-conditions.

    // i.e.
    // minRequireNoOfCoveredConditions = totalNoOfDisjointConditions(preOrPostCondition)

    // this works because Petra requires positive pre conditions to allow steps to start and
    // positive post conditions to allow steps to complete successfully.

    // Use this until we find another solution which could be
    // something like setting JaCoCo or Sonar cube to only check for
    // positive condition coverage.

}