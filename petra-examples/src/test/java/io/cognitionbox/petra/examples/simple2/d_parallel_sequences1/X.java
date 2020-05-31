package io.cognitionbox.petra.examples.simple2.d_parallel_sequences1;

import io.cognitionbox.petra.lang.annotations.Extract;

import java.io.Serializable;

@Extract
class X implements Serializable {
    Y y1;
    Y y2;
    @Extract public Y getY1() { return y1; }
    @Extract public Y getY2() { return y2; }

    public X(State state) {
        y1 = new Y(state);
        y2 = new Y(state);
    }
}