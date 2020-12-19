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
package io.cognitionbox.petra.core.impl;

import io.cognitionbox.petra.lang.*;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.lang.annotations.DoesNotTerminate;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.lang.annotations.Feedback;
import io.cognitionbox.petra.core.ILogicBoxDotDiagramRenderer;
import io.cognitionbox.petra.core.IStep;
import org.javatuples.Pair;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class PGraphDotDiagramRendererImpl2 implements ILogicBoxDotDiagramRenderer, Serializable {

  public PGraphDotDiagramRendererImpl2(){
    diagram.append("digraph Petra {\n");
  }
  private StringBuilder diagram = new StringBuilder();
  private Set<String> diagramSet = new HashSet<>();
  public void append(String str, RGraph graph){
    // hack to fix stray Object node
    if (str.split(" ")[0].equals("Object")){
      return;
    }
    if (!diagramSet.contains(str) && !str.contains("EdgeGraph") && !str.contains("Guard")){
      if (str.contains("Void")){
        String v = "Void_"+graph.getPartitionKey();
        String s = str.replaceAll("Void",v);
        diagram.append(s);
        diagramSet.add(s);
        if (graph.getStepClazz().isAnnotationPresent(DoesNotTerminate.class)){
          String cycleBack = v+"->"+graph.getType().getSimpleName()+" [label=cycle];\n";
          if (!diagramSet.contains(cycleBack)){
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

  public void finish(){
    diagram.append("}");
  }

  public String getDotOutput(){
    return diagram.toString();
  }

  private Set<Class<?>> clazzes = new CopyOnWriteArraySet<>();

  private boolean isDefaultAccess(Class<?> clazz){
    return !Modifier.isPrivate(clazz.getModifiers()) &&
            !Modifier.isPublic(clazz.getModifiers()) &&
            !Modifier.isProtected(clazz.getModifiers());
  }

  private void renderAssignableRelations(Set<Class<?>> clazzes, RGraph step){
    for (Class<?> c1 : clazzes){
      for (Class<?> c2 : clazzes){
        if(!c1.equals(c2) && c1.isAssignableFrom(c2)){
          try {
//            c1.getDeclaredMethod(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, c1.getSimpleName()));
//            c2.getDeclaredMethod(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, c2.getSimpleName()));
            append(c2.getSimpleName()+"->"+c1.getSimpleName()+" [label=assignable];\n",step);
          } catch (Exception e) {}
        }
      }
    }
  }

  @Override
  public String render(RGraph logic) {
    List<Pair<List<Guard>, Guard>> joinTypes = new ArrayList<>();
    List<AbstractStep> steps =
            LogicStepsCollector.getAllSteps(logic,joinTypes)
                    .stream()
                    .filter(s->!(s.getType().getSimpleName().equals("ThrowableList") &&
                            s.getType().getSimpleName().equals("ExtractableThrowableList")))
                    .collect(Collectors.toList());

    //collectObjectClasses(steps);

    append("start [shape=circle, style=filled, fillcolor=green];\n",logic);
    append("start->"+logic.getType().getSimpleName()+";\n",logic);

    if (!logic.getKases().isEmpty()){
        append("stop [shape=doubleoctagon, style=filled, fillcolor=red];\n",logic);
        append(logic.getType().getSimpleName()+"->stop;\n",logic);
    }

    // render all steps and states individually
    for (AbstractStep step : steps){
      String stepDesc = ((Identifyable)step).getPartitionKey();
      if (!step.getKases().isEmpty()){
        // diagram.append
        append(step.getType().getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
        append(step.getType()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
//        if (step.p() instanceof ExtractPType){
//          // flip the flow for better visuals
//          append(step.p().getTypeClass().getSimpleName()+"->"+step.p().getTypeClass().getSimpleName()+";\n");
//        } else {
//          append(step.p().getTypeClass().getSimpleName()+"->"+step.p().getTypeClass().getSimpleName()+";\n");
//        }

//        for (Object w : step.p().getWeakerPTypes()){
//          if (w instanceof Guard){
//            append(((Guard) w).getTypeClass().getSimpleName()+"->"+step.p().getTypeClass().getSimpleName()+";\n");
//          }
//        }

      }
      if (!step.getKases().isEmpty()){
        append(step.getType().getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
        append(step.getType().getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
//        if (step.q() instanceof ExtractPType){
//          // flip the flow for better visuals
//          append(step.q().getTypeClass().getSimpleName()+"->"+step.q().getTypeClass().getSimpleName()+";\n");
//        } else {
//          append(step.q().getTypeClass().getSimpleName()+"->"+step.q().getTypeClass().getSimpleName()+";\n");
//        }

//        for (Object w : step.q().getWeakerPTypes()){
//          if (w instanceof Guard){
//            append(((Guard) w).getTypeClass().getSimpleName()+"->"+step.q().getTypeClass().getSimpleName()+";\n");
//          }
//        }

      }
      append(stepDesc+" [shape=rect style=filled, fillcolor=orange];\n",logic);
    }

    for (AbstractStep step : steps){
      String stepDesc = ((Identifyable)step).getPartitionKey();
      if (!step.getKases().isEmpty()){
        append(step.getType().getSimpleName()+"->"+stepDesc+" [label=PRE];\n",logic);
      }

      if (!step.getKases().isEmpty()){
          Class<?> clazz = step.getType();
          clazzes.add(clazz);
          append(clazz.getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
          append(stepDesc+"->"+clazz.getSimpleName()+" [label=POST];\n",logic);

          if (step.getStepClazz().isAnnotationPresent(Feedback.class)){
              append(clazz.getSimpleName()+"->"+step.getType().getSimpleName()+" [label=feedback];\n",logic);
          }
      }
      if (!step.getKases().isEmpty()){
        deconstructType(logic,step.getType());
      }
      if (!step.getKases().isEmpty()){
        deconstructType(logic,step.getType());
      }
    }

    clazzes.remove(Object.class);
    for (Class<?> c : clazzes){
      deconstructType(logic,c);
    }
    renderAssignableRelations(clazzes,logic);

    append(logic.getTransitions(),logic);

    return diagram.toString();
  }

  private void deconstructType(RGraph step, Class<?> clazz){
    deconstruct(step,clazz);
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

  private void deconstruct(RGraph step, Class<?> clazz){

//    if (step instanceof RGraph){
//      for (Object s : ((RGraph) step).getParallizable()){
//        if (s instanceof IStep){
//          if (clazz.isAssignableFrom(((IStep) s).p().getTypeClass())){
//            append(((IStep) s).p().getTypeClass().getSimpleName()+"->"+clazz.getSimpleName()+" [style=dashed, label=subclass];\n");
//          }
//        }
//      }
//    }

    clazzes.add(clazz);
    append(clazz.getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",step);

    if (isAnnotatedWithDeconstruct(clazz) && isCollection(clazz)){
      if (clazz.getGenericSuperclass() instanceof ParameterizedType){
        ParameterizedType type = (ParameterizedType)clazz.getGenericSuperclass();
        Class<?> singular = (Class<?>) type.getActualTypeArguments()[0];
        if (singular!=null){
          append(clazz.getSimpleName()+"->"+singular.getSimpleName()+" [label=extract];\n",step);
        }
        deconstruct(step,singular);
      }
    }
  }
}
