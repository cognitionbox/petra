package io.cognitionbox.petra.lang.impls;

import io.cognitionbox.petra.lang.PEdge;

import java.util.concurrent.atomic.AtomicInteger;

public class Inc extends PEdge<AtomicInteger> {
    {
      type(AtomicInteger.class);
      kase(a->a.get()==0,a->a.get()==1);
      func(a->{
        a.incrementAndGet();
      });
    }
  }