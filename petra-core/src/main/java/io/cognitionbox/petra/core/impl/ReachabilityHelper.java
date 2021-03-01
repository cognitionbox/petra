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

import io.cognitionbox.petra.guarantees.impl.ClassesWithExtractsOnFieldsMustHaveExtractAnnotation;
import io.cognitionbox.petra.guarantees.impl.ExtractsAtClassLevelMustBeAppliedOnlyToIterablesOrClassesWhichHaveExtractOnFields;
import io.cognitionbox.petra.lang.Guard;
import io.cognitionbox.petra.lang.Ref;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.lang.annotations.SharedResource;

import io.cognitionbox.petra.core.IStep;
import org.javatuples.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ReachabilityHelper {

    private boolean isDefaultAccess(Class<?> clazz){
        return !Modifier.isPrivate(clazz.getModifiers()) &&
                !Modifier.isPublic(clazz.getModifiers()) &&
                !Modifier.isProtected(clazz.getModifiers());
    }

    private Guard<?> copyPTypeWithClass(Guard<?> type, Class<?> clazz){
        return type.copyWithClass(clazz);
    }

    public void addState(Class<?> type, Set<Class<?>> types){
        if (!Void.class.equals(type)){
            types.add(type);
        }
    }

    private boolean extractOnFieldOk(Class<?> clazz){
        for (Field f : ReflectUtils.getAllFieldsAccessibleFromObject(clazz)){
            if (f.isAnnotationPresent(Extract.class)){
                if (clazz.isAnnotationPresent(Extract.class)){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean extractOnClassOk(Class<?> clazz){
        if (!clazz.isAnnotationPresent(Extract.class)){
            return true;
        }
        if (Iterable.class.isAssignableFrom(clazz) || Collection.class.isAssignableFrom(clazz)){
            return true;
        }
        for (Field f : ReflectUtils.getAllFieldsAccessibleFromObject(clazz)){
            if (f.isAnnotationPresent(Extract.class)){
                return true;
            }
        }
        return false;
    }

    public void deconstruct(Set<Class<?>> resourceTypes, OperationType opp, Class<?> type, Set<Class<?>> types, int depth){
        deconstructImpl(resourceTypes, opp, type, types, depth);
        Extract ext = type.getAnnotation(Extract.class);
        if (ext!=null){
            addState(type,types);
        }
    }
    private void deconstructImpl(Set<Class<?>> resourceTypes, OperationType opp, Class<?> type, Set<Class<?>> types, int depth){
        if (!ExtractsAtClassLevelMustBeAppliedOnlyToIterablesOrClassesWhichHaveExtractOnFields.extractOnClassOk(type)){
            throw new AssertionError("Extracts at class level must be applied only to iterables or classes which have extract on fields.");
        }
        if (!ClassesWithExtractsOnFieldsMustHaveExtractAnnotation.extractOnFieldOk(type)){
            throw new AssertionError("Classes with Extracts on fields must also have an Extract annotation.");
        }
        if (type.getAnnotationsByType(Extract.class).length>0){
            types.remove(type);
            if (Collection.class.isAssignableFrom(type)){
                java.lang.reflect.Type t = type.getGenericSuperclass();
                if (t instanceof ParameterizedType){
                    java.lang.reflect.Type g = ((ParameterizedType) t).getActualTypeArguments()[0];
                    Class<?> selectedClazz = (Class<?>) g;
                    if (selectedClazz.isAnnotationPresent(SharedResource.class)){
                        throw new ResourceConflictDetected(selectedClazz,"Cannot operate on more than 1 of the same SharedResource at a time.");
                    }
                    deconstruct(resourceTypes,null,selectedClazz,types,depth+1);
                }
            } else {
                int fieldIndex = 0;
                for (Method m : ReflectUtils.getAllMethodsAccessibleFromObject(type)){
                    // check to prevent parent class type from being added.
                    // need to improve this, have seen an issue in kotlin code
                    //if (!type.getName().contains(f.getType().getName())){
                    if (m.isAnnotationPresent(Extract.class) &&
                            m.getParameterCount()==0 &&
                            Modifier.isPublic(m.getModifiers())){
                        Class<?> selectedClazz = null;
                        if (m.getReturnType().equals(Ref.class)){
                            ParameterizedType pt = (ParameterizedType) m.getGenericReturnType();
                            selectedClazz = (Class<?>) pt.getActualTypeArguments()[0];
                            deconstruct(resourceTypes,null,selectedClazz,types,depth+1);
                            Extract ext = m.getAnnotation(Extract.class);
                            if (ext!=null){
                                addState(type,types);
                            }
                        } else {
                            selectedClazz = m.getReturnType();
                            if (selectedClazz.isAnnotationPresent(SharedResource.class)){
                                if (resourceTypes.contains(selectedClazz)){
                                    throw new ResourceConflictDetected(selectedClazz,"Cannot operate on more than 1 of the same SharedResource at a time.");
                                } else {
                                    resourceTypes.add(selectedClazz);
                                }
                            }
                            deconstruct(resourceTypes,null,selectedClazz,types,depth+1);
                            Extract ext = m.getAnnotation(Extract.class);
                            if (ext!=null){
                                addState(type,types);
                            }
                        }

                    }
                    //}
                }
            }
        } else {
            addState(type,types);
        }

//        if (type.isAnnotationPresent(Extract.class) && opp!=null && opp==OperationType.READ){
//            addState(type,types);
//        }

    }

    public static class ResourceConflictDetected extends RuntimeException {
        private Class<?> resourceClass = null;
        ResourceConflictDetected(Class<?> resourceClass, String msg){
            super(msg);
            this.resourceClass = resourceClass;
        }
        @Override
        public String toString() {
            return "ResourceConflictDetected{" +
                    "resourceClass=" + resourceClass +
                    '}';
        }
    }
}
