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
package io.cognitionbox.petra.lang.impls;

import io.cognitionbox.petra.factory.PetraParallelComponentsFactory;
import io.cognitionbox.petra.factory.PetraSequentialComponentsFactory;
import io.cognitionbox.petra.lang.PGraphComputer;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.config.PetraTestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

public class BaseExecutionModesTest extends BaseTest {

  private static Properties original = new Properties(System.getProperties());

  protected ExecMode getExecMode() {
    return execMode;
  }

  private ExecMode execMode;

  public BaseExecutionModesTest(ExecMode execMode) {
    this.execMode = execMode;
  }

  @Parameterized.Parameters
  public static Collection executionModes() {
    return Arrays.asList(new Object[][] {
            {ExecMode.SEQ},
            {ExecMode.PAR}
    });
  }

  private long millisToWait = 1000;

  final protected io.cognitionbox.petra.lang.PGraphComputer getGraphComputer() {
    return PGraphComputer;
  }

  protected io.cognitionbox.petra.lang.PGraphComputer PGraphComputer;
  //private HazelcastInstance instance;
  @Before
  public void setup(){
    super.setup();
    PetraTestConfig testConfig = new PetraTestConfig();
    testConfig
            .allowExceptionsPassthrough()
            .setMode(execMode)
            //.setHazelcastServerMode(HazelcastServerMode.LOCAL).
            .enableStatesLogging()
            .setIsReachabilityChecksEnabled(true)
            .setConstructionGuaranteeChecks(true)
            .setDefensiveCopyAllInputsExceptForEffectedInputs(false)
            .setStrictModeExtraConstructionGuarantee(false)
            .setSequentialModeFactory(new PetraSequentialComponentsFactory())
            .setParallelModeFactory(new PetraParallelComponentsFactory());
    RGraphComputer.setConfig(testConfig);
    PGraphComputer = new PGraphComputer();
  }

  @After
  public void tearDown(){
    PGraphComputer.shutdown();
    PGraphComputer = null;
//    if (instance!=null){
//      instance.shutdown();
//      //instance.getCluster().shutdown();
//    }
  }

  private void waitForXmillis(long x){
    try {
      Thread.sleep(x);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
