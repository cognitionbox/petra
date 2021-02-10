package io.cognitionbox.petra.lang.impls.generics;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.seq;

public class AtoA extends PGraph<A> {
    {
      type(A.class);
      pre(a->a.value==0);
      step(seq(),x->x, BtoB.class);
      step(seq(),x->x, CtoC.class);
      post(a->a.value==1);
    }
  }