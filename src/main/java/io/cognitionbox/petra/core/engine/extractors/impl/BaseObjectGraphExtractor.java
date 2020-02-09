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
