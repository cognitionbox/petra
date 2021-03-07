package io.cognitionbox.petra.examples.simple2.d_parallel_sequences1;

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

    private State state;

    public State state() {
        return this.state;
    }

    public void state(State state) {
        this.state = state;
    }

    public boolean isA() {
        return state == State.A;
    }

    public boolean isB() {
        return state == State.B;
    }

    public boolean isC() {
        return state == State.C;
    }

    public boolean isABC() {
        return isA() ^ isB() ^ isC();
    }
}
