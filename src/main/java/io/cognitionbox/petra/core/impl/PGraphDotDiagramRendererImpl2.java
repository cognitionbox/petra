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
package io.cognitionbox.petra.core.impl;

import io.cognitionbox.petra.lang.*;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.lang.annotations.DoesNotTerminate;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.lang.annotations.Feedback;
import io.cognitionbox.petra.core.IJoin;
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
          String cycleBack = v+"->"+graph.p().getTypeClass().getSimpleName()+" [label=cycle];\n";
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
                    .filter(s->!(s.p().getTypeClass().getSimpleName().equals("ThrowableList") &&
                            s.q().getTypeClass().getSimpleName().equals("ExtractableThrowableList")))
                    .collect(Collectors.toList());

    //collectObjectClasses(steps);

    append("start [shape=circle, style=filled, fillcolor=green];\n",logic);
    append("start->"+logic.p().getTypeClass().getSimpleName()+";\n",logic);

    if (logic.q()!=null){
      append("stop [shape=doubleoctagon, style=filled, fillcolor=red];\n",logic);
      if ((logic.q()).isVoid()){
        append(Void.class.getSimpleName()+"->stop;\n",logic);
      } else {
        for (Guard<?> q : ((GuardXOR<?>) logic.q()).getChoices()){
          append(q.getTypeClass().getSimpleName()+"->stop;\n",logic);
        }
      }
    }

    // render all steps and states individually
    for (AbstractStep step : steps){
      String stepDesc = ((Identifyable)step).getPartitionKey();
      if (step.p()!=null){
        // diagram.append
        append(step.p().getTypeClass().getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
        append(step.p().getTypeClass().getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
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
      if (step.q()!=null){
        append(step.q().getTypeClass().getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
        append(step.q().getTypeClass().getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
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
      if (step.p()!=null){
        append(step.p().getTypeClass().getSimpleName()+"->"+stepDesc+" [label=PRE];\n",logic);
      }

      if (step.q()!=null){
        if (step.q() instanceof GuardXOR<?>){
          for (Guard<?> clazz : ((GuardXOR<?>) step.q()).getChoices()){
            clazzes.add(clazz.getTypeClass());
            append(clazz.getTypeClass().getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
            append(stepDesc+"->"+clazz.getTypeClass().getSimpleName()+" [label=POST];\n",logic);

            if (step.getStepClazz().isAnnotationPresent(Feedback.class)){
              append(clazz.getTypeClass().getSimpleName()+"->"+step.p().getTypeClass().getSimpleName()+" [label=feedback];\n",logic);
            }
          }
        }
      }
      if (step.p()!=null){
        deconstructType(logic,step.p());
      }
      if (step.q()!=null){
        deconstructType(logic,step.q());
      }
    }

    for (IStep step : steps){
      if (step instanceof RGraph){
        String stepDesc = ((Identifyable)step).getPartitionKey();
        int joinNo = 0;
        for (Object jt : ((RGraph) step).getJoinTypes()){
          if (jt instanceof IJoin){ // jt instanceof Pair

//            ParameterizedType pt = (ParameterizedType) jt.getClass().getGenericSuperclass();
//            Type[] actualTypeArguments = pt.getActualTypeArguments();
            Type[] actualTypeArguments = null;
            Guard[] Guards = null;
            if (jt instanceof PJoin){
              actualTypeArguments = new Class<?>[2];
              actualTypeArguments[0] = ((PJoin) jt).a().getTypeClass();
              actualTypeArguments[1] = ((PJoin) jt).r().getTypeClass();

              Guards = new Guard[2];
              Guards[0] = ((PJoin) jt).a();
              Guards[1] = ((PJoin) jt).r();
            } else if (jt instanceof PJoin2){
              actualTypeArguments = new Class<?>[3];
              actualTypeArguments[0] = ((PJoin2) jt).a().getTypeClass();
              actualTypeArguments[1] = ((PJoin2) jt).b().getTypeClass();
              actualTypeArguments[2] = ((PJoin2) jt).r().getTypeClass();

              Guards = new Guard[3];
              Guards[0] = ((PJoin2) jt).a();
              Guards[1] = ((PJoin2) jt).b();
              Guards[2] = ((PJoin2) jt).r();

            } else if (jt instanceof PJoin3){
              actualTypeArguments = new Class<?>[4];
              actualTypeArguments[0] = ((PJoin3) jt).a().getTypeClass();
              actualTypeArguments[1] = ((PJoin3) jt).b().getTypeClass();
              actualTypeArguments[2] = ((PJoin3) jt).c().getTypeClass();
              actualTypeArguments[3] = ((PJoin3) jt).r().getTypeClass();

              Guards = new Guard[4];
              Guards[0] = ((PJoin3) jt).a();
              Guards[1] = ((PJoin3) jt).b();
              Guards[2] = ((PJoin3) jt).c();
              Guards[3] = ((PJoin3) jt).r();
            }

            for (Type t : actualTypeArguments){
              clazzes.add((Class<?>) t);
            }


            for (int i=0;i<actualTypeArguments.length-1;i++){
              java.lang.reflect.Type t = actualTypeArguments[i];
              Guard Guard = Guards[i];
              Class<?> tOut = (Class<?>) t;

              java.lang.reflect.Type output = actualTypeArguments[actualTypeArguments.length-1];
              Guard ptypeOut = Guards[Guards.length-1];
              Class<?> rOut = (Class<?>) output;
              deconstructType(logic,rOut);

              append(ptypeOut.getTypeClass().getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
              append(Guard.getTypeClass().getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
              append(tOut.getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
              //append(tOut.getSimpleName()+"->"+ Guard.getTypeClass().getSimpleName()+";\n");

              append(Guard.getTypeClass().getSimpleName()+"->"+stepDesc+"_join_"+joinNo+" [label=PRE];\n",logic);

              if (ptypeOut instanceof GuardXOR<?>){
                for (Guard<?> clazz : ((GuardXOR<?>) ptypeOut).getChoices()){
                  clazzes.add(clazz.getTypeClass());
                  append(clazz.getTypeClass().getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
                  append(stepDesc+"_join_"+joinNo+"->"+clazz.getTypeClass().getSimpleName()+" [label=POST];\n",logic);
                }
              } else {
                clazzes.add(ptypeOut.getTypeClass());
                append(ptypeOut.getTypeClass().getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
                append(stepDesc+"_join_"+joinNo+"->"+ptypeOut.getTypeClass().getSimpleName()+" [label=POST];\n",logic);
              }

//              for (Object w : Guard.getWeakerPTypes()){
//                if (w instanceof Guard){
//                  append(((Guard) w).getTypeClass().getSimpleName()+"->"+Guard.getTypeClass().getSimpleName()+";\n");
//                }
//              }

              append(stepDesc+"_join_"+joinNo+" [shape=rect style=filled, fillcolor=orange fontcolor=black];\n",logic);
              append(tOut.getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n",logic);
              //append(tOut.getSimpleName()+"->"+stepDesc+"_join_"+joinNo+" [label=PRE];\n");
              //append(stepDesc+"_join_"+joinNo+"->"+rOut.getSimpleName()+" [label=POST];\n");
            }

//            for (Guard t : (List<Guard>) ((Pair) jt).getValue0()){
//              append(stepDesc+"_join_"+joinNo+" [shape=rect style=filled, fillcolor=blue fontcolor=white];\n");
//              append(t.getTypeClass().getSimpleName()+" [style=filled, fillcolor=blue fontcolor=white];\n");
//              append(t.getTypeClass().getSimpleName()+"->"+stepDesc+"_join_"+joinNo+" [label=PRE];\n");
//              append(stepDesc+"_join_"+joinNo+"->"+(((Guard) ((Pair) jt).getValue()).getTypeClass().getSimpleName())+" [label=POST];\n");
//            }
          }
          joinNo++;
        }
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

  private void deconstructType(RGraph step, Guard<?> some){
    deconstruct(step,some.getTypeClass());
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
    if (isAnnotatedWithDeconstruct(clazz)) {
      for (Method m : ReflectUtils.getAllMethodsAccessibleFromObject(clazz)) {
        if (m.isAnnotationPresent(Extract.class) &&
                m.getParameterCount()==0 &&
                Modifier.isPublic(m.getModifiers())){
          append(clazz.getSimpleName()+"->"+m.getReturnType().getSimpleName()+" [label=extract];\n",step);
          deconstruct(step,m.getReturnType());
        }
      }
    }
  }
}
