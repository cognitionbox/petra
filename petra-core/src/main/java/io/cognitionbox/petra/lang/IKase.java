package io.cognitionbox.petra.lang;

import java.io.Serializable;

public interface IKase<E> extends Serializable {
    Guard<E> p();
    Guard<E> q();
    boolean evalP(E value);
    boolean q(E value);

    void markCovered();

    boolean evalQ(E obj);
}
