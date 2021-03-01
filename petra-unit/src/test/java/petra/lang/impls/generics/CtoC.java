package petra.lang.impls.generics;

import io.cognitionbox.petra.lang.PEdge;

public class CtoC extends PEdge<C> {
    {
      type(C.class);
      pre(a->a.value==0); // disjoint pre-conditions
      func(a->{
        a.value++;
      });
      post(a->a.value==1); // disjoint post-conditions
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