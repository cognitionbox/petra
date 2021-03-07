package io.cognitionbox.petra.examples.simple2.f_parallel_sequences3;

import io.cognitionbox.petra.util.impl.PList;

import java.io.Serializable;
import java.util.List;

class X implements Serializable {
    private List<Y> ys;

    public List<Y> ys() {
        return ys;
    }

    public X(State state) {
        ys = new PList<>();
        ys.add(new Y(state));
        ys.add(new Y(state));
        ys.add(new Y(state));
    }
}