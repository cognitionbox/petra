package io.cognitionbox.petra.lang.impls.generics;

import io.cognitionbox.petra.lang.PEdge;

public class BtoB extends PEdge<B> {
    {
      type(B.class);
      pre(a->a.value==0);
      func(a->{
        a.value++;
      });
      post(a->a.value==1);
    }
  }