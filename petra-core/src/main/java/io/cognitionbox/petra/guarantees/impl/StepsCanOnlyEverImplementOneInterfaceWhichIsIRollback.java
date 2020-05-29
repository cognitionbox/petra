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

import io.cognitionbox.petra.guarantees.StepCheck;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.RGraph;
import io.cognitionbox.petra.core.IRollback;
import io.cognitionbox.petra.core.IStep;

public class StepsCanOnlyEverImplementOneInterfaceWhichIsIRollback implements StepCheck {

        @Override
        public boolean test(IStep<?> step) {
            if (step.getStepClazz().equals(PEdge.class) ||
                    step.getStepClazz().equals(RGraph.class) ||
                    step.getStepClazz().equals(PGraph.class)) {
                return true;
            }
            if (step.getStepClazz().getInterfaces().length == 0 ||
                    IRollback.class.isAssignableFrom(step.getStepClazz())) {
                return true;
            }
            return false;
        }
    }