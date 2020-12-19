package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.util.function.IPredicate;

public final class Kase<E> {
    private Guard p, q;
    public Kase(Class<E> eventClazz, IPredicate<E> pre, IPredicate<E> post) {
        this.p = new Guard(eventClazz, pre);
        this.q = new Guard(eventClazz, post);
    }

    public boolean evalP(E e) {
        if (p == null)
            return false;
        return p.test(e);
    }

    final public boolean evalQ(E output) {
        if (q == null)
            return false;
        return q.test(output);
    }

    public Guard<E> p() {
        return p;
    }

    public Guard<E> q() {
        return q;
    }
}
