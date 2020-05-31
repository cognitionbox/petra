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

    protected GuardXOR<X> q = null;

    protected void setP(Guard<X> p) {
        assertNotNull(p);
        assertNull(this.p());
        this.p = p;
    }

    protected void setQ(GuardXOR<X> q) {
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
    final public boolean evalP(X e) {
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
    public GuardXOR<X> q() {
        return q;
    }

    public abstract AbstractStep copy();

    private boolean sleepOk = true;
    private boolean failureDetected = false;
    private int milliSecondSretryDelay = 0;
    public AbstractStep<X> retryDelay(int milliSeconds){
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

    final GuardXOR<X> returnType = new GuardXOR<X>(OperationType.RETURN);

    public void pre(GuardInput<X> p) {
        setP(p);
    }

    public void pre(Class<X> p, IPredicate<X> predicate) {
        setP(new GuardWrite(p, predicate));
    }

    public void post(GuardReturn<X> q) {
        returnType.addChoice(new Guard(q.getTypeClass(),q.predicate,OperationType.RETURN));
        setQ(returnType);
    }

    public void post(Class<X> p, IPredicate<X> predicate) {
        returnType.addChoice(new Guard(p,predicate,OperationType.RETURN));
        setQ(returnType);
    }

    public void postVoid() {
        post(new GuardReturn(Void.class, x->true));
    }

}
