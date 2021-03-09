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
package io.cognitionbox.petra.core.impl;

import io.cognitionbox.petra.core.ILogicBoxDotDiagramRenderer;
import io.cognitionbox.petra.lang.AbstractStep;
import io.cognitionbox.petra.lang.Guard;
import io.cognitionbox.petra.lang.RGraph;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.lang.annotations.DoesNotTerminate;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.lang.annotations.Feedback;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class PGraphDotDiagramRendererImpl2 implements ILogicBoxDotDiagramRenderer, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(PGraphDotDiagramRendererImpl2.class);

    public PGraphDotDiagramRendererImpl2() {
        diagram.append("digraph Petra {\n");
    }

    private StringBuilder diagram = new StringBuilder();
    private Set<String> diagramSet = new HashSet<>();

    public void append(String str, RGraph graph) {
        // hack to fix stray Object node
        if (str.split(" ")[0].equals("Object")) {
            return;
        }
        if (!diagramSet.contains(str) && !str.contains("EdgeGraph") && !str.contains("Guard")) {
            if (str.contains("Void")) {
                String v = "Void_" + graph.getPartitionKey();
                String s = str.replaceAll("Void", v);
                diagram.append(s);
                diagramSet.add(s);
                if (graph.getStepClazz().isAnnotationPresent(DoesNotTerminate.class)) {
                    String cycleBack = v + "->" + graph.p().getTypeClass().getSimpleName() + " [label=cycle];\n";
                    if (!diagramSet.contains(cycleBack)) {
                        diagram.append(cycleBack);
                        diagramSet.add(cycleBack);
                    }
                }
            } else {
                diagram.append(str);
                diagramSet.add(str);
            }
        }
    }

    public void finish() {
        diagram.append("}");
    }

    public String getDotOutput() {
        return diagram.toString();
    }

    private Set<Class<?>> clazzes = new CopyOnWriteArraySet<>();

    private void renderAssignableRelations(Set<Class<?>> clazzes, RGraph step) {
        for (Class<?> c1 : clazzes) {
            for (Class<?> c2 : clazzes) {
                if (!c1.equals(c2) && c1.isAssignableFrom(c2)) {
                    try {
                        append(c2.getSimpleName() + "->" + c1.getSimpleName() + " [label=assignable];\n", step);
                    } catch (Exception e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    @Override
    public String render(RGraph logic) {
        List<Pair<List<Guard>, Guard>> joinTypes = new ArrayList<>();
        List<AbstractStep> steps =
                LogicStepsCollector.getAllSteps(logic, joinTypes)
                        .stream()
                        .filter(s -> !(s.p().getTypeClass().getSimpleName().equals("ThrowableList") &&
                                s.q().getTypeClass().getSimpleName().equals("ExtractableThrowableList")))
                        .collect(Collectors.toList());

        append("start [shape=circle, style=filled, fillcolor=green];\n", logic);
        append("start->" + logic.p().getTypeClass().getSimpleName() + ";\n", logic);

        if (logic.q() != null) {
            append("stop [shape=doubleoctagon, style=filled, fillcolor=red];\n", logic);
            if ((logic.q()).isVoid()) {
                append(Void.class.getSimpleName() + "->stop;\n", logic);
            } else {
                append(logic.q().getTypeClass().getSimpleName() + "->stop;\n", logic);
            }
        }

        // render all steps and states individually
        for (AbstractStep step : steps) {
            String stepDesc = step.getPartitionKey();
            if (step.p() != null) {
                // diagram.append
                append(step.p().getTypeClass().getSimpleName() + " [style=filled, fillcolor=blue fontcolor=white];\n", logic);
                append(step.p().getTypeClass().getSimpleName() + " [style=filled, fillcolor=blue fontcolor=white];\n", logic);
            }
            if (step.q() != null) {
                append(step.q().getTypeClass().getSimpleName() + " [style=filled, fillcolor=blue fontcolor=white];\n", logic);
                append(step.q().getTypeClass().getSimpleName() + " [style=filled, fillcolor=blue fontcolor=white];\n", logic);
            }
            append(stepDesc + " [shape=rect style=filled, fillcolor=orange];\n", logic);
        }

        for (AbstractStep step : steps) {
            String stepDesc = ((Identifyable) step).getPartitionKey();
            if (step.p() != null) {
                append(step.p().getTypeClass().getSimpleName() + "->" + stepDesc + " [label=PRE];\n", logic);
            }

            if (step.q() != null) {
                Guard<Object> clazz = step.q();
                clazzes.add(clazz.getTypeClass());
                append(clazz.getTypeClass().getSimpleName() + " [style=filled, fillcolor=blue fontcolor=white];\n", logic);
                append(stepDesc + "->" + clazz.getTypeClass().getSimpleName() + " [label=POST];\n", logic);

                if (step.getStepClazz().isAnnotationPresent(Feedback.class)) {
                    append(clazz.getTypeClass().getSimpleName() + "->" + step.p().getTypeClass().getSimpleName() + " [label=feedback];\n", logic);
                }
            }
            if (step.p() != null) {
                deconstructType(logic, step.p());
            }
            if (step.q() != null) {
                deconstructType(logic, step.q());
            }
        }

        clazzes.remove(Object.class);
        for (Class<?> c : clazzes) {
            deconstructType(logic, c);
        }
        renderAssignableRelations(clazzes, logic);

        append(logic.getTransitions(), logic);

        return diagram.toString();
    }

    private void deconstructType(RGraph step, Class<?> clazz) {
        deconstruct(step, clazz);
    }

    private void deconstructType(RGraph step, Guard<?> some) {
        deconstruct(step, some.getTypeClass());
    }

    private boolean isAnnotatedWithDeconstruct(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Extract.class)) {
            return true;
        }
        return false;
    }

    private boolean isCollection(Class<?> clazz) {
        if (Collection.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    private void deconstruct(RGraph step, Class<?> clazz) {

        clazzes.add(clazz);
        append(clazz.getSimpleName() + " [style=filled, fillcolor=blue fontcolor=white];\n", step);

        if (isAnnotatedWithDeconstruct(clazz) && isCollection(clazz)) {
            if (clazz.getGenericSuperclass() instanceof ParameterizedType) {
                ParameterizedType type = (ParameterizedType) clazz.getGenericSuperclass();
                Class<?> singular = (Class<?>) type.getActualTypeArguments()[0];
                if (singular != null) {
                    append(clazz.getSimpleName() + "->" + singular.getSimpleName() + " [label=extract];\n", step);
                }
                deconstruct(step, singular);
            }
        }
        if (isAnnotatedWithDeconstruct(clazz)) {
            for (Method m : ReflectUtils.getAllMethodsAccessibleFromObject(clazz)) {
                if (m.isAnnotationPresent(Extract.class) &&
                        m.getParameterCount() == 0 &&
                        Modifier.isPublic(m.getModifiers())) {
                    append(clazz.getSimpleName() + "->" + m.getReturnType().getSimpleName() + " [label=extract];\n", step);
                    deconstruct(step, m.getReturnType());
                }
            }
        }
    }
}
