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

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.core.IEdgeDotLogger;
import io.cognitionbox.petra.core.IGraph;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.core.engine.StepCallable;
import io.cognitionbox.petra.core.engine.StepResult;
import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.core.engine.petri.Place;
import io.cognitionbox.petra.core.engine.petri.impl.Token;
import io.cognitionbox.petra.core.impl.OperationType;
import io.cognitionbox.petra.core.impl.PEdgeDotLoggerImpl;
import io.cognitionbox.petra.exceptions.GraphException;
import io.cognitionbox.petra.exceptions.PetraException;
import io.cognitionbox.petra.lang.annotations.DoesNotTerminate;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.util.Petra;
import io.cognitionbox.petra.util.function.*;
import io.cognitionbox.petra.util.impl.PList;
import org.javatuples.Triplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class RGraph<X extends D,D> extends AbstractStep<X> implements IGraph<X> {

    final static Logger LOG = LoggerFactory.getLogger(RGraph.class);
    List<StepCallable> callables = new ArrayList<>();
    private boolean doesNotTerminate = this.getStepClazz().isAnnotationPresent(DoesNotTerminate.class);
    private List<IStep> parallizable = new ArrayList<>();
    private transient IEdgeDotLogger stepDotLogger = new PEdgeDotLoggerImpl();
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

    public RGraph() {
        init();
    }

    public RGraph(String description) {
        super(description);
        init();
    }

    public boolean isDoesNotTerminate() {
        return doesNotTerminate;
    }

    void initInput(){
        if (this.p().getTypeClass().isAnnotationPresent(Extract.class)){
            deconstruct(getInput());
            putState(getInput().getValue());
        } else {
            putState(getInput().getValue());
        }
    }

    private IPredicate<X> loopCondition = x->true;

    // this.loopCondition.test(getInput().getValue()) &&
    public void loop(IPredicate<X> loopCondition) {
        this.loopCondition = loopCondition;
    }

    private PollingTimer iterationTimer;

    X executeMatchingLoopUntilPostCondition() {
        currentIteration = 0;
        X out = null;

        boolean doesNotTerminate = getStepClazz().isAnnotationPresent(DoesNotTerminate.class);
        // use in while loop to prevent termination.
        while (this.getStepClazz().isAnnotationPresent(DoesNotTerminate.class) ||
                (!this.q().test(getInput().getValue())) ) {
            iterationId.getAndIncrement();
            currentIteration++;
            if (iterationTimer!=null && !iterationTimer.periodHasPassed(LocalDateTime.now())){
                continue;
            }
            try {
                Lg();
                iteration();
                List<Throwable> exceptions = exceptions();
                if (!exceptions.isEmpty()) {
                    return (X) new GraphException(this,(X) this.getInput().getValue(), null, exceptions);
                }
                // breach of loop invariant i.e. pre invariant
//                if (!this.p().test(getInput().getValue())) {
//                    return (X) new GraphException(this,(X) this.getInput().getValue(), null, Arrays.asList(new IllegalStateException("invariant broken.")));
//                }

                // post con check for non terminating processes
                if (this.getStepClazz().isAnnotationPresent(DoesNotTerminate.class) &&
                        !this.q().test(getInput().getValue())) {
                    return (X) new GraphException(this,(X) this.getInput().getValue(), null, Arrays.asList(new IllegalStateException("cycle not correct.")));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
//        if (out!=null){
//            return out;
//        }
//        // loop terminated before post condition reached
//        throw new PostConditionFailure();

        return getInput().getValue();
    }

    void iteration(){
//        if (currentIteration > getMaxIterations()) {
//            // if we go past the max iterations, we have failed to meet the post cons
//            putState(new IterationsTimeoutException());
//        } else {

//            if (sleepPeriod>0) {
//                sleep(sleepPeriod);
//            }

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
                place.reset();
                //if (p().getTypeClass().isAnnotationPresent(Extract.class)){
                    deconstruct(getInput());
                //}
//            }

            //De();
            Ex();
            Jn();
//            Object toReturn = Rt();
//            if (toReturn != null) {
//                return (X) toReturn;
//            } else {
//                return null;
//            }

        //  }
    }

    public void step(Class<? extends IStep<X>> computation) {
        step(x->x,Petra.createStep(computation));
    }

//    public void step(IStep<? super I> computation) {
//        addParallizable(computation);
//    }


    private List<StateTransformerStep> stateTransformerSteps = new ArrayList<>();
    private List<StateIterableTransformerStep> stateIterableTransformerSteps = new ArrayList<>();
    private List<StateTransformerStep> parTransformerSteps = new ArrayList<>();
    private List<StateIterableTransformerStep> parIterableTransformerSteps = new ArrayList<>();
    private List<StateTransformerStep> seqTransformerSteps = new ArrayList<>();
    private List<StateIterableTransformerStep> seqIterableTransformerSteps = new ArrayList<>();

    public List<StateTransformerStep> getSeqTransformerSteps() {
        return seqTransformerSteps;
    }

    public List<StateIterableTransformerStep> getSeqIterableTransformerSteps() {
        return seqIterableTransformerSteps;
    }

    public List<StateTransformerStep> getTransformerSteps() {
        return stateTransformerSteps;
    }

    public List<StateIterableTransformerStep> getForallTransformerSteps() {
        return stateIterableTransformerSteps;
    }

//    private TransformerStep currentStep = null;
//
//    public void par(){
//        stateIterableTransformerSteps.remove(currentStep);
//        parTransformerSteps.remove(currentStep);
//        if (currentStep instanceof StateIterableTransformerStep){
//            stateIterableTransformerSteps.add((StateIterableTransformerStep) currentStep);
//            parIterableTransformerSteps.add((StateIterableTransformerStep) currentStep);
//        } else if (currentStep instanceof StateTransformerStep){
//            stateTransformerSteps.add((StateTransformerStep) currentStep);
//            parTransformerSteps.add((StateTransformerStep) currentStep);
//        }
//    }
//
//    public void seq(){
//        stateIterableTransformerSteps.remove(currentStep);
//        seqTransformerSteps.remove(currentStep);
//        if (currentStep instanceof StateIterableTransformerStep){
//            stateIterableTransformerSteps.add((StateIterableTransformerStep) currentStep);
//            seqIterableTransformerSteps.add((StateIterableTransformerStep) currentStep);
//        } else if (currentStep instanceof StateTransformerStep){
//            stateTransformerSteps.add((StateTransformerStep) currentStep);
//            seqTransformerSteps.add((StateTransformerStep) currentStep);
//        }
//    }

    public <P> void stepForall(IFunction<X,Iterable<P>> transformer, IStep<P> step){
        stepForall(transformer,step,ExecMode.PAR);
    }
    public <P> void stepForall(IFunction<X,Iterable<P>> transformer, IStep<P> step, ExecMode execMode){
        StateIterableTransformerStep currentStep = new StateIterableTransformerStep(transformer, (AbstractStep) step);
        stateIterableTransformerSteps.add((StateIterableTransformerStep) currentStep);
        //parIterableTransformerSteps.add((StateIterableTransformerStep) currentStep);
        if (execMode.isSEQ()){
            seqIterableTransformerSteps.add(currentStep);
        } else if (execMode.isPAR()){
            parIterableTransformerSteps.add(currentStep);
        }
        addParallizable(step);
    }
    public <P> void step(IFunction<X,P> transformer, IStep<P> step){
        step(transformer,step,ExecMode.PAR);
    }
    public <P> void step(IFunction<X,P> transformer, IStep<P> step, ExecMode execMode){
        StateTransformerStep currentStep = new StateTransformerStep(transformer, (AbstractStep) step);
        stateTransformerSteps.add((StateTransformerStep) currentStep);
        //parTransformerSteps.add((StateTransformerStep) currentStep);
        if (execMode.isSEQ()){
            seqTransformerSteps.add(currentStep);
        } else if (execMode.isPAR()){
            parTransformerSteps.add(currentStep);
        }
        addParallizable(step);
    }
    private void executeSeqSteps(){
        for (StateTransformerStep<X,?> f : seqTransformerSteps){
            try {
                Object value = f.getTransformer().apply(getInput().getValue());
                if (f.getStep().evalP(value)){
                    AbstractStep copy = f.getStep().copy();
                    copy.setInput(new Token(value));
                    StepCallable stepCallable = new StepCallable(this,copy);
                    StepResult sr = stepCallable.call();
                    if (sr.getOutputValue().getValue() instanceof Throwable){
                        this.place.addValue(sr.getOutputValue().getValue());
                    }
                    if (OperationType.READ_WRITE != sr.getOperationType()) {
                        deconstruct(sr.getOutputValue());
                        //putState(f.get().getOutputValue());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void prepareParSteps(){
        for (StateTransformerStep<X,?> f : parTransformerSteps){
            try {
                Object value = f.getTransformer().apply(getInput().getValue());
                if (f.getStep().evalP(value)){
                    AbstractStep copy = f.getStep().copy();
                    copy.setInput(new Token(value));
                    collectCallable(new StepCallable(this,copy), callables);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void executeSeqStepForalls(){
        for (StateIterableTransformerStep<X,?> s : seqIterableTransformerSteps){
            boolean ok = true;
            Iterable<?> iterable = s.getTransformer().apply(getInput().getValue());
            for(Object o : iterable){
                if (!s.getStep().p().test(o)){
                    ok = false;
                    break;
                }
            }
            iterable = s.getTransformer().apply(getInput().getValue());
            // if all match run steps against the elements
            List<StepCallable> callables = new ArrayList<>();
            if (ok){
                for(Object o : iterable){
                    try {
                        AbstractStep copy = s.getStep().copy();
                        copy.setInput(new Token(o));
                        collectCallable(new StepCallable(this,copy), callables);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (RGraphComputer.getConfig().getMode().isSEQ()){
                        for (StepCallable callable : callables){
                            StepResult sr = callable.call();
                            if (sr.getOutputValue().getValue() instanceof Throwable){
                                this.place.addValue(sr.getOutputValue().getValue());
                            }
                            if (OperationType.READ_WRITE != sr.getOperationType()) {
                                deconstruct(sr.getOutputValue());
                                //putState(f.get().getOutputValue());
                            }
                        }
                    } else {
                        List<Future<StepResult>> futures = RGraphComputer.getWorkerExecutor().invokeAll(callables);
                        for (Future<StepResult> f : futures){
                            StepResult sr = f.get();
                            if (sr.getOutputValue().getValue() instanceof Throwable){
                                this.place.addValue(sr.getOutputValue().getValue());
                            }
                            if (OperationType.READ_WRITE != sr.getOperationType()) {
                                deconstruct(sr.getOutputValue());
                                //putState(f.get().getOutputValue());
                            }
                        }
                    }
                } catch (Throwable e){

                }
            }
        }
    }
    private void prepareParStepForalls(){
        for (StateIterableTransformerStep<X,?> f : parIterableTransformerSteps){
            boolean ok = true;
            Iterable<?> iterable = f.getTransformer().apply(getInput().getValue());
            for(Object o : iterable){
                if (!f.getStep().p().test(o)){
                    ok = false;
                    break;
                }
            }
            iterable = f.getTransformer().apply(getInput().getValue());
            // if all match run steps against the elements
            if (ok){
                for(Object o : iterable){
                    try {
                        AbstractStep copy = f.getStep().copy();
                        copy.setInput(new Token(o));
                        collectCallable(new StepCallable(this,copy), callables);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void step(IStep<X> computation) {
        step(x->x,computation);
    }

    private Set<Class<?>> deconstructable = new HashSet<>();
    public  <T> void decon(Class<T>... types) {
        deconstructable.addAll(Arrays.asList(types));
        // allows the extract to happen in the graph, kind of like scoping.
        // decons one level
    }

    public List<IStep> getParallizable() {
        return parallizable;
    }

    @Override
    public int getNoOfSteps() {
        return stateTransformerSteps.size()+stateIterableTransformerSteps.size();
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
                    if (sr.getOutputValue().getValue() instanceof Throwable){
                        this.place.addValue(sr.getOutputValue().getValue());
                    }
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
                    StepResult sr = f.get();
                    if (sr.getOutputValue().getValue() instanceof Throwable){
                        this.place.addValue(sr.getOutputValue().getValue());
                    }
                    if (OperationType.READ_WRITE != sr.getOperationType()) {
                        deconstruct(sr.getOutputValue());
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
        prepareParSteps();
        prepareParStepForalls();
//        for (int index = 0; index < steps.size(); index = index + 1) {
//            IStep c = steps.get(index);
//            for (Object token : place.filterTokensByValue(c.p())) {
//                AbstractStep copy = null;
//                if (c instanceof AbstractStep) {
//                    copy = ((AbstractStep) c).copy();
//                }
//                copy.setInput((IToken) token);
//                collectCallable(new StepCallable(this,copy), callables);
//            }
//        }
        forkAndJoinCallables(callables);
        executeSeqSteps();
        executeSeqStepForalls();
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

    private List<Throwable> exceptions() {
        return this.getPlace()
                .stream()
                .filter(s -> s.getValue() instanceof Throwable)
                .map(s -> (PetraException) s.getValue())
                .collect(Collectors.toCollection(() -> new PList<>()));
    }

    X Rt() {
        List<Throwable> exceptions = exceptions();
        if (!exceptions.isEmpty()) {
            for (Throwable throwable : exceptions) {
//                throwable.printStackTrace();
//                for (Throwable t : throwable.getCauses()) {
//                    t.printStackTrace();
//                }
            }
            return (X) new GraphException(this,(X) this.getInput().getValue(), null, exceptions);
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
        this.iterationTimer = new PollingTimer((int) (this.sleepPeriod/1000),false);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e1) {
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

    private void deconstruct(IToken s) {
        putState(s.getValue());
    }

    public RGraph copy() {
        // we dont copy the id as we need a unique id based on the hashcode of the new instance
        RGraph copy = new RGraph(getPartitionKey());
        copy.setEffectType(this.getEffectType()); // so we dont have to re-compute
        copy.setClazz(getStepClazz());
        copy.setP(p());

        // copy steps
        copy.stateTransformerSteps.addAll(stateTransformerSteps);
        copy.stateIterableTransformerSteps.addAll(stateIterableTransformerSteps);

        copy.seqTransformerSteps.addAll(seqTransformerSteps);
        copy.seqIterableTransformerSteps.addAll(seqIterableTransformerSteps);

        copy.parTransformerSteps.addAll(parTransformerSteps);
        copy.parIterableTransformerSteps.addAll(parIterableTransformerSteps);

        // copy joins one by one as with the steps above
        for (int i = 0; i < joins.size(); i++) {
            int iFinal = i;
            copy.addJoin(i, (list, toWrite) -> {
                joins.get(iFinal).accept((Collection<IToken>) list, (RGraph) toWrite);
            });
        }
        returnType.getChoices().forEach(q -> copy.post(new GuardReturn(q.getTypeClass(), q.predicate)));
        return copy;
    }

    @Override
    public X call() throws Exception {
        initInput();
        return executeMatchingLoopUntilPostCondition();
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
}
