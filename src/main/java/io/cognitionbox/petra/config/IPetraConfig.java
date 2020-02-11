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

    public boolean isDefensiveCopyAllInputsExceptForEffectedInputs();

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
