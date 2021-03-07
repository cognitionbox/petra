package io.cognitionbox.petra.examples.simple.common;

import java.io.Serializable;

public class A implements Serializable {
    public Integer value = 0;

    private Integer captured = null;

    public Integer getCaptured() {
        return captured;
    }

    public void setCaptured(Integer captured) {
        this.captured = captured;
    }

    @Override
    public String toString() {
        return "A{" +
                "value=" + value +
                '}';
    }
}