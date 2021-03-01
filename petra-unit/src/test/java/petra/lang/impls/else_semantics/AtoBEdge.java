package petra.lang.impls.else_semantics;

import io.cognitionbox.petra.lang.PEdge;

public class AtoBEdge extends PEdge<Value> {
    {
        type(Value.class);
        pre(x->x.value =="A");
        func(x->{
            x.value = "B";
        });
        post(x->x.value =="B");
    }
}
