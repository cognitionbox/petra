package io.cognitionbox.petra.lang.impls.steptest;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.par;

public class AtoA extends PGraph<A> {
    {
      type(A.class);
      pre(a->a.value==1);
      step(par(),x->x, AtoAEdge.class);
      end();
      post(a->a.value==222);
    }
  }