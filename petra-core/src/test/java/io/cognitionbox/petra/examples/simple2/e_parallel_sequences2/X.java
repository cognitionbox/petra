package io.cognitionbox.petra.examples.simple2.e_parallel_sequences2;

import java.io.Serializable;

class X implements Serializable {
    private Y y1;
    private Y y2;

    public X(State state) {
        y1 = new Y(state);
        y2 = new Y(state);
    }

    public Y y1() {
        return y1;
    }

    public Y y2() {
        return y2;
    }
}