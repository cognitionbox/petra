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
import io.cognitionbox.petra.guarantees.impl.*;

public class PetraConfig implements IPetraConfig {
    final public PrePostTypesMustBeBoundToUniquePredicates
            PrePostTypesMustBeBoundToUniquePredicates_ =
            new PrePostTypesMustBeBoundToUniquePredicates();
    final public EdgeEffectMustHaveInputTypeEqualToOutputType
            EdgeEffectMustHaveInputTypeEqualToOutputType_ =
            new EdgeEffectMustHaveInputTypeEqualToOutputType();
    final public ExclusivesMustBeMatchedOnlyByEffectSteps
            ExclusivesMustBeMatchedOnlyByEffectSteps_ = new ExclusivesMustBeMatchedOnlyByEffectSteps();
    final public EffectTypesMustBeClassesAndNotInterfaces
            EffectTypesMustBeClassesAndNotInterfaces_ = new EffectTypesMustBeClassesAndNotInterfaces();
    final public ExclusiveFieldsCanOnlyExistWithinAnExclusiveEffectTypes
            ExclusiveFieldsCanOnlyExistWithinAnExclusiveEffectTypes_ =
            new ExclusiveFieldsCanOnlyExistWithinAnExclusiveEffectTypes();
    final public ExclusiveMethodsCanOnlyExistWithinAnExclusiveTypes
            ExclusiveMethodsCanOnlyExistWithinAnExclusiveTypes_ =
            new ExclusiveMethodsCanOnlyExistWithinAnExclusiveTypes();
    final public AllFieldsMustBeSerializableUnlessTransient
            AllFieldsMustBeSerializableUnlessTransient_ =
            new AllFieldsMustBeSerializableUnlessTransient();
    final public StaticFieldsOnlyAllowedIfFinalAndPrimitive
            StaticFieldsOnlyAllowedIfFinalAndPrimitive_ =
            new StaticFieldsOnlyAllowedIfFinalAndPrimitive();
    final public NoJoinsCanHaveSameInputPreconditionTypes
            NoJoinsCanHaveSameInputPreconditionTypes_ =
            new NoJoinsCanHaveSameInputPreconditionTypes();
    final public StepsCanOnlyEverImplementOneInterfaceWhichIsIRollback
            StepsCanOnlyEverImplementOneInterfaceWhichIsIRollback_ =
            new StepsCanOnlyEverImplementOneInterfaceWhichIsIRollback();
    //     final public GraphContainingEffectStepsMustItselfBeAnEffect
//            GraphContainingEffectStepsMustItselfBeAnEffect =
//            new GraphContainingEffectStepsMustItselfBeAnEffect();
    final public JoinsWithOverlappingInputAndOutputTypesMustBeSideEffects
            JoinsWithOverlappingInputAndOutputTypesMustBeSideEffects_ =
            new JoinsWithOverlappingInputAndOutputTypesMustBeSideEffects();
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
    final public CheckAllPrePostTypesHaveNoGenericsUnlessTheyAreRefsWhichCanOnlyHaveNonGenericTypes CheckAllPrePostTypesHaveNoGenericsUnlessTheyAreRefsWhichCanOnlyHaveNonGenericTypes_ = new CheckAllPrePostTypesHaveNoGenericsUnlessTheyAreRefsWhichCanOnlyHaveNonGenericTypes();
    public boolean defensiveCopyAllInputsExceptForEffectedInputs = false;
    protected boolean exceptionsPassthrough = false;
    protected boolean testMode = false;
    protected long maxIterations = 10;
    private boolean enableUserCodeDeployment = false;
    private volatile boolean isStatesLoggingEnabled = false;
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

    public boolean isDefensiveCopyAllInputsExceptForEffectedInputs() {
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
