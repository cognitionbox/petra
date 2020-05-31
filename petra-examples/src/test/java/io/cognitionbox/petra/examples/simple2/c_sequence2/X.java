package io.cognitionbox.petra.examples.simple2.c_sequence2;

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

    State state;

    public boolean isA(){ return state== State.A; }
    public boolean isB(){ return state== State.B; }
    public boolean isC(){ return state== State.C; }
    public boolean isABC(){ return isA() ^ isB() ^ isC();}


}