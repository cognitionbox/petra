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

import java.io.IOException;

public abstract class AbstractStep<R,W extends R> extends Identifyable implements ICallable<W>, IStep<R> {

    public IToken<W> getInput() {
        return inputToken;
    }

    private IToken<W> inputToken;

    public void setInput(IToken<W> input) {
        this.inputToken = input;
    }

    private Class<? extends IStep> clazz = this.getClass();

    public Class<? extends IStep> getStepClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends IStep> aClass) {
        this.clazz = aClass;
    }

    protected Guard<R> p = null;

    protected GuardXOR<R> q = null;

    protected void setP(Guard<R> p) {
        assertNotNull(p);
        assertNull(this.p());
        this.p = p;
    }

    protected void setQ(GuardXOR<R> q) {
        assertNotNull(q);
        //assertNull(this.q());
        this.q = q;
    }

    protected AbstractStep() {
    }
    protected AbstractStep(String description, boolean isEffect) {
        super(description);
        this.isEffect = isEffect;
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

    @Override
    final public boolean evalP(R e) {
        if (p == null)
            return false;
        return p.test(e);
    }

    @Override
    final public boolean evalQ(R output) {
        if (q == null)
            return false;
        return q.test(output);
    }

    @Override
    public Guard<R> p() {
        return p;
    }

    @Override
    public GuardXOR<R> q() {
        return q;
    }

    public abstract AbstractStep copy();

    private boolean sleepOk = true;
    private boolean failureDetected = false;
    private int milliSecondSretryDelay = 0;
    public AbstractStep<R,W> retryDelay(int milliSeconds){
        this.milliSecondSretryDelay = milliSeconds;
        return this;
    }

    private Optional<Class<?>> effectType = null;
    public void setEffectType(Optional<Class<?>> effectType) {
        this.effectType = effectType;
    }

    @Override
    public final Optional<Class<?>> getEffectType() {
        if (effectType==null){
            effectType = Optional.absent();
            if (p().getOperationType()== OperationType.READ_WRITE){
                java.util.Optional<Class<?>> optional = ReflectUtils.getCommonSubType(p().getTypeClass(),q().getTypeClass());
                //Optional optional = Optional.absent();
                if (optional.isPresent()){
                    effectType = Optional.of(optional.get()); // Optional.absent();//
                }
            }

        }
        return effectType;
    }

    public boolean isEffect() {
        return isEffect || getEffectType().isPresent();
    }

    private boolean isEffect = false;

    final GuardXOR<R> returnType =  new GuardXOR<R>(OperationType.RETURN);

    public void pre(GuardInput<R> p) {
        setP((GuardInput<R>) p);
    }

    public void pre(Class<R> p, IPredicate<R> predicate) {
        setP(new GuardWrite(p, predicate));
    }

    public void post(GuardReturn<? extends R> q) {
        returnType.addChoice(new Guard(q.getTypeClass(),q.predicate,OperationType.RETURN));
        setQ(returnType);
    }

    public <Q extends R> void post(Class<Q> p, IPredicate<Q> predicate) {
        returnType.addChoice(new Guard(p,predicate,OperationType.RETURN));
        setQ(returnType);
    }

    public void postVoid() {
        post(new GuardReturn(Void.class, x->true));
    }

}
