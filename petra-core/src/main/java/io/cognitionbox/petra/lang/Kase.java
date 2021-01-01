package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.util.Petra;
import io.cognitionbox.petra.util.function.IPredicate;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.cognitionbox.petra.util.Petra.rw;

public class Kase<E> implements IKase<E> {
    private final AtomicBoolean covered = new AtomicBoolean(false);
    public final void markCovered(){
        covered.set(true);
    }
    public final boolean isCovered(){
        return covered.get();
    }
    private final Guard p, q;

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

//    public boolean isDefaultKase() {
//        return defaultKase.get();
//    }

    private final AtomicBoolean defaultKase = new AtomicBoolean(false);
    public Kase(AbstractStep step, Class<E> eventClazz, IPredicate<E> pre, IPredicate<E> post, boolean defaultKase) {
        this(step,eventClazz,pre,post);
        this.defaultKase.set(defaultKase);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kase<?> kase = (Kase<?>) o;
        return id == kase.id &&
                step.getStepClazz().equals(kase.step.getStepClazz());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, step.getStepClazz());
    }

    @Override
    public String toString() {
        return "Kase{" +
                "covered=" + covered +
                ", id=" + id +
                ", step=" + step.getStepClazz().getCanonicalName() +
                '}';
    }
}
