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

import io.cognitionbox.petra.config.IPetraConfig;
import io.cognitionbox.petra.core.IRingbuffer;
import io.cognitionbox.petra.config.PetraConfig;
import io.cognitionbox.petra.core.engine.petri.impl.Token;
import io.cognitionbox.petra.core.impl.*;
import io.cognitionbox.petra.guarantees.impl.ConstructionGuarantees;
import io.cognitionbox.petra.util.Petra;
import io.cognitionbox.petra.core.IStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

import static io.cognitionbox.petra.util.Petra.rw;

public class RGraphComputer<X extends D, D> implements Serializable {

  public static IPetraConfig getConfig() {
    return config;
  }

  public static void setConfig(IPetraConfig config) {
    RGraphComputer.config = config;
  }

  private static volatile IPetraConfig config = new PetraConfig();

  public static ExecutorService getWorkerExecutor() {
    return workerExecutor;
  }

  public static IRingbuffer<Serializable> getTaskQueue() {
    return taskQueue;
  }

  private void readObject(java.io.ObjectInputStream stream)
          throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    init();
  }

  static transient IRingbuffer<Serializable> taskQueue;
  static transient ExecutorService workerExecutor;

  final static Logger LOG = LoggerFactory.getLogger(RGraphComputer.class);

  private boolean invoked = false;
  RW<X> result;
  private transient Lock initializerLock;
  private transient Lock masterLock;
  private String dotDiagram;

  private static final String RESULT = "RESULT";
  private static final String INITIALIZER_LOCK = "INITIALIZER_LOCK";
  private static final String MASTER_LOCK = "MASTER_LOCK";

  public RGraphComputer(){
    init();
  }

  private void init() {
    result = Petra.rw(null,RESULT);
    initializerLock = Petra.getFactory().createLock(INITIALIZER_LOCK);
    masterLock = Petra.getFactory().createLock(MASTER_LOCK);
    taskQueue = Petra.getFactory().createRingbuffer("tasks");
  }

  public void shutdown() {

  }

  private RGraph<X, D> rootGraph;
  synchronized public X eval(RGraph<X, D> xGraphSafe, X input) {
    this.rootGraph = xGraphSafe;

    taskQueue = Petra.getFactory().createRingbuffer("tasks");

    if (!RGraphComputer.getConfig().getMode().isSEQ()){
      workerExecutor = Petra.getFactory().createExecutorService("exe");
    }

    List<AbstractStep> steps = LogicStepsCollector.getAllSteps(this.rootGraph);

    if (RGraphComputer.getConfig().isConstructionGuaranteeChecks()){
      ConstructionGuarantees constructionGuarantees = new ConstructionGuarantees(getConfig());
      constructionGuarantees.initAllChecks();
      List<StepError> stepErrors = new ArrayList<>();
      // check edges first
      for (IStep step : steps.stream().filter(s->!(s instanceof RGraph)).collect(Collectors.toList())){
        constructionGuarantees.runAllChecks(stepErrors,step);
      }
      // then check graphs
      for (IStep step : steps.stream().filter(s->s instanceof RGraph).collect(Collectors.toList())){
        constructionGuarantees.runAllChecks(stepErrors,step);
      }

      if (!stepErrors.isEmpty()){
        stepErrors.forEach(se->{
          LOG.error(se.toString());
        });
        throw new AssertionError("step errors exist.");
      }
    }

//    PGraphDotDiagramRendererImpl2 renderer = new PGraphDotDiagramRendererImpl2();
//    renderer.render(this.rootGraph);
//    renderer.finish();
//    LOG.info("\n"+renderer.getDotOutput());

    if (!invoked) {
      invoked = true;
      return handle(input);
    } else {
      throw new UnsupportedOperationException(
              "cannot call compute more than once per process.");
    }

  }

  public static boolean isMaster = false;

  private X handle(X input) {
    return tryAquireLoopAndExecute(this.rootGraph,input);
  }

  private X tryAquireLoopAndExecute(RGraph xGraphSafe, X input) {
      if (result.get()!=null){
        return result.get();
      }
      if (initializerLock.tryLock()){
        waitForInput(xGraphSafe, input);
        xGraphSafe.setInput(new Token(input));
        xGraphSafe.initInput();
        X out = startMasterLoop(); // one nodes wins and keeps the lock
        result.set(out);
      } else {
        try {
          masterLock.lock();
          X out = startMasterLoop(); // the rest lose and just work the iterations
          result.set(out);
        } finally {
          // what if node fails and therefore does not unlock, need to tidy locks after node failures
          masterLock.unlock();
        }
      }
      return result.get();
  }

  private X startMasterLoop(){
    return (X) this.rootGraph.executeMatchingLoopUntilPostCondition();
  }

  private void waitForInput(RGraph xGraphSafe, X input){
    while (!xGraphSafe.evalP(input)){
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

}
