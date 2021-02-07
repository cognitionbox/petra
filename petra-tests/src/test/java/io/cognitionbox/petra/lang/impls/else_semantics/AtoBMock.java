package io.cognitionbox.petra.lang.impls.else_semantics;

import io.cognitionbox.petra.lang.PGraph;

public class AtoBMock extends PGraph<X> {
    {
        type(X.class);
        pre(x->x.value1.value =="A" && x.value2.value =="A");
        mock(x->x.value1.value =="A" && x.value2.value =="A", x->{
            x.value1.value ="B";
            x.value2.value ="B";
            x.values.forEach(v->{
                v.value = "B";
            });
        });
        elseMock(x->{});
        post(x->x.values.stream().allMatch(y->y.value=="B"));
    }
}
