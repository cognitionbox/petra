package io.cognitionbox.petra.lang.impls.steptest;

import java.io.Serializable;

public class A implements Serializable {
    int value = 0;

    transient B b = new B();

    @Override
    public String toString() {
      return "A{" +
              "value=" + value +
              '}';
    }

    public A(int value) {
      this.value = value;
    }
  }