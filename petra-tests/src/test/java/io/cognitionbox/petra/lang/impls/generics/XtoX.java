package io.cognitionbox.petra.lang.impls.generics;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.seq;

public class XtoX extends PGraph<X> {
    {
      type(X.class);
      pre(x->x.aList.stream().allMatch(a->a.value==0));
      begin();
      steps(x->x.aList, AtoA.class);
      end();
      post(x->x.aList.stream().allMatch(a->a.value==1));
    }
  }