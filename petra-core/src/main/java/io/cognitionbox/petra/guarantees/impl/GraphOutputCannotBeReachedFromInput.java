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
package io.cognitionbox.petra.guarantees.impl;

import io.cognitionbox.petra.core.IGraph;
import io.cognitionbox.petra.core.IJoin;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.core.impl.OperationType;
import io.cognitionbox.petra.core.impl.ReachabilityHelper;
import io.cognitionbox.petra.guarantees.GraphCheck;
import io.cognitionbox.petra.lang.*;
import io.cognitionbox.petra.lang.annotations.Extract;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toSet;

public class GraphOutputCannotBeReachedFromInput implements GraphCheck {

    ReachabilityHelper helper = new ReachabilityHelper();

    boolean matchesState(Guard<?> type, Class<?> state) {
        if (type instanceof GuardXOR<?>) {
            return ((GuardXOR<?>) type).getChoices().stream().anyMatch(c -> c.getTypeClass().isAssignableFrom(state));
        } else {
            return type.getTypeClass().isAssignableFrom(state);
        }
    }

    private int isPostConditionReachable(IGraph<?, ?> step, List<Guard<?>> list) throws PostConditionNotReachable {

        Set<Class<?>> state = new CopyOnWriteArraySet<>();

        Set<Set<Class<?>>> previousStates = new HashSet<>();
        Set<Class<?>> lastState = null;

        Class<?> input = step.p().getTypeClass();
        if (step.p().getTypeClass().isAnnotationPresent(Extract.class)) {
            helper.deconstruct(new HashSet<>(), step.p().getOperationType(), step.p().getTypeClass(), state, 0);
            if (step.p().getOperationType() != OperationType.CONSUME) {
                state.add(input);
            }
        } else {
            state.add(input);
        }

        int reachable = 0;
        Map<IStep, AtomicInteger> stepsVisitCount = new ConcurrentHashMap<>();
        Map<IJoin, AtomicInteger> joinsVisitCount = new ConcurrentHashMap<>();
        for (IStep s : step.getParallizable()) { //perm
            stepsVisitCount.put(s, new AtomicInteger(0));
        }
        for (IJoin j : step.getJoinTypes()) {
            joinsVisitCount.put(j, new AtomicInteger(0));
        }

        if (RGraphComputer.getConfig().isStrictModeExtraConstructionGuarantee()) {
            for (Class<?> s : state) {
                int matches = 0;
                for (IStep<?, ?> stp : step.getParallizable()) {
                    if (stp.p().getTypeClass().isAssignableFrom(s)) {
                        matches++;
                        if (matches > 1) {
                            throw new IllegalStateException("pre condition conflicts, pre conditions compete for same type");
                        }
                    }
                }
            }
        }

        int count = 0;
        while (true) {
            if (((state.size() == 1 && (step.p().getTypeClass().isAssignableFrom(new ArrayList<>(state).get(0)))))) {
                if (step.p().getTypeClass().isAnnotationPresent(Extract.class) &&
                        step.p().getOperationType() != OperationType.CONSUME) {
                    helper.deconstruct(new HashSet<>(), step.p().getOperationType(), step.p().getTypeClass(), state, 0);
                }
            }
            Set<Class<?>> statesToRemove = new HashSet<>();
            Set<Class<?>> statesToAdd = new HashSet<>();
            for (IStep<?, ?> stp : step.getParallizable()) { //perm
                for (Class<?> st : state) {
                    if (stp.p().getTypeClass().isAssignableFrom(st)) {
                        stepsVisitCount.get(stp).incrementAndGet();

                        // if consume or write need to replace the input type with output type
                        // if read just add the output type and leave the input type
                        // it works differently to for equivalent semantics in execution runtime where we
                        // do not remove the state for effects.
                        if (stp.p().getOperationType() == OperationType.CONSUME ||
                                stp.p().getOperationType() == OperationType.WRITE) {
                            statesToRemove.add(st);
                        }
                        if (stp.q() instanceof GuardXOR<?>) {
                            for (Guard<?> qs : ((GuardXOR<?>) stp.q()).getChoices()) {
                                // writes don't consume and operate on an instance hence no need to deconstruct the output instance
                                // as this instance would already be atomic in size and cannot be deconstructed
                                helper.deconstruct(new HashSet<>(), qs.getOperationType(), qs.getTypeClass(), statesToAdd, 0);
                            }
                        }
                    }
                }
            }
            state.removeAll(statesToRemove);
            state.addAll(statesToAdd);

            for (IJoin jt : step.getJoinTypes()) {
                Guard[] actualTypeArguments = null;
                if (jt instanceof PJoin) {
                    actualTypeArguments = new Guard<?>[2];
                    actualTypeArguments[0] = ((PJoin) jt).a();
                    actualTypeArguments[1] = ((PJoin) jt).r();
                } else if (jt instanceof PJoin2) {
                    actualTypeArguments = new Guard<?>[3];
                    actualTypeArguments[0] = ((PJoin2) jt).a();
                    actualTypeArguments[1] = ((PJoin2) jt).b();
                    actualTypeArguments[2] = ((PJoin2) jt).r();
                } else if (jt instanceof PJoin3) {
                    actualTypeArguments = new Guard<?>[4];
                    actualTypeArguments[0] = ((PJoin3) jt).a();
                    actualTypeArguments[1] = ((PJoin3) jt).b();
                    actualTypeArguments[2] = ((PJoin3) jt).c();
                    actualTypeArguments[3] = ((PJoin3) jt).r();
                }

                int matches = 0;
                for (int i = 0; i < actualTypeArguments.length - 1; i++) {
                    Guard t = actualTypeArguments[i];
                    for (Class<?> c : state) {
                        if (matchesState(t, c)) {
                            matches++;
                        }
                    }
                }
                boolean allMatch = matches == actualTypeArguments.length - 1;
                if (allMatch) {
                    joinsVisitCount.get(jt).incrementAndGet();
                    for (int i = 0; i < actualTypeArguments.length - 1; i++) {
                        Guard t = actualTypeArguments[i];
                        Class<?> tOut = t.getTypeClass();
                        for (Class<?> s : state) {
                            if (tOut.isAssignableFrom(s) &&
                                    (t.getOperationType() == OperationType.CONSUME || t.getOperationType() == OperationType.WRITE)) {
                                state.remove(s);
                            }
                        }
                    }
                    Guard r = actualTypeArguments[actualTypeArguments.length - 1];
                    Class<?> rOut = r.getTypeClass();

                    helper.deconstruct(new HashSet<>(), r.getOperationType(), rOut, state, 0);
                }
            }
            if (lastState != null && state.equals(lastState)) {
                throw new IllegalStateException("state not changing.");
            }
            lastState = new HashSet<>(state);
            // If marked does not terminate there should be a cycle
            // where the same marking is re-visited
            if (previousStates.contains(state)) {
                if (step.isDoesNotTerminate()) {
                    reachable++;
                    return reachable;
                } else {
                    throw new IllegalStateException("cycle detected, but expected to terminate.");
                }
            } else {
                previousStates.add(new HashSet<>(state));
            }
            if ((state.size() == 0 && step.q().isVoid())
                    || (state.size() == 1 && step.q() != null && matchesState(step.q(), new ArrayList<>(state).get(0)))) {

                if (RGraphComputer.getConfig().isStrictModeExtraConstructionGuarantee()) {
                    Set<IStep> deadSteps = stepsVisitCount.entrySet().stream()
                            .filter(e -> e.getValue().get() == 0).map(e -> e.getKey()).collect(toSet());
                    Set<IJoin> deadJoins = joinsVisitCount.entrySet().stream()
                            .filter(e -> e.getValue().get() == 0).map(e -> e.getKey()).collect(toSet());
                    if (deadSteps.size() > 0) {
                        throw new DeadStepsExist(deadSteps);
                    }
                    if (deadJoins.size() > 0) {
                        throw new DeadJoinsExist(deadJoins);
                    }
                }
                reachable++;
                break;
            }
            count++;
        }

        if (reachable == 0) {
            throw new PostConditionNotReachable(step, state);
        } else {
            return reachable;
        }
    }

    @Override
    public boolean test(IGraph<?, ?> step) {

        try {
            //runAllChecksExceptForThoseNotCompatibleWithGraphsGeneratedByReachabilityCheck(new ArrayList<>(), step);
            Integer reachable = isPostConditionReachable(step, null);
            if (reachable > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //LOG.error(printErrorDotDiagram(step));
            return false;
        }
    }
}

class DeadStepsExist extends RuntimeException {
    private Set<IStep> deadSteps;

    DeadStepsExist(Set<IStep> deadSteps) {
        this.deadSteps = deadSteps;
    }

    @Override
    public String toString() {
        return "DeadStepsExist{" +
                "deadSteps=" + deadSteps +
                '}';
    }
}

class DeadJoinsExist extends RuntimeException {
    private Set<IJoin> deadJoins;

    DeadJoinsExist(Set<IJoin> deadJoins) {
        this.deadJoins = deadJoins;
    }

    @Override
    public String toString() {
        return "DeadJoinsExist{" +
                "deadJoins=" + deadJoins +
                '}';
    }
}

class PostConditionNotReachable extends Exception {
    private IGraph PRestrictedGraph;
    private Set<Class<?>> remainingStates;

    PostConditionNotReachable(IGraph PRestrictedGraph, Set<Class<?>> remainingStates) {
        this.PRestrictedGraph = PRestrictedGraph;
        this.remainingStates = remainingStates;
    }

    @Override
    public String toString() {
        return "PostConditionNotReachable{" +
                "RGraph=" + PRestrictedGraph +
                ", remainingStates=" + remainingStates +
                '}';
    }
}