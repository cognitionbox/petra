/**
 * Copyright 2016-2020 Aran Hakki
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.util.function.ICallable;
import io.cognitionbox.petra.util.function.IPredicate;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractStep<X> extends Identifyable implements ICallable<X>, IStep<X> {

    private Class<? extends IStep> clazz = this.getClass();
    private IToken<X> inputToken = null;

    private boolean elseStep = false;
    private boolean initStep = false;
    private boolean inited = false;

    protected Guard<X> p = null;
    protected Guard<X> q = null;

    private RGraph<?,?> parent;

    public RGraph<?,?> getParent() {
        return parent;
    }

    void setParent(RGraph<?,?> parent) {
        this.parent = parent;
    }

    protected AbstractStep(String description) {
        super(description);
    }

    protected AbstractStep(Guard<X> p, Guard<X> q) {
        this.p = p;
        this.q = q;
    }

    public abstract AbstractStep copy();

    public IToken<X> getInput() {
        return inputToken;
    }

    public void setInput(IToken<X> input) {
        this.inputToken = input;
    }

    public Class<? extends IStep> getStepClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends IStep> aClass) {
        this.clazz = aClass;
    }

    protected void setP(Guard<X> p) {
        assertNotNull(p);
        assertNull(this.p());
        this.p = p;
    }
    protected void setQ(Guard<X> q) {
        assertNotNull(q);
        this.q = q;
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }

    final protected void assertNotNull(Object o) {
        if (o == null) {
            throw new UnsupportedOperationException("cannot be null");
        }
    }

    final protected void assertNull(Object o) {
        if (o != null) {
            throw new UnsupportedOperationException("Value Already Set Once.");
        }
    }

    @Override
    public boolean evalP(X e) {
        if (p == null)
            return false;
        return p.test(e);
    }

    @Override
    final public boolean evalQ(X output) {
        if (q == null)
            return false;
        return q.test(output);
    }

    @Override
    public Guard<X> p() {
        return p;
    }

    @Override
    public Guard<X> q() {
        return q;
    }

    public boolean isElseStep() {
        return elseStep;
    }

    public void setElseStep(boolean elseStep) {
        this.elseStep = elseStep;
    }

    public boolean isInitStep() {
        return initStep;
    }

    public void setInitStep(boolean initStep) {
        this.initStep = initStep;
    }

    public boolean isInited() {
        return inited;
    }

    public void setInited(boolean inited) {
        this.inited = inited;
    }

    Kase<X> activeKase = null;

//    boolean setActiveKase(X value) {
//        for (int i=kases.size()-1;i>=0;i--){
//            Kase k = kases.get(i);
//            if (k.evalP(value)){
//                activeKase = k;
//                break;
//            }
//        }
//        return true;
//    }

    private Kase lastActivatedKase;
    private Set<Kase> activatedKases = new HashSet<>();
    void resetActiveKase() {
        activeKase = null;
    }
    boolean setActiveKase(X value) {
        activeKase = null;
        for (Kase k : kases){
            if (k.evalP(value)){
                if (activatedKases.size()==kases.size()){
                    activatedKases.clear();
                }
//                if (k!=lastActivatedKase && activatedKases.contains(k)){
//                    throw new IllegalStateException("active kase not changed!");
//                }
                activeKase = k;
                activatedKases.add(activeKase);
                lastActivatedKase = k;
                break;
            }
        }
        if (activeKase==null){
            activeKase = new Kase<X>(this,type,x->false,x->false);
        }
        return true;
    }

    public Kase<X> getActiveKase() {
        return activeKase;
    }

    final AtomicInteger kaseId = new AtomicInteger();

    protected List<Kase<X>> kases = new ArrayList<>();

    public List<Kase<X>> getKases(){
        return new ArrayList<>(kases);
    }

    public void kase(IPredicate<X> pre, IPredicate<X> post) {
        kases.add(new Kase<X>(this,type,pre,post));
    }

    public void kase(IPredicate<X> pre, IPredicate<X> post, Cover cover, Ignore... ignores) {
        kases.add(new Kase<X>(this,type,pre,post,cover,ignores));
    }

    public void kase(IPredicate<X> pre, IPredicate<X> post, Ignore... ignores) {
        kases.add(new Kase<X>(this,type,pre,post,new Cover(),ignores));
    }


    public void defaultKase(IPredicate<X> pre, IPredicate<X> post) {
        kases.add(new Kase<X>(this,type,pre,post,true,new Cover(),new Ignore[]{}));
    }

    public Set<Pair<Class<? extends IStep>,Integer>> getIgnoredKases() {
        return ignoredKases;
    }

    private Set<Pair<Class<? extends IStep>,Integer>> ignoredKases = new HashSet<>();
    public void ignoreKase(int kase){
        this.ignoredKases.add(Pair.with(this.getStepClazz(),kase));
    }

    void setKases(List<Kase<X>> kases){
        this.kases = kases;
    }

    void pre(IPredicate<X> predicate) {
        setP(new Guard(type, predicate));
    }
    protected boolean endAsBeenCalled = false;
    void post(IPredicate<X> predicate) {
        if (!endAsBeenCalled) {
            throw new UnsupportedOperationException("end has not been called");
        }
        setQ(new Guard(type,predicate));
        kases.add(new Kase<X>(this,type,p,q));
    }

    public Class<X> getType() {
        return type;
    }

    Class<X> type = null;

    public void type(Class<X> type) {
        this.type = type;
    }
}
