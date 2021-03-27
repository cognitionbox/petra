/**
 * Copyright 2016-2020 Aran Hakki
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.guarantees.impl;

import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.core.impl.ReflectUtils;
import io.cognitionbox.petra.guarantees.StepCheck;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class StaticFieldsOnlyAllowedIfFinalAndPrimitive implements StepCheck {
    @Override
    public boolean test(IStep<?> step) {
        if (step.p() == null) {
            return true;
        }
        Class<?> preType = step.getType();

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