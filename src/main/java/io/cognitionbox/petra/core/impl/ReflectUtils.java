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
package io.cognitionbox.petra.core.impl;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class ReflectUtils {
    static public <T> Set<Field> getAllFieldsAccessibleFromObject(Class<?> t) {
        Set<Field> fields = new HashSet<>();
        if (t.isPrimitive() || t.isArray()){
            return fields;
        }
        Class clazz = t;
        String packageNameA = clazz.getName().replaceAll(clazz.getSimpleName(),"");
        packageNameA = packageNameA.substring(0,packageNameA.length()-1);
        while (clazz!=null && clazz != Object.class) {
            for (Field f : clazz.getDeclaredFields()){
                fields.add(f);
            }
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    static public <T> Set<Method> getAllMethodsAccessibleFromObject(Class<?> t) {
        Set<Method> methods = new CopyOnWriteArraySet<>();
        if (t.isPrimitive() || t.isArray()){
            return methods;
        }
        Class clazz = t;
        String packageNameA = clazz.getName().replaceAll(clazz.getSimpleName(),"");
        packageNameA = packageNameA.substring(0,packageNameA.length()-1);
        while (clazz!=null && clazz != Object.class) {
            for (Method f : clazz.getDeclaredMethods()){
                String packageNameB = f.getDeclaringClass().getName().replaceAll(f.getDeclaringClass().getSimpleName(),"");
                packageNameB = packageNameB.substring(0,packageNameB.length()-1);
                if (Modifier.isPublic(f.getModifiers())){
                    methods.add(f);
                } else if (packageNameA.equals(packageNameB) && !Modifier.isPrivate(f.getModifiers())){
                    methods.add(f);
                }
            }
            clazz = clazz.getSuperclass();
        }

        // removes more generic methods that clash signatures as java api doesnt have way of
        // filtering out abstract methods on declared methods etc.
        for (Method m1 : methods){
            for (Method m2 : methods){
                if (!m1.equals(m2) && isSameMethodSignature(m1,m2)){
                    if (m1.getReturnType().isAssignableFrom(m2.getReturnType())){
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

    private static boolean isSameMethodSignature(Method m1, Method m2){
        return m1.getName().equals(m2.getName()) &&
                m1.getParameterCount()==(m2.getParameterCount()) &&
                Arrays.deepEquals(m1.getParameterTypes(),m2.getParameterTypes()) &&
                (m1.getReturnType().isAssignableFrom(m2.getReturnType()) || m2.getReturnType().isAssignableFrom(m1.getReturnType())) ;
    }

    public static <T> Set<Class<? extends T>> getAllSubTypes(Class<T> tClass){
        Reflections reflections = new Reflections(tClass.getPackage().getName());
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(tClass);
        subTypes.add(tClass);
        return subTypes;
    }

    public static Optional<Class<?>> getCommonSubType(Class<?> x, Class<?> y){
        Set<Class<?>> xTypes = getAllSubTypes((Class<Object>) x);
        Set<Class<?>> yTypes = getAllSubTypes((Class<Object>)y);
        xTypes.retainAll(yTypes);
        return xTypes.stream().sorted((a,b)->{
            if (a.isAssignableFrom(b) && b.isAssignableFrom(a)){
                return 0;
            } else if (a.isAssignableFrom(b)){
                return -1;
            } else if (b.isAssignableFrom(a)){
                return 1;
            }
            return 0;
        }).findFirst();
    }

    public static boolean hasCommonSubType(Class<?> a, Class<?> b){
        return getCommonSubType(a,b).isPresent();
    }
}
