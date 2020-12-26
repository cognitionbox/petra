package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.util.function.IBiPredicate;
import io.cognitionbox.petra.util.function.IPredicate;

import java.util.concurrent.atomic.AtomicInteger;

import static io.cognitionbox.petra.util.Petra.ref;

public final class Kase<E> {
    private Ref<Boolean> covered = ref(false);
    public void markCovered(){
        covered.set(true);
    }
    public boolean isCovered(){
        return covered.get();
    }
    private Guard p, q;

    public int getId() {
        return id;
    }

    private final int id;

    public AbstractStep getStep() {
        return step;
    }

    //private IBiPredicate<E,E> qBi;
    private AbstractStep step;
    public Kase(AbstractStep step, Class<E> eventClazz, IPredicate<E> pre, IPredicate<E> post) {
        this.step = step;
        this.p = new Guard(eventClazz, pre);
        this.q = new Guard(eventClazz, post);
        this.id = step.kaseId.getAndIncrement();
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

    public boolean q(E value) {
        boolean v = q.test(value);
        if (v){
            markCovered();
        }
        return v;
    }
}
