package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.core.impl.OperationType;
import io.cognitionbox.petra.util.function.IPredicate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class Kase<E> implements IKase<E> {
    private final AtomicBoolean covered = new AtomicBoolean(false);
    public final void markCovered(){
        covered.set(true);
    }
    public final boolean isCovered(){
        return covered.get();
    }
    private final Guard p, q;

    Cover getCover() {
        return cover;
    }

    List<Ignore> getIgnores() {
        return ignores;
    }

    private final Cover cover;
    private final List<Ignore> ignores;

    public int getId() {
        return id;
    }

    private final int id;

    public AbstractStep getStep() {
        return step;
    }

    //private IBiPredicate<E,E> qBi;
    private AbstractStep step;
    public Kase(AbstractStep step, Class<E> eventClazz, IPredicate<E> pre, IPredicate<E> post, Cover cover, Ignore[] ignores) {
        this.step = step;
        this.p = new Guard(eventClazz, pre, OperationType.READ_WRITE);
        this.q = new Guard(eventClazz, post, OperationType.READ_WRITE);
        this.id = step.kaseId.getAndIncrement();
        this.cover = cover;
        this.ignores = Arrays.asList(ignores);
    }

    public Kase(AbstractStep step, Class<E> eventClazz, IPredicate<E> pre, IPredicate<E> post) {
        this(step,eventClazz,pre,post,new Cover(),new Ignore[]{});
    }

    public boolean isDefault() {
        return defaultKase.get();
    }

    private final AtomicBoolean defaultKase = new AtomicBoolean(false);
    public Kase(AbstractStep step, Class<E> eventClazz, IPredicate<E> pre, IPredicate<E> post, boolean defaultKase, Cover cover, Ignore[] ignores) {
        this(step,eventClazz,pre,post,cover,ignores);
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
