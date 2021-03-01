package petra.lang.impls.generics;

import io.cognitionbox.petra.lang.PGraph;

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