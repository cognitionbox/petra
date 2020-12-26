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
import io.cognitionbox.petra.exceptions.EdgeException;
import io.cognitionbox.petra.util.function.IBiPredicate;
import io.cognitionbox.petra.util.function.IConsumer;
import io.cognitionbox.petra.util.function.IPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static io.cognitionbox.petra.util.Petra.throwRandomException;


public class PEdge<X> extends AbstractStep<X> implements Serializable {

    final static Logger LOG = LoggerFactory.getLogger(PEdge.class);
    private IConsumer<X> function;
    private io.cognitionbox.petra.core.impl.PEdgeRollbackHelper PEdgeRollbackHelper = new PEdgeRollbackHelper(100);
    private ObjectCopyerViaSerialization copyer = new ObjectCopyerViaSerialization();
    private List<Class<? extends Exception>> throwsRandomly = new ArrayList<>();

    public PEdge() {}

    public PEdge(String description) {
        super(description);
    }

    public PEdge(Guard<X> p, IConsumer<X> function, Guard<X> q) {
        this.p = p;
        this.function = function;
        this.q = q;
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
                    function.accept(input);
                    return input;
                } catch (Throwable e){
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
        } catch (InterruptedException | ExecutionException | TimeoutException e){
            throwableRef.set(e);
            LOG.error(this.getStepClazz().getSimpleName()+" "+this.getUniqueId(), e);
        }
        boolean postConditionOk = throwableRef.get()==null && (res!=null && getActiveKase().q(res) && evalV(res));
        if (postConditionOk) {
            Lg_ALL_STATES("[Eg out]",input);
            return res;
        } else if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
            // need to think about aggregating exceptions at parent graph level
            return (X) new EdgeException(this,input, res, throwableRef.get());
        } else {
            //objectTrans.restore(input);
            PEdgeRollbackHelper.rollback(input, this);
            return (X) input;
        }
    }

    public PEdge copy() {
        // we dont copy the id as we need a unique id based on the hashcode of the new instance
        PEdge PEdge = new PEdge(getPartitionKey());
        PEdge.setKases(getKases());
        PEdge.setClazz(getStepClazz());
        PEdge.func(function);
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

    public void pre(Guard<X> p) {
        setP(p);
    }

    private IBiPredicate<X,X> v = null;

    public void variant(IBiPredicate<X,X> predicate) {
        this.v = predicate;
    }

    private boolean evalV(X x){
        if (this.v ==null){
            return true;
        } else {
            // need to plumb in old value using the object transaction restore
            return this.v.test(x,x);
        }
    }
}
