package io.cognitionbox.petra.examples.simple2.d_parallel_sequences1;

import io.cognitionbox.petra.lang.annotations.Extract;

import java.io.Serializable;

@Extract
class X implements Serializable {
    private Y y1;
    private Y y2;
    @Extract public Y y1() { return y1; }
    @Extract public Y y2() { return y2; }

    public X(State state) {
        y1 = new Y(state);
        y2 = new Y(state);
    }
}