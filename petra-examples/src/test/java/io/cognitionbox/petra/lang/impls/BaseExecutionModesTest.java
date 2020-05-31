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
package io.cognitionbox.petra.lang.impls;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.factory.PetraParallelComponentsFactory;
import io.cognitionbox.petra.factory.PetraSequentialComponentsFactory;
import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.RGraphComputer;
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

  final protected PComputer getGraphComputer() {
    return PComputer;
  }

  protected PComputer PComputer;
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
            .enableAllStatesLogging()
            .setIsReachabilityChecksEnabled(false)
            .setConstructionGuaranteeChecks(true)
            .setDefensiveCopyAllInputsExceptForEffectedInputs(false)
            .setStrictModeExtraConstructionGuarantee(false)
            .setSequentialModeFactory(new PetraSequentialComponentsFactory())
            .setParallelModeFactory(new PetraParallelComponentsFactory());
    RGraphComputer.setConfig(testConfig);
    PComputer = new PComputer();
  }

  @After
  public void tearDown(){
    PComputer.shutdown();
    PComputer = null;
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
