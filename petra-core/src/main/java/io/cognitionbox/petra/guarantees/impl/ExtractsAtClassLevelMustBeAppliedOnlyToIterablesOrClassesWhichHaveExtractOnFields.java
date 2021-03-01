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
        public boolean test(IStep<?> step) {
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