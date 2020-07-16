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
import io.cognitionbox.petra.lang.annotations.Exclusive;
import io.cognitionbox.petra.lang.annotations.Feedback;
import io.cognitionbox.petra.exceptions.EdgeException;
import io.cognitionbox.petra.exceptions.UnknownEdgeFailure;
import io.cognitionbox.petra.exceptions.conditions.PostConditionFailure;
import io.cognitionbox.petra.exceptions.conditions.PreConditionFailure;
import io.cognitionbox.petra.exceptions.sideeffects.IsNotSideEffectAndDidChangeInput;
import io.cognitionbox.petra.exceptions.sideeffects.IsSideEffectAndDidNotChangeInput;
import io.cognitionbox.petra.util.function.IBiPredicate;
import io.cognitionbox.petra.util.function.IFunction;
import io.cognitionbox.petra.util.function.IPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static io.cognitionbox.petra.lang.Void.vd;
import static io.cognitionbox.petra.util.Petra.throwRandomException;


public class PEdge<X> extends AbstractStep<X> implements Serializable {

    final static Logger LOG = LoggerFactory.getLogger(PEdge.class);
    protected boolean feedback = false;
    private IFunction<X, X> function;
    private io.cognitionbox.petra.core.impl.PEdgeRollbackHelper PEdgeRollbackHelper = new PEdgeRollbackHelper(100);
    private ObjectCopyerViaSerialization copyer = new ObjectCopyerViaSerialization();
    private List<Class<? extends Exception>> throwsRandomly = new ArrayList<>();
    private ObjectTrans objectTrans = new ObjectTrans();

    public PEdge() {
        this.feedback = this.getStepClazz().getAnnotationsByType(Feedback.class).length > 0;
    }

    public PEdge(String description, boolean isEffect) {
        super(description,isEffect);
    }

    public PEdge(Guard<X> p, IFunction<X, X> function, GuardXOR<X> q) {
        this.p = p;
        this.function = function;
        this.q = q;
    }

    protected Logger logger() {
        return LoggerFactory.getLogger(this.getStepClazz());
    }

    public IFunction<X, X> getFunction() {
        return function;
    }

    public PEdge<X> func(IFunction<X, X> function) {
        this.function = function;
        return this;
    }

    protected void setMillisBeforeRetry(long millisBeforeRetry) {
        this.PEdgeRollbackHelper = new PEdgeRollbackHelper(millisBeforeRetry);
    }

    private void Lg_ALL_STATES(String desc, X value) {
        if (RGraphComputer.getConfig().isAllStatesLoggingEnabled()) {
            LOG.info(desc + " " + RGraphComputer.getConfig().getMode() + " " + " " + this.getPartitionKey() + " " + " thread=" + Thread.currentThread().getId() + " -> " + value);
        }
    }

    @Override
    public X call() {
        X input = getInput().getValue();
        Lg_ALL_STATES("[Eg in]",input);
        if (!p().test(input)) {
            return (X) input;
        }
        Set<Class<?>> classesLockKey = null;
        try {
            if (RGraphComputer.getConfig().isTestMode() && (throwsRandomly != null && throwsRandomly.size() > 0)) {
                throwRandomException(throwsRandomly);
            }

            if (isEffect() && this.p().getTypeClass().isAnnotationPresent(Exclusive.class)) {
                classesLockKey =
                        ReflectUtils.getAllMethodsAccessibleFromObject(this.p().getTypeClass())
                                .stream()
                                .filter(m -> m.isAnnotationPresent(Exclusive.class) &&
                                        m.getReturnType().isAnnotationPresent(Exclusive.class) &&
                                        m.getParameterCount() == 0 &&
                                        Modifier.isPublic(m.getModifiers()))
                                .map(m -> m.getReturnType())
                                .collect(Collectors.toSet());
                if (classesLockKey != null && !classesLockKey.isEmpty() && getEffectType().isPresent() && Exclusives.tryAquireExclusive(classesLockKey)) {
                    Exclusives.load(input, getEffectType().get());
                } else {
                    return (X) input;
                }
            }

            PEdgeRollbackHelper.capture(input, this);
            boolean inputMatchesPostConditionBeforeRunning = q().test(input);
            X res = null;
            if (RGraphComputer.getConfig().isDeadLockRecoveryActive()) {
                final ExecutorService executor = Executors.newSingleThreadExecutor();
                final Future<X> future = executor.submit(() -> {
                    synchronized (this) { // memory-barrier to ensure all updates are visible before and after func is applied.
                        if (!isEffect() && RGraphComputer.getConfig().isDefensiveCopyAllInputsExceptForEffectedInputs()) {
                            return (X) function.apply(copyer.copy(input));
                        } else {
                            return (X) function.apply(input);
                        }
                    }
                });
                executor.shutdown(); // This does not cancel the already-scheduled task.

                res = future.get(1000, TimeUnit.MILLISECONDS);
            } else {
                synchronized (this) { // memory-barrier to ensure all updates are visible before and after func is applied.
                    if (!isEffect() && RGraphComputer.getConfig().isDefensiveCopyAllInputsExceptForEffectedInputs()) {
                        res = (X) function.apply(copyer.copy(input));
                    } else {
                        res = (X) function.apply(input);
                    }
                }
            }
            if (res == null) {
                return (X) vd;
            } else {
                boolean postConditionOk = (q().test(res) && isDeltaOk(res)) || (isFeedback() && p().test(res));
                boolean isNotSideEffectAndDidChangeInput = !isEffect() && !p().test(input);

                // after successfully completes check side effect, allows for looping on same inputs
                boolean isSideEffectAndDidNotChangeInput = isEffect() && p().test(input) && !isFeedback();

                boolean sideEffectOk = !(isSideEffectAndDidNotChangeInput || isNotSideEffectAndDidChangeInput);
                if (!inputMatchesPostConditionBeforeRunning && postConditionOk && sideEffectOk) {
                    Lg_ALL_STATES("[Eg out]",input);
                    return res;
                } else if (RGraphComputer.getConfig().isExceptionsPassthrough()) {

                    if (inputMatchesPostConditionBeforeRunning) {
                        return (X) new EdgeException(input, res, new PreConditionFailure());
                    }

                    if (isNotSideEffectAndDidChangeInput) {
                        return (X) new EdgeException(input, res, new IsNotSideEffectAndDidChangeInput());
                    }

                    if (isSideEffectAndDidNotChangeInput) {
                        return (X) new EdgeException(input, res, new IsSideEffectAndDidNotChangeInput());
                    }

                    if (!postConditionOk) {
                        return (X) new EdgeException(input, res, new PostConditionFailure());
                    }

                    return (X) new EdgeException(input, res, new UnknownEdgeFailure());

                } else {
                    PEdgeRollbackHelper.rollback(input, this);
                    return (X) input;
                }
            }
        } catch (Exclusives.ExclusivesLoadException e) {
            LOG.error(this.getUniqueId(), e);
            if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                // required for Exhaust Petra to highlight errors
                return (X) new EdgeException(input, null, e);
            } else {
                // no rollback as load happens before input is transformed
                return (X) input;
            }
        } catch (Exception e) {
            LOG.error(this.getUniqueId(), e);
            if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                // required for Exhaust Petra to highlight errors
                return (X) new EdgeException(input, null, e);
            } else {
                // swallow exception in production so retry will be triggered

                PEdgeRollbackHelper.rollback(input, this);
                return (X) input;
            }
        } finally {
            if (classesLockKey != null && !classesLockKey.isEmpty() && this.getEffectType().isPresent()) {// && this.p().getTypeClass().isAnnotationPresent(Exclusive.class)){
                Exclusives.returnExclusive(classesLockKey);
            }
        }
    }

    private boolean isFeedback() {
        return feedback;
    }

    public PEdge copy() {
        // we dont copy the id as we need a unique id based on the hashcode of the new instance
        PEdge PEdge = new PEdge(getPartitionKey(), isEffect());
        PEdge.setEffectType(this.getEffectType()); // so we dont have to re-compute
        PEdge.setClazz(getStepClazz());
        PEdge.setP(p());
        PEdge.func(function);
        PEdge.setQ(q());
        PEdge.feedback = this.feedback;
        return PEdge;
    }

    public PEdge<X> throwsRandom(Class<? extends Exception> clazz) {
        throwsRandomly.add(clazz);
        return this;
    }

    @Override
    public boolean isDoesNotTerminate() {
        return false;
    }

    public void pre(GuardInput<X> p) {
        setP(p);
    }

    public void pre(IPredicate<X> predicate) {
        setP(new GuardWrite(type, predicate));
    }

    public void post(GuardReturn<X> q) {
        returnType.addChoice(new Guard(q.getTypeClass(),q.predicate,OperationType.RETURN));
        setQ(returnType);
    }

    public void post(IPredicate<X> predicate) {
        returnType.addChoice(new Guard(type,predicate,OperationType.RETURN));
        setQ(returnType);
    }

    private IBiPredicate<X,X> deltaPredicate = null;

    public void delta(IBiPredicate<X,X> predicate) {
        this.deltaPredicate = predicate;
    }

    private boolean isDeltaOk(X x){
        if (this.deltaPredicate==null){
            return true;
        } else {
            // need to plumb in old value using the object transaction restore
            return this.deltaPredicate.test(x,x);
        }
    }
}
