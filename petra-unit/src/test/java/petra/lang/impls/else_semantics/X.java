package petra.lang.impls.else_semantics;

import java.util.List;

public class X {
    Value value1 = null;
    Value value2 = null;
    List<Value> values;

    public X(Value value1, Value value2, List<Value> values) {
        this.value1 = value1;
        this.value2 = value2;
        this.values = values;
    }
}
