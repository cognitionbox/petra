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
package io.cognitionbox.petra.config;

import io.cognitionbox.petra.factory.IPetraComponentsFactory;

public interface IPetraConfig {

    public IPetraComponentsFactory getSequentialModeFactory();
    public IPetraComponentsFactory getParallelModeFactory();
    public IPetraComponentsFactory getDistributedModeFactory();

    public IPetraConfig setSequentialModeFactory(IPetraComponentsFactory factory);
    public IPetraConfig setParallelModeFactory(IPetraComponentsFactory factory);
    public IPetraConfig setDistributedModeFactory(IPetraComponentsFactory factory);

    public boolean isExceptionsPassthrough();

    public boolean isTestMode();

    public long getMaxIterations();

    public IPetraConfig setSleepPeriod(long sleepPeriod);

    public IPetraConfig setDeadLockRecovery(boolean deadLockRecovery);

    public boolean isDeadLockRecoveryActive();

    public boolean isDefensiveCopyAllInputs();

    public IPetraConfig setDefensiveCopyAllInputsExceptForEffectedInputs(boolean defensiveCopyAllInputsExceptForEffectedInputs);

    public IPetraConfig setIsReachabilityChecksEnabled(boolean isReachabilityChecksEnabled);

    public boolean isReachabilityChecksEnabled();

    public ExecMode getMode();

    public IPetraConfig setMode(ExecMode mode);

    public boolean isDebugModeEnabled();

    public void enableDebugMode();

    public IPetraConfig enableStatesLogging();

    public IPetraConfig enableAllStatesLogging();

    public boolean isConstructionGuaranteeChecks();

    public IPetraConfig setConstructionGuaranteeChecks(boolean constructionGuaranteeChecks);

    public boolean isStrictModeExtraConstructionGuarantee();

    public IPetraConfig setStrictModeExtraConstructionGuarantee(boolean strictModeExtraConstructionGuarantee);

    public boolean isStatesLoggingEnabled();

    public boolean isAllStatesLoggingEnabled();

    public boolean isEnableUserCodeDeployment();

    public IPetraConfig setEnableUserCodeDeployment(boolean enableUserCodeDeployment);

    public long getSleepPeriod();
}
