package io.cognitionbox.petra.lang.impls.else_semantics.incompletewithoutelse;

import io.cognitionbox.petra.lang.PGraph;
import static io.cognitionbox.petra.util.Petra.seq;

public class AtoA extends PGraph<A> {
    {
      type(A.class);
      pre(a->a.value==1 ^ a.value==2);
      step(seq(),x->x, AtoA1.class);
      step(seq(),x->x, AtoA2.class);
        end();
      post(a->a.value==2 ^ a.value==3);
    }
  }