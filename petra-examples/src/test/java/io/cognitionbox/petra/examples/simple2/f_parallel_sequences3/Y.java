package io.cognitionbox.petra.examples.simple2.f_parallel_sequences3;

public class Y {
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

    public boolean isA(){ return state== State.A; }
    public boolean isB(){ return state== State.B; }
    public boolean isC(){ return state== State.C; }
    public boolean isAB(){ return isA() ^ isB();}
}
