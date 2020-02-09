package io.cognitionbox.petra.examples.simple.helloworld;

import java.io.Serializable;

class A implements Serializable {
    @Override
    public String toString() {
        return "A{" +
                "value='" + value + '\'' +
                '}';
    }

    public A(String value) {
        this.value = value;
    }

    String value;
}