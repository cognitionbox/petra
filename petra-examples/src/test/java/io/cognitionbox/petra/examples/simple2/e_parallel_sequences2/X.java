package io.cognitionbox.petra.examples.simple2.e_parallel_sequences2;

import io.cognitionbox.petra.lang.annotations.Extract;

import java.io.Serializable;

class X implements Serializable, Yone, Ytwo{
    Y y1;
    Y y2;

    public X(State state) {
        y1 = new Y(state);
        y2 = new Y(state);
    }

    @Override
    public Y y1() {
        return y1;
    }

    @Override
    public Y y2() {
        return y2;
    }
}