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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

public class ExtractsAtClassLevelMustBeAppliedOnlyToIterablesOrClassesWhichHaveExtractOnFields implements StepCheck {

        public static boolean extractOnClassOk(Class<?> clazz) {
            if (!clazz.isAnnotationPresent(Extract.class)) {
                return true;
            }
            if (Iterable.class.isAssignableFrom(clazz) || Collection.class.isAssignableFrom(clazz)) {
                return true;
            }
            for (Method m : ReflectUtils.getAllMethodsAccessibleFromObject(clazz)) {
                if (m.isAnnotationPresent(Extract.class) &&
                        m.getParameterCount() == 0 &&
                        Modifier.isPublic(m.getModifiers())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean test(IStep<?, ?> step) {
            if (step.p() == null) {
                return true;
            }
            boolean a = extractOnClassOk(step.p().getTypeClass());
            if (step.q() == null) {
                return true;
            }
            boolean b = extractOnClassOk(step.q().getTypeClass());
            return a && b;
        }
    }