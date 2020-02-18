/**
 * Copyright 2016-2020 Aran Hakki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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