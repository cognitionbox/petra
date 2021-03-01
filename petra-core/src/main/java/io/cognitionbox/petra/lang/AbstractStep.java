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
import io.cognitionbox.petra.core.impl.OperationType;
import io.cognitionbox.petra.core.impl.ReflectUtils;
import io.cognitionbox.petra.google.Optional;
import io.cognitionbox.petra.util.function.ICallable;

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

    protected Guard<X> q = null;

    protected void setP(Guard<X> p) {
        assertNotNull(p);
        assertNull(this.p());
        this.p = p;
    }

    protected void setQ(Guard<X> q) {
        assertNotNull(q);
        //assertNull(this.q());
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

    public abstract AbstractStep copy();

    private boolean sleepOk = true;
    private boolean failureDetected = false;
    private int milliSecondSretryDelay = 0;

    public AbstractStep<X> retryDelay(int milliSeconds) {
        this.milliSecondSretryDelay = milliSeconds;
        return this;
    }

    private Optional<Class<?>> effectType = null;

    public void setEffectType(Optional<Class<?>> effectType) {
        this.effectType = effectType;
    }

    @Override
    public final Optional<Class<?>> getEffectType() {
        if (effectType == null) {
            effectType = Optional.absent();
            if (p().getOperationType() == OperationType.READ_WRITE) {
                java.util.Optional<Class<?>> optional = ReflectUtils.getCommonSubType(p().getTypeClass(), q().getTypeClass());
                //Optional optional = Optional.absent();
                if (optional.isPresent()) {
                    effectType = Optional.of(optional.get()); // Optional.absent();//
                }
            }

        }
        return effectType;
    }

    final GuardXOR<X> returnType = new GuardXOR<X>(OperationType.RETURN);

    public Class<X> getType() {
        return type;
    }

    Class<X> type = null;

    public void type(Class<X> type) {
        this.type = type;
    }

    public boolean isElseStep() {
        return isElseStep;
    }

    boolean isElseStep = false;

    boolean isInitStep = false;

    boolean isInited = false;

    public boolean isInitStep() {
        return isInitStep;
    }

    public boolean isInited() {
        return isInited;
    }
}
