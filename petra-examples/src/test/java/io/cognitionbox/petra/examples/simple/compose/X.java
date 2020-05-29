package io.cognitionbox.petra.examples.simple.compose;

public class X implements rwABC, rwF, rwE, rwCD{
    A a;
    B b;
    C c;
    D d;
    E e;
    F f;

    @Override
    public void a(A a) {

    }

    @Override
    public A a() {
        return null;
    }

    @Override
    public F f() {
        return null;
    }

    @Override
    public C c() {
        return null;
    }

    @Override
    public D d() {
        return null;
    }

    @Override
    public void c(C c) {

    }

    @Override
    public void d(D d) {

    }

    @Override
    public void b(B b) {

    }

    @Override
    public B b() {
        return null;
    }

    @Override
    public void printF() {

    }

    @Override
    public void e(E e) {

    }

    @Override
    public void printMe() {

    }

    @Override
    public E e() {
        return null;
    }
}
