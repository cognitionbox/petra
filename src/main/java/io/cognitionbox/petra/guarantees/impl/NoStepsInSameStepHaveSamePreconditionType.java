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

public class NoStepsInSameStepHaveSamePreconditionType implements StepCheck {
        @Override
        public boolean test(IStep<?, ?> step) {
            if (step instanceof RGraph) {
                for (IStep<?, ?> s1 : ((RGraph<?, ?, ?>) step).getParallizable()) {
                    for (IStep<?, ?> s2 : ((RGraph<?, ?, ?>) step).getParallizable()) {
                        if (s1.getStepClazz().equals(s2.getStepClazz())) {
                            continue;
                        }
                        if (s1.p().getTypeClass().isAssignableFrom(s2.p().getTypeClass()) ||
                                s2.p().getTypeClass().isAssignableFrom(s1.p().getTypeClass())) {
                            return false;
                        }
                    }
                }
            } else if (step instanceof PEdge) {
                return !step.p().getTypeClass().equals(step.q().getTypeClass());
            }
            return true;
        }
    }