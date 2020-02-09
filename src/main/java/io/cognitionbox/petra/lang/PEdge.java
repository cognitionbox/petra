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
import io.cognitionbox.petra.lang.annotations.Exclusive;
import io.cognitionbox.petra.lang.annotations.Feedback;
import io.cognitionbox.petra.exceptions.EdgeException;
import io.cognitionbox.petra.exceptions.UnknownEdgeFailure;
import io.cognitionbox.petra.exceptions.conditions.PostConditionFailure;
import io.cognitionbox.petra.exceptions.conditions.PreConditionFailure;
import io.cognitionbox.petra.exceptions.sideeffects.IsNotSideEffectAndDidChangeInput;
import io.cognitionbox.petra.exceptions.sideeffects.IsSideEffectAndDidNotChangeInput;
import io.cognitionbox.petra.util.function.IFunction;
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


public class PEdge<I, O> extends AbstractStep<I, O> implements Serializable {

    final static Logger LOG = LoggerFactory.getLogger(PEdge.class);
    protected boolean feedback = false;
    private IFunction<I, O> function;
    private io.cognitionbox.petra.core.impl.PEdgeRollbackHelper PEdgeRollbackHelper = new PEdgeRollbackHelper(100);
    private ObjectCopyerViaSerialization copyer = new ObjectCopyerViaSerialization();
    private List<Class<? extends Exception>> throwsRandomly = new ArrayList<>();

    public PEdge() {
        this.feedback = this.getStepClazz().getAnnotationsByType(Feedback.class).length > 0;
    }

    public PEdge(String description, boolean isEffect) {
        super(description,isEffect);
    }

    public PEdge(Guard<I> p, IFunction<I, O> function, GuardXOR<O> q) {
        this.p = p;
        this.function = function;
        this.q = q;
    }

    protected Logger logger() {
        return LoggerFactory.getLogger(this.getStepClazz());
    }

    public IFunction<I, O> getFunction() {
        return function;
    }

    public PEdge<I, O> func(IFunction<I, O> function) {
        this.function = function;
        return this;
    }

    protected void setMillisBeforeRetry(long millisBeforeRetry) {
        this.PEdgeRollbackHelper = new PEdgeRollbackHelper(millisBeforeRetry);
    }

    @Override
    public O call() {
        I input = getInput().getValue();
        if (!p().test(input)) {
            return (O) input;
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
                    return (O) input;
                }
            }

            PEdgeRollbackHelper.capture(input, this);
            boolean inputMatchesPostConditionBeforeRunning = q().test(input);
            O res = null;
            if (RGraphComputer.getConfig().isDeadLockRecoveryActive()) {
                final ExecutorService executor = Executors.newSingleThreadExecutor();
                final Future<O> future = executor.submit(() -> {
                    synchronized (this) { // memory-barrier to ensure all updates are visible before and after func is applied.
                        if (!isEffect() && RGraphComputer.getConfig().isDefensiveCopyAllInputsExceptForEffectedInputs()) {
                            return (O) function.apply(copyer.copy(input));
                        } else {
                            return (O) function.apply(input);
                        }
                    }
                });
                executor.shutdown(); // This does not cancel the already-scheduled task.

                res = future.get(1000, TimeUnit.MILLISECONDS);
            } else {
                synchronized (this) { // memory-barrier to ensure all updates are visible before and after func is applied.
                    if (!isEffect() && RGraphComputer.getConfig().isDefensiveCopyAllInputsExceptForEffectedInputs()) {
                        res = (O) function.apply(copyer.copy(input));
                    } else {
                        res = (O) function.apply(input);
                    }
                }
            }
            if (res == null) {
                return (O) vd;
            } else {
                boolean postConditionOk = q().test(res) || (isFeedback() && p().test(res));
                boolean isNotSideEffectAndDidChangeInput = !isEffect() && !p().test(input);

                // after successfully completes check side effect, allows for looping on same inputs
                boolean isSideEffectAndDidNotChangeInput = isEffect() && p().test(input) && !isFeedback();

                boolean sideEffectOk = !(isSideEffectAndDidNotChangeInput || isNotSideEffectAndDidChangeInput);
                if (!inputMatchesPostConditionBeforeRunning && postConditionOk && sideEffectOk) {
                    return res;
                } else if (RGraphComputer.getConfig().isExceptionsPassthrough()) {

                    if (inputMatchesPostConditionBeforeRunning) {
                        return (O) new EdgeException(input, res, new PreConditionFailure());
                    }

                    if (isNotSideEffectAndDidChangeInput) {
                        return (O) new EdgeException(input, res, new IsNotSideEffectAndDidChangeInput());
                    }

                    if (isSideEffectAndDidNotChangeInput) {
                        return (O) new EdgeException(input, res, new IsSideEffectAndDidNotChangeInput());
                    }

                    if (!postConditionOk) {
                        return (O) new EdgeException(input, res, new PostConditionFailure());
                    }

                    return (O) new EdgeException(input, res, new UnknownEdgeFailure());

                } else {
                    PEdgeRollbackHelper.rollback(input, this);
                    return (O) input;
                }
            }
        } catch (Exclusives.ExclusivesLoadException e) {
            LOG.error(this.getUniqueId(), e);
            if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                // required for Exhaust Petra to highlight errors
                return (O) new EdgeException(input, null, e);
            } else {
                // no rollback as load happens before input is transformed
                return (O) input;
            }
        } catch (Exception e) {
            LOG.error(this.getUniqueId(), e);
            if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                // required for Exhaust Petra to highlight errors
                return (O) new EdgeException(input, null, e);
            } else {
                // swallow exception in production so retry will be triggered

                PEdgeRollbackHelper.rollback(input, this);
                return (O) input;
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

    public PEdge<I, O> throwsRandom(Class<? extends Exception> clazz) {
        throwsRandomly.add(clazz);
        return this;
    }

    @Override
    public boolean isDoesNotTerminate() {
        return false;
    }
}