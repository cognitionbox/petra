/*
 ********************************************************************************************************
 * Copyright © 2016-2020 Cognition Box Ltd. - All Rights Reserved
 *
 * This file is part of the "Petra" system. "Petra" is owned by:
 *
 * Cognition Box Ltd. (10194162)
 * 9 Grovelands Road,
 * Palmers Green,
 * London, N13 4RJ
 * England.
 *
 * "Petra" is Proprietary and Confidential.
 * Unauthorized copying of "Petra" files, via any medium is strictly prohibited.
 *
 * "Petra" can not be copied and/or distributed without the express
 * permission of Cognition Box Ltd.
 *
 * "Petra" includes trade secrets of Cognition Box Ltd.
 * In order to protect "Petra", You shall not decompile, reverse engineer, decrypt,
 * extract or disassemble "Petra" or otherwise reduce or attempt to reduce any software
 * in "Petra" to source code form. You shall ensure, both during and
 * (if you still have possession of "Petra") after the performance of this Agreement,
 * that (i) persons who are not bound by a confidentiality agreement consistent with this Agreement
 * shall not have access to "Petra" and (ii) persons who are so bound are put on written notice that
 * "Petra" contains trade secrets, owned by and proprietary to Cognition Box Ltd.
 *
 * "Petra" is written by Aran Hakki <aran@cognitionbox.io>
 ********************************************************************************************************
 */
package io.cognitionbox.petra.lang.impls;

import com.hazelcast.config.Config;
import com.hazelcast.config.RingbufferConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.config.IPetraConfig;
import io.cognitionbox.petra.config.PetraHazelcastConfig;
import io.cognitionbox.petra.factory.PetraHazelcastComponentsFactory;
import io.cognitionbox.petra.factory.PetraParallelComponentsFactory;
import io.cognitionbox.petra.factory.PetraSequentialComponentsFactory;
import io.cognitionbox.petra.lang.PGraphComputer;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.config.PetraHazelcastTestConfig;
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
        {ExecMode.SEQ}
        ,
        {ExecMode.PAR}
//        ,
//        {ExecMode.DIS}
    });
  }

  private long millisToWait = 1000;

  final protected io.cognitionbox.petra.lang.PGraphComputer getGraphComputer() {
    return PGraphComputer;
  }

  protected PGraphComputer PGraphComputer;
  private HazelcastInstance instance;
  @Before
  public void setup(){
    super.setup();
    IPetraConfig testConfig =
            new PetraHazelcastTestConfig()
                    .allowExceptionsPassthrough()
                    .setMode(execMode)
                    //.setHazelcastServerMode(HazelcastServerMode.LOCAL).
                    .enableStatesLogging()
                    .setIsReachabilityChecksEnabled(true)
                    .setConstructionGuaranteeChecks(true)
                    .setDefensiveCopyAllInputsExceptForEffectedInputs(false)
                    .setStrictModeExtraConstructionGuarantee(false)
                    .setSequentialModeFactory(new PetraSequentialComponentsFactory())
                    .setParallelModeFactory(new PetraParallelComponentsFactory())
                    .setDistributedModeFactory(new PetraHazelcastComponentsFactory());
    RGraphComputer.setConfig(testConfig);
    if (execMode.isDIS()){
      Config config = new Config();
      RingbufferConfig rbConfig1 = new RingbufferConfig("tasks")
              .setCapacity(10000)
              .setTimeToLiveSeconds(30);
      RingbufferConfig rbConfig2 = new RingbufferConfig("iterationTasks")
              .setCapacity(10000)
              .setTimeToLiveSeconds(30);
      config.addRingBufferConfig(rbConfig1);
      config.addRingBufferConfig(rbConfig2);
      instance = Hazelcast.newHazelcastInstance(config);
    }
    PGraphComputer = new PGraphComputer();
  }

  @After
  public void tearDown(){
    PGraphComputer.shutdown();
    PGraphComputer = null;
    if (instance!=null){
//      if (RGraphComputer.getConfig() instanceof PetraHazelcastTestConfig){
//        PetraHazelcastTestConfig c = (PetraHazelcastTestConfig) RGraphComputer.getConfig();
//        c.getHazelcastClient().shutdown();
//        while(c.getHazelcastClient().getClientService().get)
//      }
      instance.shutdown();
      //instance.getCluster().shutdown();
    }
  }

  private void waitForXmillis(long x){
    try {
      Thread.sleep(x);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
