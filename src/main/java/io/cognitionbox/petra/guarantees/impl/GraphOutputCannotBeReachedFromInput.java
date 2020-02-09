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
package io.cognitionbox.petra.guarantees.impl;

import io.cognitionbox.petra.core.impl.*;
import io.cognitionbox.petra.guarantees.GraphCheck;
import io.cognitionbox.petra.lang.*;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.core.IGraph;
import io.cognitionbox.petra.core.IJoin;
import io.cognitionbox.petra.core.IStep;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toSet;

public class GraphOutputCannotBeReachedFromInput implements GraphCheck {

        ReachabilityHelper helper = new ReachabilityHelper();

        private boolean isReachableAccordingToDotDiagram(String dot, Class<?> input, Class<?> output) {
            String dotDiagram = dot;
            dotDiagram = dotDiagram.replaceAll("diagram Petra \\{", "");
            dotDiagram = dotDiagram.replaceAll("\\}", "");
            dotDiagram = dotDiagram.replaceAll("\n", "");
            String[] split = dotDiagram.split(";");
            Set<String> transitions = new HashSet<>();
            for (String l : split) {
                if (l.contains("->")) {
                    String[] type = l.split(" ");
                    String t = type[0];
                    transitions.add(t);
                }
            }
            int count = 0;
            String state = input.getSimpleName();
            while (count <= RGraphComputer.getConfig().getMaxIterations()) {
                for (String t : transitions) {
                    String[] ts = t.split("->");
                    if (state.equals(ts[0])) {
                        state = ts[1];
                    }
                    if (state.equals(output.getSimpleName())) {
                        return true;
                    }
                }
                count++;
            }
            return false;
        }

        boolean matchesState(Guard<?> type, Class<?> state) {
            if (type instanceof GuardXOR<?>) {
                return ((GuardXOR<?>) type).getChoices().stream().anyMatch(c -> c.getTypeClass().isAssignableFrom(state));
            } else {
                return type.getTypeClass().isAssignableFrom(state);
            }
        }

        private int isPostConditionReachable(IGraph<?, ?> step, List<Guard<?>> list) throws PostConditionNotReachable {
            //            PGraphDotDiagramRendererImpl2 renderer = new PGraphDotDiagramRendererImpl2();
//            renderer.render(step);
//            renderer.finish();
//            System.out.println(renderer.getDotOutput());
//
//            if (isReachableAccordingToDotDiagram(renderer.getDotOutput(),step.p().getTypeClass(),step.q().getTypeClass())){
//                return true;
//            } else {
//                return false;
//            }

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

            // update algorithm find all paths to output, so the number of these can be cross checked with all paths to
            // input from the output in the backward reasoning algorithm.

//            Collection<List<AbstractStep>> perms = Collections2.permutations(step.getParallizable());
            int reachable = 0;
//            for (List<AbstractStep> perm : perms){

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
            //outer:
            while (true){//count <= step.getTestModeMaxIterations()) {
                if (((state.size() == 1 && (step.p().getTypeClass().isAssignableFrom(new ArrayList<>(state).get(0)))))) {
                    if (step.p().getTypeClass().isAnnotationPresent(Extract.class) &&
                            step.p().getOperationType() != OperationType.CONSUME) {
                        helper.deconstruct(new HashSet<>(), step.p().getOperationType(), step.p().getTypeClass(), state, 0);
                    }
                }
                //for (Class<?> type : state){
                // run steps
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
                                    // writes dont consume and operate on an instance hence no need to deconstruct the output instance
                                    // as this instance would already be atomic in size and cannot be deconstructed
                                    helper.deconstruct(new HashSet<>(), qs.getOperationType(), qs.getTypeClass(), statesToAdd, 0);
                                }
                            }
                        }
                    }
                }
                state.removeAll(statesToRemove);
                state.addAll(statesToAdd);
                // run joins
//                    for (Pair<List<Guard>, Guard> jt : step.getJoinTypes()){
//                        boolean allMatch = true;
//                        for (Guard t : jt.getValue0()){
//                            allMatch = allMatch && matchesState(t.getTypeClass(),state);
//                        }
//                        if (allMatch){
//                            for (Guard t : jt.getValue0()){
//                                state.remove(t.getTypeClass());
//                            }
//                            addState(jt.getValue().getTypeClass(),state);
//                        }
//                    }

                for (IJoin jt : step.getJoinTypes()) {

//                        ParameterizedType pt = (ParameterizedType) jt.getClass().getGenericSuperclass();
//                        Type[] actualTypeArguments = pt.getActualTypeArguments();
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
                if (lastState!=null && state.equals(lastState)){
                    throw new IllegalStateException("state not changing.");
                }
                lastState = new HashSet<>(state);
                // If marked does not terminate there should be a cycle
                // where the same marking is re-visited
                if (previousStates.contains(state)){
                    if (step.isDoesNotTerminate()){
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
                        // this can generate false positives at the moment.
                        // Need to look at preventing steps and joins from having same
                        // pre/post condition type, i.e. same pre/post condition type class

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
                    //break outer;

                    break;
                }
                //}
                count++;
            }
            //           }

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