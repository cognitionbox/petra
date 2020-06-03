package io.cognitionbox.petra.examples.simple2.a_flagswitch;

import java.io.Serializable;

class X implements Serializable {
    @Override
    public String toString() {
        return "X{" +
                "value='" + value + '\'' +
                '}';
    }

    public X(Boolean value) {
        this.value = value;
    }

    public Boolean value() {
        return value;
    }

    public void value(Boolean value) {
        this.value = value;
    }

    private Boolean value;

    public boolean isTrue(){
       return value==true;
    }
    public boolean isFalse(){
        return value==false;
    }
    public boolean isTrueOrFalse(){
        return isTrue() ^ isFalse();
    }
}