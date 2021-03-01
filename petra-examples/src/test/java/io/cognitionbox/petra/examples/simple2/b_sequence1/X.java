package io.cognitionbox.petra.examples.simple2.b_sequence1;

import java.io.Serializable;

class X implements Serializable {

    @Override
    public String toString() {
        return "X{" +
                "state='" + state + '\'' +
                '}';
    }

    public X(State state) {
        this.state = state;
    }

    private State state;

    public State state() {
        return state;
    }

    public void state(State state) {
        this.state = state;
    }

    public boolean isA(){ return state==State.A; }
    public boolean isB(){ return state==State.B; }
    public boolean isC(){ return state==State.C; }
    public boolean isAB(){ return isA() ^ isB();}


}