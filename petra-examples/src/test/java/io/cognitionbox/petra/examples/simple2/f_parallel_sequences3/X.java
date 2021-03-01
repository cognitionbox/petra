package io.cognitionbox.petra.examples.simple2.f_parallel_sequences3;

import io.cognitionbox.petra.lang.annotations.Extract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Extract
class X implements Serializable {
    private List<Y> ys;
    @Extract public List<Y> ys() { return ys; }

    public X(State state) {
        ys = new ArrayList<>();
        ys.add(new Y(state));
        ys.add(new Y(state));
        ys.add(new Y(state));
    }
}