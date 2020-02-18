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
package io.cognitionbox.petra.core.engine.extractors.impl;

import io.cognitionbox.petra.core.engine.extractors.ObjectGraphExtractor;
import io.cognitionbox.petra.core.engine.petri.Place;
import io.cognitionbox.petra.core.impl.ReflectUtils;
import io.cognitionbox.petra.lang.annotations.Extract;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class BaseObjectGraphExtractor<E>  extends AbstractValueExtractor<E> implements ObjectGraphExtractor<E> {
    @Override
    public void extractToPlace(Object value, Place place) {
        for (Method m : ReflectUtils.getAllMethodsAccessibleFromObject(value.getClass())) {
            try {
                m.setAccessible(true);
                if (m.isAnnotationPresent(Extract.class) &&
                        m.getParameterCount() == 0 &&
                        Modifier.isPublic(m.getModifiers())) {
                    try {
                        Object field = m.invoke(value);
                        AbstractRootValueExtractor extractor = new SequentialRootValueExtractor();
                        extractor.extractToPlace(field,place);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
