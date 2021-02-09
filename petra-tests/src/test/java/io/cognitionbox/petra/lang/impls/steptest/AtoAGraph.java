package io.cognitionbox.petra.lang.impls.steptest;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.par;
import static io.cognitionbox.petra.util.Petra.seq;

public class AtoAGraph extends PGraph<A> {
    {
      type(A.class);
      pre(a->a.value==1);
      post(a->a.value==222);
      step(par(),x->x,AtoAEdge.class);
    }
  }