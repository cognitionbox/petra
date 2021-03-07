package io.cognitionbox.petra.examples.simple2.e_parallel_sequences2;

import java.io.Serializable;

public class Y implements Serializable {
    @Override
    public String toString() {
        return "Y{" +
                "state='" + state + '\'' +
                '}';
    }

    public Y(State state) {
        this.state = state;
    }

    public State state() {
        return state;
    }

    public void state(State state) {
        this.state = state;
    }

    private State state;

    public boolean isA() {
        return state == State.A;
    }

    public boolean isB() {
        return state == State.B;
    }

    public boolean isC() {
        return state == State.C;
    }

    public boolean isAB() {
        return isA() ^ isB();
    }
}
