package io.cognitionbox.petra.lang.impls.else_semantics.withelse;

import io.cognitionbox.petra.lang.PEdge;

public class AtoA2 extends PEdge<A> {
    {
      type(A.class);
      pre(a->a.value==2);
      func(a->{
        a.value++;
      });
      post(a->a.value==3);
    }
  }