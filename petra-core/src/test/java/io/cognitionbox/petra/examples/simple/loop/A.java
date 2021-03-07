package io.cognitionbox.petra.examples.simple.loop;

import java.io.Serializable;

class A implements Serializable {
    Integer value = 0;

    private Integer captured = null;

    public Integer getCaptured() {
        return captured;
    }

    public void setCaptured(Integer captured) {
        this.captured = captured;
    }
}