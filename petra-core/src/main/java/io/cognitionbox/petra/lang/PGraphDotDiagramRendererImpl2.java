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

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.core.ILogicBoxDotDiagramRenderer;
import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.core.impl.LogicStepsCollector;
import io.cognitionbox.petra.core.impl.ReflectUtils;
import io.cognitionbox.petra.lang.annotations.DoesNotTerminate;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.lang.annotations.Feedback;
import org.javatuples.Pair;

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
                    String cycleBack = v + "->" + graph.getType().getSimpleName() + " [label=cycle];\n";
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

    private boolean isDefaultAccess(Class<?> clazz) {
        return !Modifier.isPrivate(clazz.getModifiers()) &&
                !Modifier.isPublic(clazz.getModifiers()) &&
                !Modifier.isProtected(clazz.getModifiers());
    }

    private void renderAssignableRelations(Set<Class<?>> clazzes, RGraph step) {
        for (Class<?> c1 : clazzes) {
            for (Class<?> c2 : clazzes) {
                if (!c1.equals(c2) && c1.isAssignableFrom(c2)) {
                    try {
//            c1.getDeclaredMethod(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, c1.getSimpleName()));
//            c2.getDeclaredMethod(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, c2.getSimpleName()));
                        append(c2.getSimpleName() + "->" + c1.getSimpleName() + " [label=assignable];\n", step);
                    } catch (Exception e) {
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
                        .filter(s -> !(s.getType().getSimpleName().equals("ThrowableList") &&
                                s.q().getTypeClass().getSimpleName().equals("ExtractableThrowableList")))
                        .collect(Collectors.toList());

        //collectObjectClasses(steps);

//        append("start [shape=circle, style=filled, fillcolor=green];\n", logic);
//        append("start->" + logic.getStepClazz().getSimpleName() + "_"+logic.getType().getSimpleName()+" [shape=rect style=filled, fillcolor=orange];\n", logic);
//        append("stop [shape=doubleoctagon, style=filled, fillcolor=red];\n", logic);
//
//        Pair<ExecMode, List<TransformerStep>> firstPair = (Pair<ExecMode, List<TransformerStep>>) logic.allSteps.get(0);
//        List<TransformerStep> firstSteps = firstPair.getValue1();
//        if (firstPair.getValue0().isSEQ()){
//            append(logic.getStepClazz().getSimpleName() + "_"+logic.getType().getSimpleName() + "->" + firstSteps.get(0).getStep().getStepClazz().getSimpleName() + "_"+firstSteps.get(0).getStep().getType().getSimpleName() +"[label=then];\n", logic);
//        } else if (firstPair.getValue0().isPAR() || firstPair.getValue0().isCHOICE()){
//            for (int j=0;j<firstSteps.size();j++){
//                append(logic.getStepClazz().getSimpleName() + "_"+logic.getType().getSimpleName() + "->" + firstSteps.get(0).getStep().getStepClazz().getSimpleName() + "_"+firstSteps.get(0).getStep().getType().getSimpleName() +"[label=then];\n", logic);
//            }
//        }
//        for (int i=0;i<logic.allSteps.size();i++){
//            Pair<ExecMode, List<TransformerStep>> currentPair = (Pair<ExecMode, List<TransformerStep>>) logic.allSteps.get(i);
//            Pair<ExecMode, List<TransformerStep>> lastPair = null;
//            if (i>0){
//                lastPair = (Pair<ExecMode, List<TransformerStep>>) logic.allSteps.get(i-1);
//            }
//            if (i==logic.allSteps.size()-1){
//                for (int j=0;j<currentPair.getValue1().size();j++){
//                    if (j==currentPair.getValue1().size()-1){
//                        append(currentPair.getValue1().get(j).getStep().getStepClazz().getSimpleName() + "_"+currentPair.getValue1().get(j).getStep().getType().getSimpleName() + "->stop [label=then];\n", logic);
//                    } else {
//                        if (currentPair.getValue0().isSEQ()){
//                            append(currentPair.getValue1().get(j).getStep().getStepClazz().getSimpleName() + "_"+currentPair.getValue1().get(j).getStep().getType().getSimpleName() + "->" + currentPair.getValue1().get(j+1).getStep().getStepClazz().getSimpleName() + "_"+currentPair.getValue1().get(j+1).getStep().getType().getSimpleName() +"[label=then];\n", logic);
//                        } else if (firstPair.getValue0().isPAR() || firstPair.getValue0().isCHOICE()){
//                            if (lastPair!=null){
//                                append(lastPair.getValue1().get(lastPair.getValue1().size()-1).getStep().getStepClazz().getSimpleName() + "_"+currentPair.getValue1().get(j).getStep().getType().getSimpleName() + "->" + firstSteps.get(0).getStep().getStepClazz().getSimpleName() + "_"+firstSteps.get(0).getStep().getType().getSimpleName() +"[label=then];\n", logic);
//                            }
//                        }
//                    }
//                }
//            } else {
//                if (currentPair.getValue0().isSEQ()) {
//                    for (int j=0;j<currentPair.getValue1().size();j++){
//                        if (currentPair.getValue1().size()>1){
//                            append(currentPair.getValue1().get(j).getStep().getStepClazz().getSimpleName() + "_"+currentPair.getValue1().get(j).getStep().getType().getSimpleName() + "->" + currentPair.getValue1().get(j+1).getStep().getStepClazz().getSimpleName() + "_"+currentPair.getValue1().get(j+1).getStep().getType().getSimpleName() +"[label=then];\n", logic);
//                        }
//                    }
//                } else if (currentPair.getValue0().isPAR() || currentPair.getValue0().isCHOICE()){
//                    for (int j=0;j<currentPair.getValue1().size();j++){
//                        if (lastPair!=null){
//                            append(lastPair.getValue1().get(lastPair.getValue1().size()-1).getStep().getStepClazz().getSimpleName() + "_"+lastPair.getValue1().get(lastPair.getValue1().size()-1).getStep().getType().getSimpleName() + "->" + currentPair.getValue1().get(0).getStep().getStepClazz().getSimpleName() + "_"+currentPair.getValue1().get(0).getStep().getType().getSimpleName() +"[label=then];\n", logic);
//                        }
//                    }
//                }
//            }
//        }

        for (AbstractStep step : steps) {
            String stepDesc = step.getStepClazz().getSimpleName() + "_"+logic.getType().getSimpleName();

            if (step.q() != null) {
                Guard<Object> clazz = step.q();
                clazzes.add(clazz.getTypeClass());
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

//  private EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
//          .seed(123L)
//          .objectPoolSize(100)
//          .randomizationDepth(3)
//          .stringLengthRange(5, 50)
//          .collectionSizeRange(1, 10)
//          .scanClasspathForConcreteTypes(true)
//          .overrideDefaultInitialization(false)
//          .build();

    private void deconstruct(RGraph step, Class<?> clazz) {

//    if (step instanceof RGraph){
//      for (Object s : ((RGraph) step).getParallizable()){
//        if (s instanceof IStep){
//          if (clazz.isAssignableFrom(((IStep) s).getType())){
//            append(((IStep) s).getType().getSimpleName()+"->"+clazz.getSimpleName()+" [style=dashed, label=subclass];\n");
//          }
//        }
//      }
//    }

        clazzes.add(clazz);

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
