package io.cognitionbox.petra.lang.impls.else_semantics.withelse;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.seq;

public class AtoA extends PGraph<A> {
    {
      type(A.class);
      pre(a->a.value==1 ^ a.value==2);
      step(seq(),x->x, AtoA1.class);
      elseStep(x->x,AtoA2.class);
      post(a->a.value==2 ^ a.value==3);
    }
  }