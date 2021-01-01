package io.cognitionbox.petra.examples.kases4.objects;

public class SystemState {
    public SystemState(State state) {
        this.state = state;
    }

    public enum State {
        A,B,C,D,E;
    }
    private State state;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
