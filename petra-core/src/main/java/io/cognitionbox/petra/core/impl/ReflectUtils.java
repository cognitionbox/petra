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
package io.cognitionbox.petra.core.impl;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ReflectUtils {

    public static class ObjectNotNullStringNotEmptyAndValidFloatingPoint implements Predicate<Object>{

        @Override
        public boolean test(Object v) {
            if (v==null){
                return false;
            }
            if (v instanceof String){
                return !((String) v).isEmpty();
            } else if (v instanceof Float){
                Float f = ((Float) v);
                return Float.isFinite(f.floatValue()) && !Float.isInfinite(f.floatValue()) && !Float.isNaN(f.floatValue());
            } else if (v instanceof Double){
                Double d = ((Double) v);
                return Double.isFinite(d.floatValue()) && !Double.isInfinite(d.floatValue()) && !Double.isNaN(d.floatValue());
            }
            return true;
        }
    }

    static public boolean isAllObjectGraphFieldsValid(Object object, Predicate validator) {
        for (Field f : getAllFieldsAccessibleFromObject(object.getClass())){
            Object fieldValue = null;
            try {
                boolean accessible = f.isAccessible();
                f.setAccessible(true);
                fieldValue = f.get(object);
                f.setAccessible(accessible);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("could not complete validation of object.");
            }
            if (fieldValue instanceof Iterable){
                for (Object o : (Iterable)fieldValue){
                    if (!validator.test(o)) {
                        return false;
                    }
                }
            } else if (!validator.test(fieldValue)){
                return false;
            }
            if (!(fieldValue instanceof Number || fieldValue instanceof String || fieldValue instanceof Boolean)){
                return isAllObjectGraphFieldsValid(fieldValue,validator);
            }
        }
        return true;
    }

    static public <T> void actionAllFieldsAccessibleFromObjectInstance(Object value, BiConsumer<Field,Object> fieldConsumer) {
            if (value==null){
                return;
            }
            if (Boolean.class.isAssignableFrom(value.getClass()) || String.class.isAssignableFrom(value.getClass()) || Integer.class.isAssignableFrom(value.getClass())){
                return;
            }
            Set<Field> fields = getAllFieldsAccessibleFromObject(value.getClass());
            for (Field f : fields){
                if (!(Boolean.class.isAssignableFrom(value.getClass()) || String.class.isAssignableFrom(value.getClass()) || Integer.class.isAssignableFrom(value.getClass()))){
                    if (!Modifier.isStatic(f.getModifiers())){
                        try {
                            boolean isAccessable = f.isAccessible();
                            f.setAccessible(true);
                            Object v = f.get(value);
                            actionAllFieldsAccessibleFromObjectInstance(v,fieldConsumer);
                            f.setAccessible(isAccessable);
                            fieldConsumer.accept(f,value);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    }

    static public <T> void actionAllFieldsAccessibleFromObjectGraph(Class<?> t, Consumer<Field> fieldConsumer) {
        if (!(Boolean.class.isAssignableFrom(t) || String.class.isAssignableFrom(t) || Integer.class.isAssignableFrom(t))){
            Set<Field> fields = getAllFieldsAccessibleFromObject(t);
            for (Field f : fields){
                actionAllFieldsAccessibleFromObjectGraph(f.getType(),fieldConsumer);
                fieldConsumer.accept(f);
            }
        }
    }

    static public <T> Set<Field> getAllFieldsAccessibleFromObjectGraph(Class<?> t) {
        Set<Field> allFields = new HashSet<>();
        addAllFieldsAccessibleFromObjectGraph(t,allFields);
        return allFields;
    }

    private static <T> void addAllFieldsAccessibleFromObjectGraph(Class<?> t, Set<Field> allFields) {
        if (!(Boolean.class.isAssignableFrom(t) || String.class.isAssignableFrom(t) || Integer.class.isAssignableFrom(t))){
            Set<Field> fields = getAllFieldsAccessibleFromObject(t);
            for (Field f : fields){
                addAllFieldsAccessibleFromObjectGraph(f.getType(),allFields);
            }
            allFields.addAll(fields);
        }
    }

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
