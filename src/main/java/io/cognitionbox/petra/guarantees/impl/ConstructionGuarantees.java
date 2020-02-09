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
package io.cognitionbox.petra.guarantees.impl;

import io.cognitionbox.petra.config.IPetraConfig;
import io.cognitionbox.petra.config.PetraConfig;
import io.cognitionbox.petra.core.IGraph;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.core.impl.PGraphDotDiagramRendererImpl2;
import io.cognitionbox.petra.core.impl.StepError;
import io.cognitionbox.petra.guarantees.Check;
import io.cognitionbox.petra.guarantees.EdgeCheck;
import io.cognitionbox.petra.guarantees.GraphCheck;
import io.cognitionbox.petra.guarantees.StepCheck;
import io.cognitionbox.petra.lang.AbstractStep;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.RGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ConstructionGuarantees {

    final Logger LOG = LoggerFactory.getLogger(ConstructionGuarantees.class);
    private IPetraConfig config;

    private List<Check> checks = new ArrayList<>();

    public void addCheck(Check check){
        checks.add(check);
    }

    public ConstructionGuarantees(IPetraConfig config) {
        this.config = config;
    }

    public void initAllChecks(){
        addCheck(new StepMustHaveValidPreAndPostCondition());

        initAllChecksExceptForThoseNotCompatibleWithGraphsGeneratedByReachabilityCheck();

        addCheck(new EdgeEffectMustHaveInputTypeEqualToOutputType());

//        if (!config.JoinEffectMustHaveOnlyOneInputTypeEqualToTheOutputType_.test( step)){
//            stepErrors.add(new StepError(step,config.JoinEffectMustHaveOnlyOneInputTypeEqualToTheOutputType.getClass().getSimpleName()));
//        }

        addCheck(new OnlyEdgesWithSideEffectsMustImplementIRollback());
        addCheck(new OnlyStepWithSideAffectTrueMustImplementSideEffect());
        addCheck(new StepWithImmutablePreOrPostConditionTypesCannotBeSideEffects());
        addCheck(new StepsCannotHaveFields());

        addCheck(new StepsCanCannotDeclareConstructors());
        addCheck(new PrePostTypesMustBeBoundToUniquePredicates());
        addCheck(new StepsCannotHaveFields());

        if (config.isStrictModeExtraConstructionGuarantee()) {
            addCheck(new CheckAllPrePostTypesAreStates());
            addCheck(new NoStepsInSameStepHaveSamePreconditionType());
            addCheck(new NoJoinsCanHaveSameInputPreconditionTypes());
        }

        if (config.isReachabilityChecksEnabled()) {
            addCheck(new GraphOutputCannotBeReachedFromInput());
        }
    }

    void initAllChecksExceptForThoseNotCompatibleWithGraphsGeneratedByReachabilityCheck(){
        if (config.isDefensiveCopyAllInputsExceptForEffectedInputs()) {
            addCheck(new AllFieldsMustBeSerializableUnlessTransient());
        }
        addCheck(new StepsCanOnlyEverImplementOneInterfaceWhichIsIRollback());
        addCheck(new StepsMustHavePublicClasses());
        addCheck(new EffectTypesMustBeClassesAndNotInterfaces());
        addCheck(new ExclusivesMustBeMatchedOnlyByEffectSteps());
        addCheck(new ExclusiveFieldsCanOnlyExistWithinAnExclusiveEffectTypes());
        addCheck(new ExclusiveMethodsCanOnlyExistWithinAnExclusiveTypes());
        addCheck(new StaticFieldsOnlyAllowedIfFinalAndPrimitive());
        addCheck(new ClassesWithExtractsOnFieldsMustHaveExtractAnnotation());
        addCheck(new ExtractsAtClassLevelMustBeAppliedOnlyToIterablesOrClassesWhichHaveExtractOnFields());
        addCheck(new CheckAllPrePostTypesHaveNoGenericsUnlessTheyAreRefsWhichCanOnlyHaveNonGenericTypes());
        addCheck(new GraphMustHaveAtLeastOneStepOrJoin());
        addCheck(new EdgeMustHaveAfunction());
    }

    public void runAllChecks(List<StepError> stepErrors, IStep step) {
        for (Check c : checks){
            if ((c instanceof EdgeCheck && step instanceof PEdge) ||
                    (c instanceof StepCheck && step instanceof IStep) ||
                    (c instanceof GraphCheck && step instanceof IGraph)){
                if (!c.test(step)){
                    stepErrors.add(new StepError(step,c.getClass().getSimpleName()));
                    if (c instanceof StepMustHaveValidPreAndPostCondition &&
                            stepErrors.isEmpty()) {
                        return;
                    }
                }
            }
        }
    }

    public static boolean isSideEffect(AbstractStep<?, ?> step) {
        if (step.isEffect()) {// || step instanceof EffectG){
            return true;
        } else {
            return false;
        }
    }

    String printErrorDotDiagram(RGraph xGraphSafe) {
        PGraphDotDiagramRendererImpl2 renderer = new PGraphDotDiagramRendererImpl2();
        renderer.render(xGraphSafe);
        renderer.finish();
        return renderer.getDotOutput();
    }
}