package io.cognitionbox.petra.examples.simple.compose;

import java.io.Serializable;

public class A implements Serializable, rwB {
    B b;

    @Override
    public B b() {
        return b;
    }

    @Override
    public void b(B b) {

    }
}