package io.cognitionbox.petra.lang.impls.generics;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.seq;

public class AtoA extends PGraph<A> {
    {
      type(A.class);
      pre(a->a.value==0);
      begin();
      step(BtoB.class);
      step(CtoC.class);
      end();
      post(a->a.value==1);
    }
  }