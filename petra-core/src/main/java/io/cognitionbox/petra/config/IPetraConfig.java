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
package io.cognitionbox.petra.config;

import io.cognitionbox.petra.factory.IPetraComponentsFactory;

public interface IPetraConfig {

    IPetraComponentsFactory getSequentialModeFactory();

    IPetraComponentsFactory getParallelModeFactory();

    IPetraComponentsFactory getDistributedModeFactory();

    IPetraConfig setSequentialModeFactory(IPetraComponentsFactory factory);

    IPetraConfig setParallelModeFactory(IPetraComponentsFactory factory);

    IPetraConfig setDistributedModeFactory(IPetraComponentsFactory factory);

    boolean isExceptionsPassthrough();

    boolean isTestMode();

    long getMaxIterations();

    IPetraConfig setSleepPeriod(long sleepPeriod);

    IPetraConfig setDeadLockRecovery(boolean deadLockRecovery);

    boolean isDeadLockRecoveryActive();

    boolean isDefensiveCopyAllInputs();

    IPetraConfig setDefensiveCopyAllInputsExceptForEffectedInputs(boolean defensiveCopyAllInputsExceptForEffectedInputs);

    IPetraConfig setIsReachabilityChecksEnabled(boolean isReachabilityChecksEnabled);

    boolean isReachabilityChecksEnabled();

    ExecMode getMode();

    IPetraConfig setMode(ExecMode mode);

    boolean isDebugModeEnabled();

    void enableDebugMode();

    IPetraConfig enableStatesLogging();

    IPetraConfig enableAllStatesLogging();

    boolean isConstructionGuaranteeChecks();

    IPetraConfig setConstructionGuaranteeChecks(boolean constructionGuaranteeChecks);

    boolean isStrictModeExtraConstructionGuarantee();

    IPetraConfig setStrictModeExtraConstructionGuarantee(boolean strictModeExtraConstructionGuarantee);

    boolean isStatesLoggingEnabled();

    boolean isAllStatesLoggingEnabled();

    boolean isEnableUserCodeDeployment();

    IPetraConfig setEnableUserCodeDeployment(boolean enableUserCodeDeployment);

    long getSleepPeriod();
}
