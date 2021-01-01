package io.cognitionbox.petra.lang.impls;

import io.cognitionbox.petra.lang.PGraph;

public class AtoA extends PGraph<A> {
    {
      type(A.class);
      // no more default kases, kases are must always be explicitly defined by the dev
        // next thing to do is removal of the side affects by collecting covered kases from new root graph instances
      kase(a->!a.atomicIntegers.isEmpty(),a->!a.atomicIntegers.isEmpty());
      kase(a->a.atomicIntegers.isEmpty(),a->a.atomicIntegers.isEmpty());
      stepForall(x->x.atomicIntegers,new Inc());
    }
  }