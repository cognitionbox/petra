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

public class PetraConfig implements IPetraConfig {

    public boolean defensiveCopyAllInputsExceptForEffectedInputs = false;
    protected boolean exceptionsPassthrough = false;
    protected boolean testMode = false;
    protected long maxIterations = 10;
    private boolean enableUserCodeDeployment = false;
    private volatile boolean isStatesLoggingEnabled = false;
    private volatile boolean isAllStatesLoggingEnabled = false;
    private long sleepPeriod = 0;
    private boolean deadLockRecovery = false;
    private boolean isReachabilityChecksEnabled = true;
    private boolean debugModeEnabled = false;
    private boolean constructionGuaranteeChecks = true;
    private boolean strictModeExtraConstructionGuarantee = false;
    private ExecMode execMode = ExecMode.PAR;

    private volatile IPetraComponentsFactory sequentialModeFactory;
    private volatile IPetraComponentsFactory parallelModeFactory;
    private volatile IPetraComponentsFactory distributedModeFactory;

    @Override
    public IPetraComponentsFactory getSequentialModeFactory() {
        return sequentialModeFactory;
    }

    @Override
    public IPetraComponentsFactory getParallelModeFactory() {
        return parallelModeFactory;
    }

    @Override
    public IPetraComponentsFactory getDistributedModeFactory() {
        return distributedModeFactory;
    }

    @Override
    public IPetraConfig setSequentialModeFactory(IPetraComponentsFactory factory) {
        this.sequentialModeFactory = factory;
        return this;
    }

    @Override
    public IPetraConfig setParallelModeFactory(IPetraComponentsFactory factory) {
        this.parallelModeFactory = factory;
        return this;
    }

    @Override
    public IPetraConfig setDistributedModeFactory(IPetraComponentsFactory factory) {
        this.distributedModeFactory = factory;
        return this;
    }

    public boolean isExceptionsPassthrough() {
        return exceptionsPassthrough;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public long getMaxIterations() {
        return maxIterations;
    }

    public PetraConfig setSleepPeriod(long sleepPeriod) {
        this.sleepPeriod = sleepPeriod;
        return this;
    }

    public PetraConfig setDeadLockRecovery(boolean deadLockRecovery) {
        this.deadLockRecovery = deadLockRecovery;
        return this;
    }

    public boolean isDeadLockRecoveryActive() {
        return deadLockRecovery;
    }

    public boolean isDefensiveCopyAllInputs() {
        return defensiveCopyAllInputsExceptForEffectedInputs;
    }

    // This is sets whether or not all objects should be defensively copied when being tested against,
    // pre/post conditions and before being passed into edges/joins for processing.
    // Defensive copying gives assurance against accidental state changes.
    // The defensive copying is achieved through Java serialization.
    // Only classes which are serializable can be defensively copied, however in order to make use of the
    // distributed processing mode all classes need to be serializable, so you get two functionalities for the
    // price of making everything serializable.
    // This feature is off as default.
    public PetraConfig setDefensiveCopyAllInputsExceptForEffectedInputs(boolean defensiveCopyAllInputsExceptForEffectedInputs) {
        this.defensiveCopyAllInputsExceptForEffectedInputs = defensiveCopyAllInputsExceptForEffectedInputs;
        return this;
    }

    public PetraConfig setIsReachabilityChecksEnabled(boolean isReachabilityChecksEnabled) {
        this.isReachabilityChecksEnabled = isReachabilityChecksEnabled;
        return this;
    }

    public boolean isReachabilityChecksEnabled() {
        return isReachabilityChecksEnabled;
    }

    public ExecMode getMode() {
        return execMode;
    }

    public PetraConfig setMode(ExecMode mode) {
        execMode = mode;
        return this;
    }

    public boolean isDebugModeEnabled() {
        return debugModeEnabled;
    }

    public void enableDebugMode() {
        this.debugModeEnabled = true;
    }

    public PetraConfig enableStatesLogging() {
        isStatesLoggingEnabled = true;
        return this;
    }

    @Override
    public IPetraConfig enableAllStatesLogging() {
        isAllStatesLoggingEnabled = true;
        return this;
    }

    public boolean isConstructionGuaranteeChecks() {
        return constructionGuaranteeChecks;
    }

    public PetraConfig setConstructionGuaranteeChecks(boolean constructionGuaranteeChecks) {
        this.constructionGuaranteeChecks = constructionGuaranteeChecks;
        return this;
    }

    public boolean isStrictModeExtraConstructionGuarantee() {
        return strictModeExtraConstructionGuarantee;
    }

    public PetraConfig setStrictModeExtraConstructionGuarantee(boolean strictModeExtraConstructionGuarantee) {
        this.strictModeExtraConstructionGuarantee = strictModeExtraConstructionGuarantee;
        return this;
    }

    public boolean isStatesLoggingEnabled() {
        return isStatesLoggingEnabled;
    }

    @Override
    public boolean isAllStatesLoggingEnabled() {
        return isAllStatesLoggingEnabled;
    }

    public boolean isEnableUserCodeDeployment() {
        return enableUserCodeDeployment;
    }

    public PetraConfig setEnableUserCodeDeployment(boolean enableUserCodeDeployment) {
        this.enableUserCodeDeployment = enableUserCodeDeployment;
        return this;
    }

    public long getSleepPeriod() {
        return this.sleepPeriod;
    }
}
