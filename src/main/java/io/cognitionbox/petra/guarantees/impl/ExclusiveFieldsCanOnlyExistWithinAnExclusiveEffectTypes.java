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
import io.cognitionbox.petra.lang.Exclusives;
import io.cognitionbox.petra.core.impl.ReflectUtils;
import io.cognitionbox.petra.lang.annotations.Exclusive;
import io.cognitionbox.petra.core.IStep;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class ExclusiveFieldsCanOnlyExistWithinAnExclusiveEffectTypes implements StepCheck {
        @Override
        public boolean test(IStep<?, ?> step) {
            if (step.getEffectType().isPresent()) {
                Class<?> effectType = step.getEffectType().get();
                boolean a = checkType(effectType);
                boolean b = effectType.isAnnotationPresent(Exclusive.class) &&
                        Modifier.isPublic(effectType.getModifiers());
                return (a && b) || (!a && !b);
            } else {
                return true;
            }
        }

        private boolean checkType(Class<?> type) {
            Set<Class<?>> traversed = new HashSet<>();
            Set<Field> exclusiveFields = new HashSet<>();
            checkTypeRecursively(type, exclusiveFields, traversed);
            try {
                Exclusives.register(type, exclusiveFields);
                return !exclusiveFields.isEmpty();
            } catch (Exception e) {
                return false;
            }
        }

        private void checkTypeRecursively(Class<?> type, Set<Field> exclusiveFields, Set<Class<?>> traversed) {
            if ((type.isPrimitive() ||
                    Void.class.equals(type) ||
                    java.lang.Void.class.equals(type) ||
                    String.class.isAssignableFrom(type) ||
                    Boolean.class.isAssignableFrom(type) ||
                    Number.class.isAssignableFrom(type))) {
                return;
            }
            for (Field f : ReflectUtils.getAllFieldsAccessibleFromObject(type)) {
                if (f.isAnnotationPresent(Exclusive.class)) {
                    if (Modifier.isFinal(f.getModifiers())) {
                        exclusiveFields.add(f);
                    } else {
                        throw new IllegalStateException("Exclusive fields must be final");
                    }
                }
                if (!traversed.contains(f.getType())) {
                    traversed.add(f.getType());
                    checkTypeRecursively(f.getType(), exclusiveFields, traversed);
                }
            }
        }
    }