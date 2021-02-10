package io.cognitionbox.petra.lang.impls.generics;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.seq;

public class XtoX extends PGraph<X> {
    {
      type(X.class);
      pre(x->x.aList.stream().allMatch(a->a.value==0));
      stepForall(seq(),x->x.aList, AtoA.class);
      post(x->x.aList.stream().allMatch(a->a.value==1));
    }
  }