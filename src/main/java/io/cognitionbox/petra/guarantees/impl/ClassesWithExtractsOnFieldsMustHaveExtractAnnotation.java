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