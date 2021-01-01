package io.cognitionbox.petra.lang;

public interface IKase<E> {
    Guard<E> p();
    Guard<E> q();
    boolean evalP(E value);
    boolean q(E value);

    void markCovered();

    boolean evalQ(E obj);
}
