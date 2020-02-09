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
import io.cognitionbox.petra.lang.annotations.DoesNotTerminate;
import io.cognitionbox.petra.core.IStep;

public class StepMustHaveValidPreAndPostCondition implements StepCheck {
        @Override
        public boolean test(IStep<?, ?> step) {
            boolean a = step.p() != null && step.p().getTypeClass() != null;
            boolean b = step.q() != null && step.q().getTypeClass() != null;
            boolean c = step.q() == null && step.getStepClazz().isAnnotationPresent(DoesNotTerminate.class);
            return (a && b) || (a && c);
        }
    }