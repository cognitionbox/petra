/**
 * Copyright (C) 2016-2020 Aran Hakki.
 *
 * This file is part of Petra.
 *
 * Petra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Petra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Petra.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.core.impl.*;
import io.cognitionbox.petra.google.Optional;
import io.cognitionbox.petra.util.function.ICallable;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.core.engine.petri.IToken;

import java.io.IOException;

public abstract class AbstractStep<I, O> extends Identifyable implements ICallable<O>, IStep<I, O> {

    public IToken<I> getInput() {
        return inputToken;
    }

    private IToken<I> inputToken;

    public void setInput(IToken<I> input) {
        this.inputToken = input;
    }

    private Class<? extends IStep> clazz = this.getClass();

    public Class<? extends IStep> getStepClazz() {
        return clazz;
    }

    public void setClazz(Class<? extends IStep> aClass) {
        this.clazz = aClass;
    }

    protected Guard<I> p = null;

    protected GuardXOR<O> q = null;

    protected void setP(Guard<I> p) {
        assertNotNull(p);
        assertNull(this.p());
        this.p = p;
    }

    protected void setQ(GuardXOR<O> q) {
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
    final public boolean evalP(I e) {
        if (p == null)
            return false;
        return p.test(e);
    }

    @Override
    final public boolean evalQ(O output) {
        if (q == null)
            return false;
        return q.test(output);
    }

    @Override
    public Guard<I> p() {
        return p;
    }

    @Override
    public GuardXOR<O> q() {
        return q;
    }

    public abstract AbstractStep copy();

    private boolean sleepOk = true;
    private boolean failureDetected = false;
    private int milliSecondSretryDelay = 0;
    public AbstractStep<I, O> retryDelay(int milliSeconds){
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
            if (p().getOperationType()== OperationType.WRITE){
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

    final GuardXOR<O> returnType =  new GuardXOR<O>(OperationType.RETURN);

    public void pre(GuardInput<? super I> p) {
        setP((GuardInput<I>) p);
    }

    public void post(GuardReturn<? super O> q) {
        returnType.addChoice(new Guard(q.getTypeClass(),q.predicate,OperationType.RETURN));
        setQ(returnType);
    }

    public void postVoid() {
        post(new GuardReturn(Void.class, x->true));
    }

}
