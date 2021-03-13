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

import io.cognitionbox.petra.core.impl.ObjectCopyerViaSerialization;
import io.cognitionbox.petra.core.impl.OperationType;
import io.cognitionbox.petra.core.impl.PEdgeRollbackHelper;
import io.cognitionbox.petra.exceptions.EdgeException;
import io.cognitionbox.petra.exceptions.conditions.PostConditionFailure;
import io.cognitionbox.petra.util.function.IBiPredicate;
import io.cognitionbox.petra.util.function.IConsumer;
import io.cognitionbox.petra.util.function.IPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static io.cognitionbox.petra.util.Petra.throwRandomException;


public class PEdge<X> extends AbstractStep<X> implements Serializable {

    final static Logger LOG = LoggerFactory.getLogger(PEdge.class);
    private IConsumer<X> function;
    private io.cognitionbox.petra.core.impl.PEdgeRollbackHelper PEdgeRollbackHelper = new PEdgeRollbackHelper(100);
    private ObjectCopyerViaSerialization copyer = new ObjectCopyerViaSerialization();
    private List<Class<? extends Exception>> throwsRandomly = new ArrayList<>();

    public PEdge() {
        super("");
    }

    public PEdge(String description) {
        super(description);
    }

    public PEdge(Guard<X> p, IConsumer<X> function, Guard<X> q) {
        super(p, q);
        this.function = function;
    }

    protected Logger logger() {
        return LoggerFactory.getLogger(this.getStepClazz());
    }

    public IConsumer<X> getFunction() {
        return function;
    }

    public PEdge<X> func(IConsumer<X> function) {
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
        setActiveKase(getInput().getValue());
        if (!getActiveKase().p().test(input)) {
            return (X) input;
        }
        PEdgeRollbackHelper.capture(input, this);
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        //ObjectTrans objectTrans = new ObjectTrans();
        AtomicReference<Throwable> throwableRef = new AtomicReference<>();
        final Future<X> future = executor.submit(() -> {
            synchronized (this) { // memory-barrier to ensure all updates are visible before and after func is applied.
                //if (RGraphComputer.getConfig().isDefensiveCopyAllInputs()) {
                //objectTrans.capture(input);
                //}
                try {
                    if (RGraphComputer.getConfig().isTestMode() && (throwsRandomly != null && throwsRandomly.size() > 0)) {
                        throwRandomException(throwsRandomly);
                    }
                    Collection<Object> results = null;
                    if (this instanceof PCollectionEdge) {
                        if (PComputer.getConfig().getMode().isPAR() &&
                                ((PCollectionEdge<X, ?, ?>) this).getExecMode().isPAR()) {
                            // defensively copy input to protect from race conditions
                            results = ((PCollectionEdge<X, ?, ?>) this).collection().apply(input)
                                    .parallelStream()
                                    .map(y -> {
                                        try {
                                            if (((PCollectionEdge) this).getBefore().test(copyer.copy(((PCollectionEdge<X, ?, ?>) this).shared().apply(input)), y)) {
                                                ((PCollectionEdge) this).biConsumer.accept(copyer.copy(((PCollectionEdge<X, ?, ?>) this).shared().apply(input)), y);
                                                if (!((PCollectionEdge) this).getAfter().test(copyer.copy(((PCollectionEdge<X, ?, ?>) this).shared().apply(input)), y)) {
                                                    throw new PostConditionFailure();
                                                }
                                            }
                                            return y;
                                        } catch (Throwable e) {
                                            LOG.error(this.getStepClazz().getName(), e);
                                            return e;
                                        }
                                    }).collect(Collectors.toList());
                        } else if (PComputer.getConfig().getMode().isSEQ() ||
                                ((PCollectionEdge<X, ?, ?>) this).getExecMode().isSEQ()) {
                            results = ((PCollectionEdge<X, ?, ?>) this).collection().apply(input)
                                    .stream()
                                    .map(y -> {
                                        try {
                                            if (((PCollectionEdge) this).getBefore().test(((PCollectionEdge<X, ?, ?>) this).shared().apply(input), y)) {
                                                ((PCollectionEdge) this).biConsumer.accept(((PCollectionEdge<X, ?, ?>) this).shared().apply(input), y);
                                                if (!((PCollectionEdge) this).getAfter().test(((PCollectionEdge<X, ?, ?>) this).shared().apply(input), y)) {
                                                    throw new PostConditionFailure();
                                                }
                                            }
                                            return y;
                                        } catch (Throwable e) {
                                            LOG.error(this.getStepClazz().getName(), e);
                                            return e;
                                        }
                                    }).collect(Collectors.toList());
                        }
                    } else if (this instanceof PEdge) {
                        this.function.accept(input);
                    }
                    if ((this instanceof PCollectionEdge) && results.stream().anyMatch(o -> o instanceof Throwable)) {
                        throw new PostConditionFailure();
                    }
                    return input;
                } catch (Throwable e) {
                    throwableRef.set(e);
                    LOG.error(this.getUniqueId(), e);
                }
                return null;
            }
        });
        executor.shutdown(); // This does not cancel the already-scheduled task.
        X res = null;
        try {
            res = future.get(60, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throwableRef.set(e);
            LOG.error(this.getStepClazz().getSimpleName() + " " + this.getUniqueId(), e);
        }
        boolean postConditionOk = throwableRef.get() == null && (res != null && q().test(res) && evalV(res));
        if (postConditionOk) {
            if (this.isInitStep() && !this.isInited()) {
                setInited(true);
            }
            Lg_ALL_STATES("[Eg out]", input);
            return res;
        } else if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
            // need to think about aggregating exceptions at parent graph level
            return (X) new EdgeException(this, input, res, throwableRef.get());
        } else {
            //objectTrans.restore(input);
            PEdgeRollbackHelper.rollback(input, this);
            return (X) input;
        }
    }

    public PEdge copy() {
        // we dont copy the id as we need a unique id based on the hashcode of the new instance
        PEdge PEdge;
        if (this instanceof PCollectionEdge) {
            PEdge = new PCollectionEdge(getPartitionKey());
            ((PCollectionEdge) PEdge).shared(((PCollectionEdge) this).getExecMode(), ((PCollectionEdge) this).shared());
            ((PCollectionEdge) PEdge).collection(((PCollectionEdge) this).collection());
            ((PCollectionEdge) PEdge).func(((PCollectionEdge) this).getBiConsumer());
            ((PCollectionEdge) PEdge).pre(((PCollectionEdge) this).getBefore());
            ((PCollectionEdge) PEdge).post(((PCollectionEdge) this).getAfter());
        } else {
            PEdge = new PEdge(getPartitionKey());
            PEdge.func(function);
        }
        PEdge.setP(p());
        PEdge.setQ(q());
        PEdge.setKases(getKases());
        PEdge.setClazz(getStepClazz());
        PEdge.type(getType());
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

//    public void pre(IPredicate<X> predicate) {
//        setP(new GuardWrite(getType(), predicate));
//    }
//
//    public void post(GuardReturn<X> q) {
//        setQ(q);
//    }
//
//    public void post(IPredicate<X> predicate) {
//        setQ(new Guard<>(getType(), predicate, OperationType.RETURN));
//    }

    private IBiPredicate<X, X> v = null;

    public void variant(IBiPredicate<X, X> predicate) {
        this.v = predicate;
    }

    private boolean evalV(X x) {
        if (this.v == null) {
            return true;
        } else {
            // need to plumb in old value using the object transaction restore
            return this.v.test(x, x);
        }
    }

    public void post(IPredicate<X> predicate) {
        setQ(new Guard(getType(),predicate, OperationType.READ_WRITE));
        kases.add(new Kase<X>(this,getType(),p,q));
    }
}
