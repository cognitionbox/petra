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
package io.cognitionbox.petra.core.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class ReflectUtils {

    static public <T> Set<Field> getAllNonStaticFieldsAccessibleFromObject(Class<T> t) {
        return getAllFieldsAccessibleFromObject(t).stream()
                .filter(f -> !Modifier.isStatic(f.getModifiers())).collect(Collectors.toSet());
    }

    static public <T> Set<Field> getAllFieldsAccessibleFromObject(Class<T> t) {
        Set<Field> fields = new HashSet<>();
        if (t.isPrimitive() || t.isArray()) {
            return fields;
        }
        Class clazz = t;
        while (clazz != null && clazz != Object.class) {
            for (Field f : clazz.getDeclaredFields()) {
                fields.add(f);
            }
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    static public <T> Set<Method> getAllMethodsAccessibleFromObject(Class<T> t) {
        Set<Method> methods = new CopyOnWriteArraySet<>();
        if (t.isPrimitive() || t.isArray()) {
            return methods;
        }
        Class clazz = t;
        String packageNameA = clazz.getName().replaceAll(clazz.getSimpleName(), "");
        packageNameA = packageNameA.substring(0, packageNameA.length() - 1);
        while (clazz != null && clazz != Object.class) {
            for (Method f : clazz.getDeclaredMethods()) {
                String packageNameB = f.getDeclaringClass().getName().replaceAll(f.getDeclaringClass().getSimpleName(), "");
                packageNameB = packageNameB.substring(0, packageNameB.length() - 1);
                if (Modifier.isPublic(f.getModifiers())) {
                    methods.add(f);
                } else if (packageNameA.equals(packageNameB) && !Modifier.isPrivate(f.getModifiers())) {
                    methods.add(f);
                }
            }
            clazz = clazz.getSuperclass();
        }

        // removes more generic methods that clash signatures as java api doesnt have way of
        // filtering out abstract methods on declared methods etc.
        for (Method m1 : methods) {
            for (Method m2 : methods) {
                if (!m1.equals(m2) && isSameMethodSignature(m1, m2)) {
                    if (m1.getReturnType().isAssignableFrom(m2.getReturnType())) {
                        methods.remove(m1);
                        methods.add(m2);
                    } else {
                        methods.remove(m2);
                        methods.add(m1);
                    }
                }
            }
        }
        return methods;
    }

    private static boolean isSameMethodSignature(Method m1, Method m2) {
        return m1.getName().equals(m2.getName()) &&
                m1.getParameterCount() == (m2.getParameterCount()) &&
                Arrays.deepEquals(m1.getParameterTypes(), m2.getParameterTypes()) &&
                (m1.getReturnType().isAssignableFrom(m2.getReturnType()) || m2.getReturnType().isAssignableFrom(m1.getReturnType()));
    }

}
