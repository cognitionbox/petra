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

import io.cognitionbox.petra.guarantees.StepCheck;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.RGraph;
import io.cognitionbox.petra.core.IStep;

import java.lang.reflect.ParameterizedType;

public class CheckAllPrePostTypesHaveNoGenericsUnlessTheyAreRefsWhichCanOnlyHaveNonGenericTypes implements StepCheck {

        private boolean nonRefTypeIsOk(ParameterizedType pt, int prePostType) {
            if (!(pt.getActualTypeArguments()[prePostType] instanceof ParameterizedType)) {
                return true;
            }
            return false;
        }


        @Override
        public boolean test(IStep<?, ?> abstractStep) {
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