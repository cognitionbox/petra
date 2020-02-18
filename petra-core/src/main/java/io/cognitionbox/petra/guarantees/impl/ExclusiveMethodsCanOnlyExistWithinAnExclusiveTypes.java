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