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
package io.cognitionbox.petra.guarantees.impl;

import io.cognitionbox.petra.config.IPetraConfig;
import io.cognitionbox.petra.core.IPGraph;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.core.impl.StepError;
import io.cognitionbox.petra.guarantees.Check;
import io.cognitionbox.petra.guarantees.EdgeCheck;
import io.cognitionbox.petra.guarantees.GraphCheck;
import io.cognitionbox.petra.guarantees.StepCheck;
import io.cognitionbox.petra.lang.AbstractStep;
import io.cognitionbox.petra.lang.PEdge;
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
        addCheck(new PrePostTypesMustBeBoundToUniquePredicates());

        initAllChecksExceptForThoseNotCompatibleWithGraphsGeneratedByReachabilityCheck();

//        if (!config.JoinEffectMustHaveOnlyOneInputTypeEqualToTheOutputType_.test( step)){
//            stepErrors.add(new StepError(step,config.JoinEffectMustHaveOnlyOneInputTypeEqualToTheOutputType.getClass().getSimpleName()));
//        }

        //addCheck(new OnlyEdgesWithSideEffectsMustImplementIRollback());
        addCheck(new OnlyStepWithSideAffectTrueMustImplementSideEffect());
        //addCheck(new StepsCannotHaveFields());

        //addCheck(new StepsCanCannotDeclareConstructors());
        //addCheck(new StepsCannotHaveFields());

        if (config.isStrictModeExtraConstructionGuarantee()) {

        }

        if (config.isReachabilityChecksEnabled()) {
            addCheck(new GraphOutputCannotBeReachedFromInput());
        }
    }

    void initAllChecksExceptForThoseNotCompatibleWithGraphsGeneratedByReachabilityCheck(){
        if (config.isDefensiveCopyAllInputs()) {
            addCheck(new AllFieldsMustBeSerializableUnlessTransient());
        }
        addCheck(new StepsCanOnlyEverImplementOneInterfaceWhichIsIRollback());
        addCheck(new StepsMustHavePublicClasses());
        addCheck(new EffectTypesMustBeClassesAndNotInterfaces());
        addCheck(new StaticFieldsOnlyAllowedIfFinalAndPrimitive());
        addCheck(new CheckAllPrePostTypesHaveNoGenericsUnlessTheyAreRefsWhichCanOnlyHaveNonGenericTypes());
        addCheck(new GraphMustHaveAtLeastOneStepOrJoin());
        addCheck(new EdgeMustHaveAfunction());
    }

    public void runAllChecks(List<StepError> stepErrors, IStep step) {
        for (Check c : checks){
            if ((c instanceof EdgeCheck && step instanceof PEdge) ||
                    (c instanceof StepCheck && step instanceof IStep) ||
                    (c instanceof GraphCheck && step instanceof IPGraph)){
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

    public static boolean isSideEffect(AbstractStep<?> step) {
        return true;
    }
}