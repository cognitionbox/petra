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
package io.cognitionbox.petra.guarantees.impl;

import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.guarantees.StepCheck;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.RGraph;

import java.lang.reflect.ParameterizedType;

public class CheckAllPrePostTypesHaveNoGenericsUnlessTheyAreRefsWhichCanOnlyHaveNonGenericTypes implements StepCheck {

    private boolean nonRefTypeIsOk(ParameterizedType pt, int prePostType) {
        if (!(pt.getActualTypeArguments()[prePostType] instanceof ParameterizedType)) {
            return true;
        }
        return false;
    }


    @Override
    public boolean test(IStep<?> abstractStep) {
        ParameterizedType pt = (ParameterizedType) abstractStep.getStepClazz().getGenericSuperclass();
        if (abstractStep.getEffectType().isPresent()) {
            return nonRefTypeIsOk(pt, 0);
        } else if (abstractStep instanceof PEdge || abstractStep instanceof RGraph) {
            boolean a = (nonRefTypeIsOk(pt, 0) && nonRefTypeIsOk(pt, 1));
            if (a) {
                return true;
            }
            return false;
        }
        return true;
    }
}