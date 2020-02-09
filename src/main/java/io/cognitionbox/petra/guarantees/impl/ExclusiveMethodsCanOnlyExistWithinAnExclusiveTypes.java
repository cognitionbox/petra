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
import io.cognitionbox.petra.lang.annotations.Exclusive;
import io.cognitionbox.petra.core.IStep;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class ExclusiveMethodsCanOnlyExistWithinAnExclusiveTypes implements StepCheck {
        @Override
        public boolean test(IStep<?, ?> step) {
            if (step.p() == null) {
                return true;
            }
            Class<?> preType = step.p().getTypeClass();
            boolean a = checkType(preType);
            boolean b = preType.isAnnotationPresent(Exclusive.class);
            return (a && b) || (!a && !b);
        }

        private boolean checkType(Class<?> type) {
            Set<Class<?>> traversed = new HashSet<>();
            Set<Method> exclusiveMethods = new HashSet<>();
            checkTypeLookingAtAllMethods(type, exclusiveMethods);
            return !exclusiveMethods.isEmpty();
        }

        private void checkTypeLookingAtAllMethods(Class<?> type, Set<Method> exclusiveMethods) {
            for (Method m : ReflectUtils.getAllMethodsAccessibleFromObject(type)) {
                if (m.isAnnotationPresent(Exclusive.class) &&
                        m.getReturnType().isAnnotationPresent(Exclusive.class) &&
                        m.getParameterCount() == 0 &&
                        Modifier.isPublic(m.getModifiers())) {
                    exclusiveMethods.add(m);
                }
            }
        }
    }