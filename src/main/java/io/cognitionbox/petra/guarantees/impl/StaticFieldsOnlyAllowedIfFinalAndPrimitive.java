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
import io.cognitionbox.petra.core.IStep;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class StaticFieldsOnlyAllowedIfFinalAndPrimitive implements StepCheck {
        @Override
        public boolean test(IStep<?, ?> step) {
            if (step.p() == null) {
                return true;
            }
            Class<?> preType = step.p().getTypeClass();

            if (step.q() == null) {
                return true;
            }
            Class<?> postType = step.q().getTypeClass();

            return checkTypeRecursively(preType) && checkTypeRecursively(postType);
        }

        private boolean checkTypeRecursively(Class<?> type) {
            if ((type.isPrimitive() ||
                    io.cognitionbox.petra.lang.Void.class.equals(type) ||
                    java.lang.Void.class.equals(type) ||
                    String.class.isAssignableFrom(type) ||
                    Boolean.class.isAssignableFrom(type) ||
                    Number.class.isAssignableFrom(type))) {
                return true;
            }
            for (Field f : ReflectUtils.getAllFieldsAccessibleFromObject(type)) {
                if (Modifier.isStatic(f.getModifiers())) {
                    boolean finalAndPrimtive = Modifier.isFinal(f.getModifiers()) &&
                            (f.getType().isPrimitive() ||
                                    Void.class.equals(f.getType()) ||
                                    java.lang.Void.class.equals(type) ||
                                    String.class.isAssignableFrom(f.getType()) ||
                                    Boolean.class.isAssignableFrom(f.getType()) ||
                                    Number.class.isAssignableFrom(f.getType()));
                    if (finalAndPrimtive || f.getName().equals("$jacocoData")) {
                        // then ok
                    } else {
                        return false;
                    }
                }
                checkTypeRecursively(f.getType());
            }
            return true;
        }
    }