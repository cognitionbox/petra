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

import io.cognitionbox.petra.core.*;
import io.cognitionbox.petra.core.engine.StepCallable;
import io.cognitionbox.petra.core.engine.StepResult;
import io.cognitionbox.petra.core.engine.extractors.ExtractedStore;
import io.cognitionbox.petra.core.engine.extractors.impl.AbstractRootValueExtractor;
import io.cognitionbox.petra.core.engine.extractors.impl.ExtractedStoreImpl;
import io.cognitionbox.petra.core.engine.extractors.impl.SequentialRootValueExtractor;
import io.cognitionbox.petra.core.engine.petri.Place;
import io.cognitionbox.petra.core.engine.petri.impl.Token;
import io.cognitionbox.petra.core.impl.*;
import io.cognitionbox.petra.lang.annotations.DoesNotTerminate;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.exceptions.GraphException;
import io.cognitionbox.petra.exceptions.IterationsTimeoutException;
import io.cognitionbox.petra.exceptions.JoinException;
import io.cognitionbox.petra.exceptions.PetraException;
import io.cognitionbox.petra.exceptions.conditions.PostConditionFailure;
import io.cognitionbox.petra.exceptions.sideeffects.SideEffectFailure;
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.util.function.*;
import io.cognitionbox.petra.util.impl.PList;
import io.cognitionbox.petra.util.Petra;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.cognitionbox.petra.lang.Void.vd;
import static io.cognitionbox.petra.util.Petra.rt;
import static java.lang.Math.min;

public class RGraph<X extends D,D> extends AbstractStep<X> implements IGraph<X> {

    final static Logger LOG = LoggerFactory.getLogger(RGraph.class);
    List<StepCallable> callables = new ArrayList<>();
    private boolean doesNotTerminate = this.getStepClazz().isAnnotationPresent(DoesNotTerminate.class);
    private List<IStep> parallizable = new ArrayList<>();
    private transient IEdgeDotLogger stepDotLogger = new PEdgeDotLoggerImpl();
    private List<IJoin> joinsTypes = new ArrayList<>();
    private List<Triplet<Guard<IPredicate<X>>,IConsumer<X>,Guard<IPredicate<X>>>> newJoins = new ArrayList<>();
    private List<IBiConsumer<Collection<IToken>, RGraph>> joins =
            new ArrayList<>();
    private Set lastStates;
    Place place;
    private boolean logImplementationDetails = false;
    private AtomicLong iterationId = new AtomicLong(0);
    private long currentIteration;
    private Long maxIterations = RGraphComputer.getConfig().getMaxIterations();
    private long sleepPeriod = RGraphComputer.getConfig().getSleepPeriod();
    private JoinRollbackHelper rollbackHelper = new JoinRollbackHelper();

    public RGraph() {
        init();
    }

    public RGraph(String description, boolean isEffect) {
        super(description, isEffect);
        init();
    }

    public boolean isDoesNotTerminate() {
        return doesNotTerminate;
    }

    void setDoesNotTerminate(boolean doesNotTerminate) {
        this.doesNotTerminate = doesNotTerminate;
    }

    public List<IJoin> getJoinTypes() {
        return joinsTypes;
    }

    private AbstractRootValueExtractor rootValueExtractor = new SequentialRootValueExtractor();

    void initInput(){
        if (this.p().getTypeClass().isAnnotationPresent(Extract.class)){
            deconstruct(getInput());
            putState(getInput().getValue());
        } else {
            putState(getInput().getValue());
        }
    }

    X executeMatchingLoopUntilPostCondition() {
        currentIteration = 0;
        while (evalP(getInput().getValue()) && this.loopCondition.test(getInput().getValue())) {
            try {
                Lg();
                X out = iteration();
                if (out!=null){
                    return out;
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            iterationId.getAndIncrement();
            currentIteration++;
        }
        // loop terminated before post condition reached
        throw new PostConditionFailure();
    }

    X iteration(){
        if (currentIteration > getMaxIterations()) {
            // if we go past the max iterations, we have failed to meet the post cons
            putState(new IterationsTimeoutException());
            return (X) new IterationsTimeoutException();
        } else {
            if (sleepPeriod>0) {
                sleep(sleepPeriod);
            }
//            boolean b = getPlace().size() == 1;
//            boolean c = false;
//            if (b){
//                for (IToken o : getPlace()){
//                    if (p().test(o.getValue())){
//                        c = true;
//                        break;
//                    }
//                }
//            }
//            if ((b && c)) {
//                if (p().getTypeClass().isAnnotationPresent(Extract.class)){
//                    deconstruct(getInput());
//                }
//            }

            //De();
            Ex();
            Jn();
            Object toReturn = Rt();
            if (toReturn != null) {
                return (X) toReturn;
            } else {
                return null;
            }
        }
    }

    public void step(Class<? extends IStep<?>> computation) {
        step(Petra.createStep(computation));
    }

//    public void step(IStep<? super I> computation) {
//        addParallizable(computation);
//    }

    public void step(IStep<?> computation) {
        addParallizable(computation);
    }

    private IPredicate<X> loopCondition = x->true;
    public void lc(IPredicate<X> loopCondition) {
        this.loopCondition = loopCondition;
    }

    private void addJoinType(IJoin join) {
        joinsTypes.add(join);
    }

    // need to add the domain restriction support to joins as rc point
    @Deprecated
    public <A extends D, R extends D> void joinSome(Class<? extends IJoin> clazz) {
        if (AbstractPureJoin1.class.isAssignableFrom(clazz)) {
            joinSome((AbstractPureJoin1<? extends D, ? extends D>) Petra.createJoin(clazz));
        } else if (AbstractPureJoin2.class.isAssignableFrom(clazz)) {
            joinSome((AbstractPureJoin2<? extends D, ? extends D, ? extends D>) Petra.createJoin(clazz));
        } else if (AbstractPureJoin3.class.isAssignableFrom(clazz)) {
            joinSome((AbstractPureJoin3<? extends D, ? extends D, ? extends D, ? extends D>) Petra.createJoin(clazz));
        }
        throw new UnsupportedOperationException();
    }

    public <A extends D, B extends D, R extends D> void join(IPredicate<X> pre,
                                                             IConsumer<X> joinEdge,
                                                             IPredicate<X> post) {
        this.newJoins.add(Triplet.with(new GuardWrite(Object.class, pre),joinEdge,new GuardWrite(Object.class, post)));
    }

    public <A extends D, R extends D> void joinSome(AbstractPureJoin1<A, R> PJoinEdge) {
        addJoinType(PJoinEdge);
        join(false,PJoinEdge, PJoinEdge.a(), PJoinEdge.func(), PJoinEdge.r(), null);
    }

    public <A extends D, B extends D, R extends D> void joinSome(AbstractPureJoin2<A, B, R> joinEdge) {
        addJoinType(joinEdge);
        join(false,joinEdge, joinEdge.a(), joinEdge.b(), joinEdge.func(), joinEdge.r(), null);
    }

    public <A extends D, B extends D, C extends D, R extends D> void joinSome(AbstractPureJoin3<A, B, C, R> joinEdge) {
        addJoinType(joinEdge);
        join(false,joinEdge, joinEdge.a(), joinEdge.b(), joinEdge.c(), joinEdge.func(), joinEdge.r(), null);
    }

    public <A extends D, R extends D> void joinAll(AbstractPureJoin1<A, R> PJoinEdge) {
        addJoinType(PJoinEdge);
        join(true,PJoinEdge, PJoinEdge.a(), PJoinEdge.func(), PJoinEdge.r(), null);
    }

    public <A extends D, B extends D, R extends D> void joinAll(AbstractPureJoin2<A, B, R> joinEdge) {
        addJoinType(joinEdge);
        join(true,joinEdge, joinEdge.a(), joinEdge.b(), joinEdge.func(), joinEdge.r(), null);
    }

    public <A extends D, B extends D, C extends D, R extends D> void joinAll(AbstractPureJoin3<A, B, C, R> joinEdge) {
        addJoinType(joinEdge);
        join(true,joinEdge, joinEdge.a(), joinEdge.b(), joinEdge.c(), joinEdge.func(), joinEdge.r(), null);
    }

    private <A extends D, B extends D, C extends D, R extends D, E extends Throwable> void join(boolean joinAll, AbstractPureJoin3 join, Guard<? super A> a, Guard<? super B> b, Guard<? super C> c,
                                                                                                ITriFunction<List<A>, List<B>, List<C>, R> transform, Guard<? super R> predicate, List<Class<? extends Exception>> throwsRandomly) {
        int index = getNoOfJoins();
        addJoin(index, (toUse, toWrite) -> {
            executePureJoin3(joinAll,join, index, a, b, c, transform, predicate, toUse, toWrite, throwsRandomly);
        });
    }

    private <A extends D, B extends D, R extends D, E extends Throwable> void join(boolean joinAll, AbstractPureJoin2 join, Guard<? super A> a, Guard<? super B> b, IBiFunction<List<A>, List<B>, R> transform, Guard<? super R> predicate, List<Class<? extends Exception>> throwsRandomly) {
        int index = getNoOfJoins();
        addJoin(index, (toUse, toWrite) -> {
            executePureJoin2(joinAll,join, index, a, b, transform, predicate, toUse, toWrite, throwsRandomly);
        });
    }

    private <A extends D, R extends D> void join(boolean joinAll, AbstractPureJoin1 join, Guard<? super A> a, IFunction<List<A>, R> transform, Guard<? super R> predicate, List<Class<? extends Exception>> throwsRandomly) {
        int index = getNoOfJoins();
        addJoin(index, (toUse, toWrite) -> {
            executePureJoin1(joinAll,join, index, a, transform, predicate, toUse, toWrite, throwsRandomly);
        });
    }



    public <A extends D, R extends D> void joinSome(AbstractEffectJoin1<A> PJoinEdge) {
        addJoinType(PJoinEdge);
        join(false,PJoinEdge, PJoinEdge.a(), PJoinEdge.func(),null);
    }

    public <A extends D, B extends D, R extends D> void joinSome(AbstractEffectJoin2<A, B> joinEdge) {
        addJoinType(joinEdge);
        join(false,joinEdge, joinEdge.a(), joinEdge.b(), joinEdge.func(), null);
    }

    public <A extends D, B extends D, C extends D, R extends D> void joinSome(AbstractEffectJoin3<A, B, C> joinEdge) {
        addJoinType(joinEdge);
        join(false,joinEdge, joinEdge.a(), joinEdge.b(), joinEdge.c(), joinEdge.func(),null);
    }

    public <A extends D, R extends D> void joinAll(AbstractEffectJoin1<A> PJoinEdge) {
        addJoinType(PJoinEdge);
        join(true,PJoinEdge, PJoinEdge.a(), PJoinEdge.func(),null);
    }

    public <A extends D, B extends D, R extends D> void joinAll(AbstractEffectJoin2<A, B> joinEdge) {
        addJoinType(joinEdge);
        join(true,joinEdge, joinEdge.a(), joinEdge.b(), joinEdge.func(), null);
    }

    public <A extends D, B extends D, C extends D, R extends D> void joinAll(AbstractEffectJoin3<A, B, C> joinEdge) {
        addJoinType(joinEdge);
        join(true,joinEdge, joinEdge.a(), joinEdge.b(), joinEdge.c(), joinEdge.func(),null);
    }

    private <A extends D, B extends D, C extends D, R extends D, E extends Throwable> void join(boolean joinAll, AbstractEffectJoin3 join, Guard<? super A> a, Guard<? super B> b, Guard<? super C> c,
                                                                                                ITriConsumer<List<A>, List<B>, List<C>> transform, List<Class<? extends Exception>> throwsRandomly) {
        int index = getNoOfJoins();
        addJoin(index, (toUse, toWrite) -> {
            executeEffectJoin3(joinAll,join, index, a, b, c, transform, toUse, toWrite, throwsRandomly);
        });
    }

    private <A extends D, B extends D, R extends D, E extends Throwable> void join(boolean joinAll, AbstractEffectJoin2 join, Guard<? super A> a, Guard<? super B> b, IBiConsumer<List<A>, List<B>> transform, List<Class<? extends Exception>> throwsRandomly) {
        int index = getNoOfJoins();
        addJoin(index, (toUse, toWrite) -> {
            executeEffectJoin2(joinAll,join, index, a, b, transform, toUse, toWrite, throwsRandomly);
        });
    }

    private <A extends D, R extends D> void join(boolean joinAll, AbstractEffectJoin1 join, Guard<? super A> a, IConsumer<List<A>> transform, List<Class<? extends Exception>> throwsRandomly) {
        int index = getNoOfJoins();
        addJoin(index, (toUse, toWrite) -> {
            executeEffectJoin1(joinAll,join, index, a, transform, toUse, toWrite, throwsRandomly);
        });
    }






    public List<IStep> getParallizable() {
        return parallizable;
    }

    Collection<IToken> getPlace() {
        return place.getTokens();
    }

    private void init() {
        ExecMode mode = RGraphComputer.getConfig().getMode();
        place = Petra.getFactory().createPlace(getUniqueId());
    }

    public String getTransitions() {
        return stepDotLogger.getTransitions();
    }


    void addJoin(int index, IBiConsumer<Collection<IToken>, RGraph> consumer) {
        joins.add(index, consumer);
        stepDotLogger.logJoin(this, index);
    }

    void De(){
         for (IToken t : place.getTokens()){
             deconstruct(t);
         }
    }

    // Exclusivity check
    void Ex() {
        Lg_ALL_STATES("[Ex in]");
        Collection<IToken> toUse = getWorkingStatesToUse();
//        if (place.tokensMatchedByUniqueStepPreconditions(this.getParallizable())) {
            Collections.rotate(parallizable, 1);
            matchComputationsToStatesAndExecute(parallizable);
//        } else {
//            if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
//                throw new AssertionError("fails overlap check!");
//            }
//        }
        Lg_ALL_STATES("[Ex out]");
    }

    Collection<IToken> getWorkingStatesToUse() {
        return getPlace();
    }

    private void collectCallable(StepCallable callable, List<StepCallable> callables) {
        callables.add(callable);
    }

    private void forkAndJoinCallables(List<StepCallable> callables) {
        if (RGraphComputer.getConfig().getMode().isSEQ()){
            for (StepCallable callable : callables) {
                try {
                    StepResult sr = callable.call();
                    if (OperationType.READ_WRITE != sr.getOperationType()) {
                        deconstruct(sr.getOutputValue());
                        //putState(f.get().getOutputValue());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                List<Future<StepResult>> futures = RGraphComputer.getWorkerExecutor().invokeAll(callables);
                for (Future<StepResult> f : futures){
                    if (OperationType.READ_WRITE != f.get().getOperationType()) {
                        deconstruct(f.get().getOutputValue());
                        //putState(f.get().getOutputValue());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void matchComputationsToStatesAndExecute(List<IStep> steps) {
        callables.clear();
        for (int index = 0; index < steps.size(); index = index + 1) {
            IStep c = steps.get(index);
            for (Object token : place.filterTokensByValue(c.p())) {
                AbstractStep copy = null;
                if (c instanceof AbstractStep) {
                    copy = ((AbstractStep) c).copy();
                }
                copy.setInput((IToken) token);
                collectCallable(new StepCallable(this,copy), callables);
            }
        }
        forkAndJoinCallables(callables);
    }

    <D> void addParallizable(IStep<? extends D> computation) {
        if (Throwable.class.isAssignableFrom(computation.p().getTypeClass())) {
            throw new UnsupportedOperationException("Cannot match directly on Throwable types, please handle errors inside XEdges instead.");
        }
        parallizable.add((AbstractStep) computation);
        stepDotLogger.logCompletedStep(this, computation);
    }

    int getNoOfParallizables() {
        return parallizable.size();
    }

    public int getNoOfJoins() {
        return joins.size();
    }

    void Jn() {
        Lg_ALL_STATES("[Jn in]");
        for (IBiConsumer<Collection<IToken>, RGraph> t : this.joins) {
            t.accept(getWorkingStatesToUse(), this);
        }
        for (Triplet<Guard<IPredicate<X>>,IConsumer<X>,Guard<IPredicate<X>>> t : this.newJoins) {
            if (t.getValue0().test(getInput().getValue())){
                t.getValue1().accept(this.getInput().getValue());
                if (t.getValue2().test(getInput().getValue())){
                   // ok
                }
            }
        }
        Lg_ALL_STATES("[Jn out]");
    }

    private Object peekState() {
        Optional<IToken> opt = place.findAny();
        if (!opt.isPresent()) {
            return null;
        }
        return opt.get().getValue();
    }

    private boolean doReturnVOID() {
        return this.getPlace().size() == 0 && this.q().isVoid();
    }

    private List<PetraException> exceptions() {
        return this.getPlace()
                .stream()
                .filter(s -> s.getValue() instanceof PetraException)
                .map(s -> (PetraException) s.getValue())
                .collect(Collectors.toCollection(() -> new PList<>()));
    }

    X Rt() {
        List<PetraException> exceptions = exceptions();
        if (!exceptions.isEmpty()) {
            for (PetraException petraException : exceptions) {
                petraException.printStackTrace();
                for (Throwable t : petraException.getCauses()) {
                    t.printStackTrace();
                }
            }
            return (X) new GraphException((X) this.getInput().getValue(), null, exceptions);
        }
        if (checkOutput(getInput().getValue())) {
            if (this.getPlace().size() == 0) {
                return (X) new IllegalStateException("cannot have zero tokens in place and not return void.");
            }
            Object obj = getInput().getValue();
            /*
             * Arpad's Hack
             */
            /*
             * Arpad's Kotlin Hack
             */
            if (this.q().getTypeClass().equals(int.class) && Integer.class.isInstance(obj)) {
                if (this.evalQ((X) obj)) {
                    return (X) obj;
                }
            }

            if (this.q().getTypeClass().equals(int.class)) {
                if (Integer.class.isInstance(obj)) {
                    if (this.evalQ((X) obj)) {
                        return (X) obj;
                    }
                }
            }
            if (checkOutput(obj)) {
                return (X) obj;
            }
        }
        return null;
    }

    private boolean checkOutput(Object output) {
        return (this.q() != null && this.q().generic().test(output));
    }

    private void Lg() {
        Set newStates = null;
        if (RGraphComputer.getConfig().isStatesLoggingEnabled()) {
            newStates = new HashSet(this.getPlace());
            if (lastStates == null || !lastStates.toString().equals(newStates.toString())) { // logs only if state changes
                LOG.info(RGraphComputer.getConfig().getMode() + " " + " " + this.getPartitionKey() + ":" + iterationId.get() + " thread=" + Thread.currentThread().getId() + " size=" + newStates.size() + " -> " + newStates);
            }
        }
        this.lastStates = newStates;
    }

    private void Lg_ALL_STATES(String desc) {
        if (RGraphComputer.getConfig().isAllStatesLoggingEnabled()) {
            List copy = this.getPlace().stream().map(p -> p.getValue()).collect(Collectors.toList());
            String stepId = "";
            if (logImplementationDetails) {
                stepId = this.getUniqueId();
            }
            LOG.info(desc + " " + RGraphComputer.getConfig().getMode() + " " + " " + this.getPartitionKey() + " " + stepId + ":" + iterationId.get() + " thread=" + Thread.currentThread().getId() + " size=" + copy.size() + " -> " + copy);
        }
    }

    private void Lg_STATE(String desc, IStep edge, Object o) {
        if (RGraphComputer.getConfig().isStatesLoggingEnabled()) {
            String stepId = "";
            if (logImplementationDetails) {
                stepId = edge.getUniqueId();
            }
            LOG.info(desc + " " + RGraphComputer.getConfig().getMode() + " " + " " + this.getPartitionKey() + " " + stepId + ":" + iterationId.get() + " thread=" + Thread.currentThread().getId() + " -> " + o);
        }
    }

    private void randomSleepToPreventThreadStarvationAndMinimizeWaitsOnLocks() {
        sleep((long) (Math.random() * 10));
    }

    public long getCurrentIteration() {
        return currentIteration;
    }

    public Long getMaxIterations() {
        return RGraphComputer.getConfig().isTestMode() ? getTestModeMaxIterations() : Long.MAX_VALUE;
    }

    protected void setMaxIterations(long maxIterations) {
        this.maxIterations = maxIterations;
    }

    public Long getTestModeMaxIterations() {
        return maxIterations;
    }

    protected void setSleepPeriod(long sleepPeriod) {
        this.sleepPeriod = sleepPeriod;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e1) {
        }
    }

    // PJoin 1 rc
    <A, E extends Throwable> void executeEffectJoin1(boolean joinAll, AbstractEffectJoin1 join, int index, Guard<? super A> a,
                                                      IConsumer<List<A>> transform, Collection<IToken> toUse, RGraph toWrite, List<Class<? extends Exception>> throwsRandomly) {
        Guard[] Guards = new Guard[1];
        Guards[0] = a;
        List<List<IToken>> listOfMatches = new PList();
        populateListOfMatchesAndRemoveFromCurrentStates(join, index, Guards, listOfMatches, toUse);
        if ((!joinAll && listOfMatches.stream().flatMap(l->l.stream()).findAny().isPresent()) || (joinAll && listOfMatches.stream().flatMap(l->l.stream()).count()==toUse.size())){
            List<IToken> one = listOfMatches.get(0);
            PList<A> as = one.stream().map(x -> x.getValue()).collect(Collectors.toCollection(() -> new PList()));
            try {
                if (RGraphComputer.getConfig().isTestMode() && (throwsRandomly != null && throwsRandomly.size() > 0)) {
                    Petra.throwRandomException(throwsRandomly);
                }
                //rollbackHelper.captureListStates(join.getMillisBeforeRetry(), join, as);
                synchronized (this) {
                    transform.accept(as);
                }
                boolean postConditionOk = as.stream().allMatch(join.postA);
                boolean sideEffectsOk = true;//rollbackHelper.sideEffectsCheck(join, as);
                if (postConditionOk && sideEffectsOk) {

                } else {
                    if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                        if (!postConditionOk && sideEffectsOk) {
                            toWrite.putState(new JoinException(null, new PostConditionFailure()));
                        } else if (postConditionOk && !sideEffectsOk) {
                            toWrite.putState(new JoinException(null, new SideEffectFailure()));
                        } else if (!postConditionOk && !sideEffectsOk) {
                            toWrite.putState(new JoinException(null, new PostConditionFailure(), new SideEffectFailure()));
                        }
                    } else {
                       // rollbackHelper.rollbackListStates(join.getMillisBeforeRetry(), join, as);
                    }
                }
            } catch (Exception e) {
                LOG.error(this.getUniqueId()+"."+join.getJoinClazz().getSimpleName()+"."+index, e);
                if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                    toWrite.putState(new JoinException(null, e));
                } else {
                    //rollbackHelper.rollbackListStates(join.getMillisBeforeRetry(), join, as);
                }
            }
        }
    }

    // PJoin 1 rc
    <A, R, E extends Throwable> void executePureJoin1(boolean joinAll, AbstractPureJoin1 join, int index, Guard<? super A> a,
                                                      IFunction<List<A>, R> transform,
                                                      Guard<? super R> predicate, Collection<IToken> toUse, RGraph toWrite, List<Class<? extends Exception>> throwsRandomly) {
        Guard[] Guards = new Guard[1];
        Guards[0] = a;
        List<List<IToken>> listOfMatches = new PList();
        populateListOfMatchesAndRemoveFromCurrentStates(join, index, Guards, listOfMatches, toUse);
        if ((!joinAll && listOfMatches.stream().flatMap(l->l.stream()).findAny().isPresent()) || (joinAll && listOfMatches.stream().flatMap(l->l.stream()).count()==toUse.size())){
            List<IToken> one = listOfMatches.get(0);
            Object value = null;
            PList<A> as = one.stream().map(x -> x.getValue()).collect(Collectors.toCollection(() -> new PList()));
            try {
                if (RGraphComputer.getConfig().isTestMode() && (throwsRandomly != null && throwsRandomly.size() > 0)) {
                    Petra.throwRandomException(throwsRandomly);
                }
                synchronized (this) {
                    value = transform.apply(as);
                }
                if (value == null) {
                    value = vd;
                }
                boolean postConditionOk = predicate.test((R) value);
                if (postConditionOk) {
                    // remove matches
                    int i = 0;
                    for (List matches : listOfMatches) {
                        i++;
                    }
                    toWrite.deconstruct(new Token(value),t->true);
                } else {
                    if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                        if (!postConditionOk) {
                            toWrite.putState(new JoinException(value, new PostConditionFailure()));
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error(this.getUniqueId()+"."+join.getJoinClazz().getSimpleName()+"."+index, e);
                if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                    toWrite.putState(new JoinException(null, e));
                }
            }
        }
    }

    // PJoin 2 types
    <A, B, E extends Throwable> void executeEffectJoin2(boolean joinAll, AbstractEffectJoin2 join, int index, Guard<? super A> a, Guard<? super B> b,
                                                         IBiConsumer<List<A>, List<B>> transform, Collection<IToken> toUse, RGraph toWrite, List<Class<? extends Exception>> throwsRandomly) {
        Guard[] Guards = new Guard[2];
        Guards[0] = a;
        Guards[1] = b;
        List<List<IToken>> listOfMatches = new PList();
        populateListOfMatchesAndRemoveFromCurrentStates(join, index, Guards, listOfMatches, toUse);
        if ((!joinAll && listOfMatches.stream().flatMap(l->l.stream()).findAny().isPresent()) || (joinAll && listOfMatches.stream().flatMap(l->l.stream()).count()==toUse.size())){
            Pair<PList<IToken>, PList<IToken>> pair = Pair.with((PList<IToken>) listOfMatches.get(0), (PList<IToken>) listOfMatches.get(1));
            Object value = null;
            PList<A> as = (PList<A>) pair.getValue0().stream().map(x -> x.getValue()).collect(Collectors.toCollection(() -> new PList<>()));
            PList<B> bs = (PList<B>) pair.getValue1().stream().map(x -> x.getValue()).collect(Collectors.toCollection(() -> new PList<>()));
            try {
                if (RGraphComputer.getConfig().isTestMode() && (throwsRandomly != null && throwsRandomly.size() > 0)) {
                    Petra.throwRandomException(throwsRandomly);
                }
                //rollbackHelper.captureListStates(join.getMillisBeforeRetry(), join, as, bs);
                synchronized (this) {
                    transform.accept(as, bs);
                }
                boolean postConditionOk = as.stream().allMatch(join.postA) && bs.stream().allMatch(join.postB);
                boolean sideEffectsOk = true;//rollbackHelper.sideEffectsCheck(join, as, bs);
                if (postConditionOk) {

                } else {
                    if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                        if (!postConditionOk && sideEffectsOk) {
                            toWrite.putState(new JoinException(value, new PostConditionFailure()));
                        } else if (postConditionOk && !sideEffectsOk) {
                            toWrite.putState(new JoinException(value, new SideEffectFailure()));
                        } else if (!postConditionOk && !sideEffectsOk) {
                            toWrite.putState(new JoinException(value, new PostConditionFailure(), new SideEffectFailure()));
                        }
                    } else {
                        //rollbackHelper.rollbackListStates(join.getMillisBeforeRetry(), join, as, bs);
                    }
                }
            } catch (Exception e) {
                LOG.error(this.getUniqueId()+"."+join.getJoinClazz().getSimpleName()+"."+index, e);
                if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                    toWrite.putState(new JoinException(null, e));
                } else {
                    //rollbackHelper.rollbackListStates(join.getMillisBeforeRetry(), join, as, bs);
                }
            }
        }
    }

    // PJoin 2 types
    <A, B, R, E extends Throwable> void executePureJoin2(boolean joinAll, AbstractPureJoin2 join, int index, Guard<? super A> a, Guard<? super B> b,
                                                         IBiFunction<List<A>, List<B>, R> transform,
                                                         Guard<? super R> predicate, Collection<IToken> toUse, RGraph toWrite, List<Class<? extends Exception>> throwsRandomly) {
        Guard[] Guards = new Guard[2];
        Guards[0] = a;
        Guards[1] = b;
        List<List<IToken>> listOfMatches = new PList();
        populateListOfMatchesAndRemoveFromCurrentStates(join, index, Guards, listOfMatches, toUse);
        if ((!joinAll && listOfMatches.stream().flatMap(l->l.stream()).findAny().isPresent()) || (joinAll && listOfMatches.stream().flatMap(l->l.stream()).count()==toUse.size())){
            Pair<PList<IToken>, PList<IToken>> pair = Pair.with((PList<IToken>) listOfMatches.get(0), (PList<IToken>) listOfMatches.get(1));
            Object value = null;
            PList<A> as = (PList<A>) pair.getValue0().stream().map(x -> x.getValue()).collect(Collectors.toCollection(() -> new PList<>()));
            PList<B> bs = (PList<B>) pair.getValue1().stream().map(x -> x.getValue()).collect(Collectors.toCollection(() -> new PList<>()));
            try {
                if (RGraphComputer.getConfig().isTestMode() && (throwsRandomly != null && throwsRandomly.size() > 0)) {
                    Petra.throwRandomException(throwsRandomly);
                }
                synchronized (this) {
                    value = transform.apply(as, bs);
                }
                if (value == null) {
                    value = vd;
                }
                boolean postConditionOk = predicate.test((R) value);
                if (postConditionOk) {
                    // remove matches
                    int i = 0;
                    for (List matches : listOfMatches) {
                        i++;
                    }
                    toWrite.deconstruct(new Token(value),t->true);
                } else {
                    if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                        if (!postConditionOk) {
                            toWrite.putState(new JoinException(value, new PostConditionFailure()));
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error(this.getUniqueId()+"."+join.getJoinClazz().getSimpleName()+"."+index, e);
                if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                    toWrite.putState(new JoinException(null, e));
                }
            }
        }
    }

    // PJoin 3 types
    <A, B, C, E extends Throwable> void executeEffectJoin3(boolean joinAll, AbstractEffectJoin3 join, int index, Guard<? super A> a, Guard<? super B> b, Guard<? super C> c,
                                                            ITriConsumer<List<A>, List<B>, List<C>> transform, Collection<IToken> toUse, RGraph toWrite, List<Class<? extends Exception>> throwsRandomly) {
        Guard[] Guards = new Guard[3];
        Guards[0] = a;
        Guards[1] = b;
        Guards[2] = c;
        List<List<IToken>> listOfMatches = new PList();
        populateListOfMatchesAndRemoveFromCurrentStates(join, index, Guards, listOfMatches, toUse);
        if ((!joinAll && listOfMatches.stream().flatMap(l->l.stream()).findAny().isPresent()) || (joinAll && listOfMatches.stream().flatMap(l->l.stream()).count()==toUse.size())){
            Triplet<PList<IToken>, PList<IToken>, PList<IToken>> triplet = Triplet.with((PList<IToken>) listOfMatches.get(0), (PList<IToken>) listOfMatches.get(1), (PList<IToken>) listOfMatches.get(2));
            PList<A> as = (PList<A>) triplet.getValue0().stream().map(x -> x.getValue()).collect(Collectors.toCollection(() -> new PList<>()));
            PList<B> bs = (PList<B>) triplet.getValue1().stream().map(x -> x.getValue()).collect(Collectors.toCollection(() -> new PList<>()));
            PList<C> cs = (PList<C>) triplet.getValue2().stream().map(x -> x.getValue()).collect(Collectors.toCollection(() -> new PList<>()));
            try {
                if (RGraphComputer.getConfig().isTestMode() && (throwsRandomly != null && throwsRandomly.size() > 0)) {
                    Petra.throwRandomException(throwsRandomly);
                }
                //rollbackHelper.captureListStates(join.getMillisBeforeRetry(), join, as, bs, cs);
                synchronized (this) {
                    transform.accept(as, bs, cs);
                }
                boolean postConditionOk = as.stream().allMatch(join.postA) && bs.stream().allMatch(join.postB) && cs.stream().allMatch(join.postC);
                boolean sideEffectsOk = true;//rollbackHelper.sideEffectsCheck(join, as, bs, cs);
                if (postConditionOk && sideEffectsOk) {

                } else {
                    if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                        if (!postConditionOk && sideEffectsOk) {
                            toWrite.putState(new JoinException(null, new PostConditionFailure()));
                        } else if (postConditionOk && !sideEffectsOk) {
                            toWrite.putState(new JoinException(null, new SideEffectFailure()));
                        } else if (!postConditionOk && !sideEffectsOk) {
                            toWrite.putState(new JoinException(null, new PostConditionFailure(), new SideEffectFailure()));
                        }
                    } else {
                        //rollbackHelper.rollbackListStates(join.getMillisBeforeRetry(), join, as, bs, cs);
                    }
                }
            } catch (Exception e) {
                LOG.error(this.getUniqueId()+"."+join.getJoinClazz().getSimpleName()+"."+index, e);
                if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                    toWrite.putState(new JoinException(null, e));
                } else {
                    //rollbackHelper.rollbackListStates(join.getMillisBeforeRetry(), join, as, bs, cs);
                }
            }
        }
    }

    // PJoin 3 types
    <A, B, C, R, E extends Throwable> void executePureJoin3(boolean joinAll, AbstractPureJoin3 join, int index, Guard<? super A> a, Guard<? super B> b, Guard<? super C> c,
                                                            ITriFunction<List<A>, List<B>, List<C>, R> transform,
                                                            Guard<? super R> predicate, Collection<IToken> toUse, RGraph toWrite, List<Class<? extends Exception>> throwsRandomly) {
        Guard[] Guards = new Guard[3];
        Guards[0] = a;
        Guards[1] = b;
        Guards[2] = c;
        List<List<IToken>> listOfMatches = new PList();
        populateListOfMatchesAndRemoveFromCurrentStates(join, index, Guards, listOfMatches, toUse);
        if ((!joinAll && listOfMatches.stream().flatMap(l->l.stream()).findAny().isPresent()) || (joinAll && listOfMatches.stream().flatMap(l->l.stream()).count()==toUse.size())){
            Triplet<PList<IToken>, PList<IToken>, PList<IToken>> triplet = Triplet.with((PList<IToken>) listOfMatches.get(0), (PList<IToken>) listOfMatches.get(1), (PList<IToken>) listOfMatches.get(2));
            Object value = null;
            PList<A> as = (PList<A>) triplet.getValue0().stream().map(x -> x.getValue()).collect(Collectors.toCollection(() -> new PList<>()));
            PList<B> bs = (PList<B>) triplet.getValue1().stream().map(x -> x.getValue()).collect(Collectors.toCollection(() -> new PList<>()));
            PList<C> cs = (PList<C>) triplet.getValue2().stream().map(x -> x.getValue()).collect(Collectors.toCollection(() -> new PList<>()));
            try {
                if (RGraphComputer.getConfig().isTestMode() && (throwsRandomly != null && throwsRandomly.size() > 0)) {
                    Petra.throwRandomException(throwsRandomly);
                }
                synchronized (this) {
                    value = transform.apply(as, bs, cs);
                }
                if (value == null) {
                    value = vd; // really should throw null point as if they wanted to return Void they would have, so must be a mistake
                }
                boolean postConditionOk = predicate.test((R) value);
                if (postConditionOk) {
                    // remove matches
                    int i = 0;
                    for (List matches : listOfMatches) {
                        i++;
                    }
                    toWrite.deconstruct(new Token(value),t->true);
                } else {
                    if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                        if (!postConditionOk) {
                            toWrite.putState(new JoinException(value, new PostConditionFailure()));
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error(this.getUniqueId()+"."+join.getJoinClazz().getSimpleName()+"."+index, e);
                if (RGraphComputer.getConfig().isExceptionsPassthrough()) {
                    toWrite.putState(new JoinException(null, e));
                }
            }
        }
    }

    private void populateListOfMatchesAndRemoveFromCurrentStates(IJoin join, int index, Guard[] Guards, List<List<IToken>> listOfMatches, Collection<IToken> toUse) {
        for (Guard<?> guard : Guards) {
            boolean copyInputs = guard.operationType!=OperationType.READ_WRITE;
//            if (join instanceof IMaybeEffect) {
//                io.cognitionbox.petra.google.Optional<Class<?>> effectType = ((IMaybeEffect) join).getEffectType();
//                copyInputs = !( effectType.isPresent() &&
//                        guard.getTypeClass().isAssignableFrom(effectType.get()));
//            }
            copyInputs = copyInputs && RGraphComputer.getConfig().isDefensiveCopyAllInputsExceptForEffectedInputs();
            IJoinMatchesProcessor joinMatchesProcessor = new JoinMatchesProcessor();
            List<IToken> matches = joinMatchesProcessor.getMatchesUsingGuard(guard,copyInputs, toUse);
            if (matches != null) {
                listOfMatches.add(matches);
            }
        }
    }

    synchronized private void removeAllStates(List<IToken> matches) {
        matches.stream().forEach(m -> removeState(m));
    }

    private void executeJoin(IRunnable runnable) {
        runnable.run();
    }

    public void putState(Object t) {
        place.addValue(t);
    }

    boolean removeState(IToken t) {
        return place.removeToken(t);
    }

    private Object takeState() {
        Optional<IToken> peek = place.findAny();
        if (peek.isPresent()) {
            removeState(peek.get());
        }
        return peek.get().getValue();
    }

    private boolean isOutputAnnotatedWithExtract(Class<? extends IStep> stepClass) {
        AnnotatedType at = stepClass.getAnnotatedSuperclass();
        if (at instanceof AnnotatedParameterizedType) {
            AnnotatedType[] aTypes = ((AnnotatedParameterizedType) at)
                    .getAnnotatedActualTypeArguments();
            Extract output = aTypes[1].getAnnotation(Extract.class);
            if (output != null) {
                return true;
            }
        }
        return false;
    }

    private boolean isInputAnnotatedWithExtract(Class<? extends IStep> stepClass) {
        AnnotatedType at = stepClass.getAnnotatedSuperclass();
        if (at instanceof AnnotatedParameterizedType) {
            AnnotatedType[] aTypes = ((AnnotatedParameterizedType) at)
                    .getAnnotatedActualTypeArguments();
            Extract input = aTypes[0].getAnnotation(Extract.class);
            if (input != null) {
                return true;
            }
        }
        return false;
    }

    private ExtractedStore extractedStore = new ExtractedStoreImpl();
    private void deconstruct(IToken s, Predicate<IToken> extractIfMatches) {
        Extract extract = s.getValue().getClass().getAnnotation(Extract.class);
        rootValueExtractor.extractToPlace(s,place,extractedStore,extractIfMatches);
        Extract ext = s.getClass().getAnnotation(Extract.class);
        if (ext!=null && !extractedStore.isExtracted(s)){
            place.addValue(s.getValue());
        }
    }
    private void deconstruct(IToken s) {
        deconstruct(s,t->true);
    }

    public RGraph copy() {
        // we dont copy the id as we need a unique id based on the hashcode of the new instance
        RGraph copy = new RGraph(getPartitionKey(), isEffect());
        copy.setEffectType(this.getEffectType()); // so we dont have to re-compute
        copy.setClazz(getStepClazz());
        copy.setP(p());

        // need to copy steps and joins

        // copy steps
        for (IStep edge : parallizable) {
            copy.step(edge);
        }
        // copy joins one by one as with the steps above
        for (int i = 0; i < joins.size(); i++) {
            int iFinal = i;
            copy.addJoin(i, (list, toWrite) -> {
                joins.get(iFinal).accept((Collection<IToken>) list, (RGraph) toWrite);
            });
        }
        returnType.getChoices().forEach(q -> copy.qi(new GuardReturn(q.getTypeClass(), q.predicate)));
        return copy;
    }

    @Override
    public X call() throws Exception {
        initInput();
        return executeMatchingLoopUntilPostCondition();
    }

    public void pi(GuardInput<X> p) {
        setP(p);
    }

    public void pi(Class<X> p, IPredicate<X> predicate) {
        setP(new GuardWrite(p, predicate));
    }

    public void qi(GuardReturn<X> q) {
        returnType.addChoice(new Guard(q.getTypeClass(),q.predicate,OperationType.RETURN));
        setQ(returnType);
    }

    public void qi(Class<X> p, IPredicate<X> predicate) {
        returnType.addChoice(new Guard(p,predicate,OperationType.RETURN));
        setQ(returnType);
    }
}
