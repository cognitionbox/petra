/**
 * Copyright 2016-2020 Aran Hakki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.core.impl.*;
import io.cognitionbox.petra.google.Optional;
import io.cognitionbox.petra.util.function.ICallable;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.util.function.IPredicate;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractStep<X> extends Identifyable implements ICallable<X>, IStep<X> {

    public IToken<X> getInput() {
        return inputToken;
    }

    private IToken<X> inputToken;

    public void setInput(IToken<X> input) {
        this.inputToken = input;
    }

    private Class<? extends IStep> clazz = this.getClass();

    public Class<? extends IStep> getStepClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends IStep> aClass) {
        this.clazz = aClass;
    }

    protected Guard<X> p = null;

    protected Guard<X> q = null;

    protected void setP(Guard<X> p) {
        assertNotNull(p);
        this.p = p;
    }

    protected void setQ(Guard<X> q) {
        assertNotNull(q);
        this.q = q;
    }

    protected AbstractStep() {
    }
    protected AbstractStep(String description) {
        super(description);
    }

//    public AbstractStep(String description, Guard<I> p, Guard<O> q) {
//        super(description);
//        this.p = p;
//        this.q = q;
//    }

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

    public abstract AbstractStep copy();

    private boolean sleepOk = true;
    private boolean failureDetected = false;
    private int milliSecondSretryDelay = 0;
    public AbstractStep<X> retryDelay(int milliSeconds){
        this.milliSecondSretryDelay = milliSeconds;
        return this;
    }

    @Override
    public final Optional<Class<?>> getEffectType() {
        return Optional.of(kases.get(0).p().getTypeClass());
    }

//    // think this needs to set the active kases, ie there can be multiple active kases,
//    // this will allow more specific kases to be added when extending
//    boolean setActiveKase(X value) {
//        activeKases.clear();
//        for (IKase k : kases){
//            if (k.evalP(value)){
//                activeKases.addKase(k);
//            }
//        }
//        return true;
//    }
//
//    public MultiKase<X> getActiveKase() {
//        return activeKases;
//    }

    private MultiKase<X> activeKases = new MultiKase<>();
    private Kase<X> activeKase = null;

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

    boolean setActiveKase(X value) {
        activeKase = null;
        for (Kase k : kases){
            if (k.evalP(value)){
                activeKase = k;
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

    private List<Kase<X>> kases = new ArrayList<>();

    public List<Kase<X>> getKases(){
        return new ArrayList<>(kases);
    }

    public void kase(IPredicate<X> pre, IPredicate<X> post) {
        kases.add(new Kase<X>(this,type,pre,post));
    }

//    public void defaultKase(IPredicate<X> pre, IPredicate<X> post) {
//        kases.add(new Kase<X>(this,type,pre,post,true));
//    }

    public Set<Pair<AbstractStep,Integer>> getIgnoredKases() {
        return ignoredKases;
    }

    private Set<Pair<AbstractStep,Integer>> ignoredKases = new HashSet<>();
    public void ignoreKase(int kase){
        this.ignoredKases.add(Pair.with(this,kase));
    }

    void setKases(List<Kase<X>> kases){
        this.kases = kases;
    }

    public void pre(IPredicate<X> predicate) {
        setP(new Guard(type, predicate));
    }

    public void post(IPredicate<X> predicate) {
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
