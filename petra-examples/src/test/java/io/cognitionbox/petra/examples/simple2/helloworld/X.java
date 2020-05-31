package io.cognitionbox.petra.examples.simple2.helloworld;

import java.io.Serializable;

class X implements Serializable {
    @Override
    public String toString() {
        return "X{" +
                "value='" + value + '\'' +
                '}';
    }

    public X(String value) {
        this.value = value;
    }

    String value;

    public boolean isString(){
        return this.value.equals("");
    }

    public boolean isBlankOrHelloWorld(){
        return isBlank() ^ isHelloWorld();
    }

    public boolean isBlank(){
       return this.value.equals("");
    }

    public boolean isHelloWorld(){
        return this.value.equals("hello world.");
    }
}