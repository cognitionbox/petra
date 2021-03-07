package io.cognitionbox.petra.examples.simple2.f_parallel_sequences3;

import io.cognitionbox.petra.lang.Ref;

import java.io.Serializable;

import static io.cognitionbox.petra.util.Petra.ref;

public class Y implements Serializable {
    @Override
    public String toString() {
        return "Y{" +
                "state='" + state + '\'' +
                '}';
    }

    public Y(State state) {
        this.state.set(state);
    }

    private Ref<State> state = ref();

    public State state() {
        return this.state.get();
    }

    public void state(State state) {
        this.state.set(state);
    }

    public boolean isA() {
        return state.get() == State.A;
    }

    public boolean isB() {
        return state.get() == State.B;
    }

    public boolean isC() {
        return state.get() == State.C;
    }

    public boolean isAB() {
        return isA() ^ isB();
    }
}
