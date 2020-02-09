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
import io.cognitionbox.petra.lang.annotations.Immutable;
import io.cognitionbox.petra.core.IStep;

public class StepWithImmutablePreOrPostConditionTypesCannotBeSideEffects implements StepCheck {

        private boolean isImmutableType(Class<?> clazz) {
            if (clazz.getAnnotationsByType(Immutable.class).length > 0 ||
                    Boolean.class.isAssignableFrom(clazz) ||
                    Number.class.isAssignableFrom(clazz) ||
                    String.class.isAssignableFrom(clazz)) {
                return true;
            }
            return false;
        }

        private boolean isSideEffect(IStep<?, ?> step) {
            return step.getEffectType().isPresent();
        }

        @Override
        public boolean test(IStep<?, ?> step) {
            if (isSideEffect(step) && (isImmutableType(step.p().getTypeClass()) ||
                    isImmutableType(step.q().getTypeClass()))) {
                return false;
            }
            return true;
        }
    }