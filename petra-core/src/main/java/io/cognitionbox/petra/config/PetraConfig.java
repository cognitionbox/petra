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
import io.cognitionbox.petra.guarantees.impl.AllFieldsMustBeSerializableUnlessTransient;
import io.cognitionbox.petra.guarantees.impl.CheckAllPrePostTypesAreStates;
import io.cognitionbox.petra.guarantees.impl.ClassesWithExtractsOnFieldsMustHaveExtractAnnotation;
import io.cognitionbox.petra.guarantees.impl.EdgeEffectMustHaveInputTypeEqualToOutputType;
import io.cognitionbox.petra.guarantees.impl.EdgeMustHaveAfunction;
import io.cognitionbox.petra.guarantees.impl.EffectTypesMustBeClassesAndNotInterfaces;
import io.cognitionbox.petra.guarantees.impl.ExtractsAtClassLevelMustBeAppliedOnlyToIterablesOrClassesWhichHaveExtractOnFields;
import io.cognitionbox.petra.guarantees.impl.GraphMustHaveAtLeastOneStep;
import io.cognitionbox.petra.guarantees.impl.GraphMustHaveAtLeastOneStepOrJoin;
import io.cognitionbox.petra.guarantees.impl.GraphOutputCannotBeReachedFromInput;
import io.cognitionbox.petra.guarantees.impl.NoStepsInSameStepHaveSamePreconditionType;
import io.cognitionbox.petra.guarantees.impl.OnlyEdgesWithSideEffectsMustImplementIRollback;
import io.cognitionbox.petra.guarantees.impl.OnlyStepWithSideAffectTrueMustImplementSideEffect;
import io.cognitionbox.petra.guarantees.impl.PrePostTypesMustBeBoundToUniquePredicates;
import io.cognitionbox.petra.guarantees.impl.StaticFieldsOnlyAllowedIfFinalAndPrimitive;
import io.cognitionbox.petra.guarantees.impl.StepMustHaveValidPreAndPostCondition;
import io.cognitionbox.petra.guarantees.impl.StepWithImmutablePreOrPostConditionTypesCannotBeSideEffects;
import io.cognitionbox.petra.guarantees.impl.StepsCanCannotDeclareConstructors;
import io.cognitionbox.petra.guarantees.impl.StepsCanOnlyEverImplementOneInterfaceWhichIsIRollback;
import io.cognitionbox.petra.guarantees.impl.StepsCannotHaveFields;
import io.cognitionbox.petra.guarantees.impl.StepsMustHavePublicClasses;

public class PetraConfig implements IPetraConfig {
    final public PrePostTypesMustBeBoundToUniquePredicates
            PrePostTypesMustBeBoundToUniquePredicates_ =
            new PrePostTypesMustBeBoundToUniquePredicates();
    final public EdgeEffectMustHaveInputTypeEqualToOutputType
            EdgeEffectMustHaveInputTypeEqualToOutputType_ =
            new EdgeEffectMustHaveInputTypeEqualToOutputType();
    final public EffectTypesMustBeClassesAndNotInterfaces
            EffectTypesMustBeClassesAndNotInterfaces_ = new EffectTypesMustBeClassesAndNotInterfaces();
    final public AllFieldsMustBeSerializableUnlessTransient
            AllFieldsMustBeSerializableUnlessTransient_ =
            new AllFieldsMustBeSerializableUnlessTransient();
    final public StaticFieldsOnlyAllowedIfFinalAndPrimitive
            StaticFieldsOnlyAllowedIfFinalAndPrimitive_ =
            new StaticFieldsOnlyAllowedIfFinalAndPrimitive();
    final public StepsCanOnlyEverImplementOneInterfaceWhichIsIRollback
            StepsCanOnlyEverImplementOneInterfaceWhichIsIRollback_ =
            new StepsCanOnlyEverImplementOneInterfaceWhichIsIRollback();
    //     final public GraphContainingEffectStepsMustItselfBeAnEffect
//            GraphContainingEffectStepsMustItselfBeAnEffect =
//            new GraphContainingEffectStepsMustItselfBeAnEffect();
    final public OnlyEdgesWithSideEffectsMustImplementIRollback
            OnlyEdgesWithSideEffectsMustImplementIRollback_
            = new OnlyEdgesWithSideEffectsMustImplementIRollback();
    final public ClassesWithExtractsOnFieldsMustHaveExtractAnnotation
            ClassesWithExtractsOnFieldsMustHaveExtractAnnotation_ =
            new ClassesWithExtractsOnFieldsMustHaveExtractAnnotation();
    final public ExtractsAtClassLevelMustBeAppliedOnlyToIterablesOrClassesWhichHaveExtractOnFields
            ExtractsAtClassLevelMustBeAppliedOnlyToIterablesOrClassesWhichHaveExtractOnFields_ =
            new ExtractsAtClassLevelMustBeAppliedOnlyToIterablesOrClassesWhichHaveExtractOnFields();
    final public GraphMustHaveAtLeastOneStep GraphMustHaveAtLeastOneStep_ = new GraphMustHaveAtLeastOneStep();
    final public StepsCannotHaveFields StepsCannotHaveFields_ = new StepsCannotHaveFields();
    final public StepsCanCannotDeclareConstructors StepsCanCannotDeclareConstructors_ = new StepsCanCannotDeclareConstructors();
    final public StepsMustHavePublicClasses StepsMustHavePublicClasses_ = new StepsMustHavePublicClasses();
    final public EdgeMustHaveAfunction EdgeMustHaveAfunction_ = new EdgeMustHaveAfunction();
    final public GraphOutputCannotBeReachedFromInput GraphOutputCannotBeReachedFromInput_ = new GraphOutputCannotBeReachedFromInput();
    final public GraphMustHaveAtLeastOneStepOrJoin GraphMustHaveAtLeastOneStepOrJoin_ = new GraphMustHaveAtLeastOneStepOrJoin();
    final public NoStepsInSameStepHaveSamePreconditionType NoStepsInSameStepHaveSamePreconditionType_ = new NoStepsInSameStepHaveSamePreconditionType();
    final public OnlyStepWithSideAffectTrueMustImplementSideEffect OnlyStepWithSideAffectTrueMustImplementSideEffect_ = new OnlyStepWithSideAffectTrueMustImplementSideEffect();
    // final public StepOutputTypesAssignableFromInputTypesMustHaveSideAffectAnnotation StepOutputTypesAssignableFromInputTypesMustHaveSideAffectAnnotation = new StepOutputTypesAssignableFromInputTypesMustHaveSideAffectAnnotation();
    final public CheckAllPrePostTypesAreStates CheckAllPrePostTypesAreStates_ = new CheckAllPrePostTypesAreStates();
    final public StepMustHaveValidPreAndPostCondition StepMustHaveValidPreAndPostCondition_ = new StepMustHaveValidPreAndPostCondition();
    // final public StepsWithSameNonStateInputAndOutputTypesMustBeSideEffects StepWithSameInputAndOutputTypesMustImplementTransform = new StepsWithSameNonStateInputAndOutputTypesMustBeSideEffects();
    final public StepWithImmutablePreOrPostConditionTypesCannotBeSideEffects StepWithImmutablePreOrPostConditionTypesCannotBeSideEffects_ = new StepWithImmutablePreOrPostConditionTypesCannotBeSideEffects();
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
