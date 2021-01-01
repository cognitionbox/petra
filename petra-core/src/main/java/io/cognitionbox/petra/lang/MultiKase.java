package io.cognitionbox.petra.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public final class MultiKase<E> implements IKase<E>  {
    private final AtomicBoolean covered = new AtomicBoolean(false);
    public final void markCovered(){
        covered.set(true);
    }

    @Override
    public boolean evalQ(E obj) {
        return q().test(obj);
    }

    public final boolean isCovered(){
        return covered.get();
    }

    private final List<IKase<E>> kases = new ArrayList<>();

    public void clear(){
        this.kases.clear();
        this.pre.clear();
        this.post.clear();
    }

    private final MultiGuard<E> pre = new MultiGuard<>();
    private final MultiGuard<E> post = new MultiGuard<>();

    public void addKase(IKase<E> kase){
        this.kases.add(kase);
        this.pre.addGuard(kase.p());
        this.post.addGuard(kase.q());
    }

    @Override
    public Guard<E> p() {
        return pre;
    }

    @Override
    public Guard<E> q() {
        return post;
    }

    @Override
    public boolean evalP(E value) {
        return p().test(value);
        //return kases.stream().allMatch(k->k.evalP(value));
    }

    @Override
    public boolean q(E value) {
//        return q().test(value);
//        //return kases.stream().allMatch(k->k.q(value));

        boolean v = q().test(value);
        if (v){
            markCovered();
            kases.forEach(k->k.markCovered());
        }
        return v;
    }
}
