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
import io.cognitionbox.petra.core.impl.ReflectUtils;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.core.IStep;

import java.lang.reflect.Field;

public class ClassesWithExtractsOnFieldsMustHaveExtractAnnotation implements StepCheck {

        static public boolean extractOnFieldOk(Class<?> clazz) {
            boolean hasExtractedField = false;
            for (Field f : ReflectUtils.getAllFieldsAccessibleFromObject(clazz)) {
                if (f.isAnnotationPresent(Extract.class)) {
                    hasExtractedField = true;
                    break;
                }
            }

            if (hasExtractedField && clazz.isAnnotationPresent(Extract.class)) {
                return true;
            } else if (hasExtractedField && !clazz.isAnnotationPresent(Extract.class)) {
                return false;
            }
            return true;
        }

        @Override
        public boolean test(IStep<?, ?> step) {
            if (step.p() == null) {
                return true;
            }
            boolean a = extractOnFieldOk(step.p().getTypeClass());
            if (step.q() == null) {
                return true;
            }
            boolean b = extractOnFieldOk(step.q().getTypeClass());
            return a && b;
        }
    }