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
package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.core.impl.ObjectCopyerViaSerialization;
import io.cognitionbox.petra.core.impl.OperationType;
import io.cognitionbox.petra.core.impl.ReflectUtils;
import io.cognitionbox.petra.google.Optional;
import io.cognitionbox.petra.lang.annotations.Exclusive;
import io.cognitionbox.petra.exceptions.TypeEvalException;
import io.cognitionbox.petra.util.function.IPredicate;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.stream.Collectors;

import static io.cognitionbox.petra.lang.Void.vd;

public class Guard<E> implements IPredicate<E> {

    public Guard(){}

    public boolean isVoid(){
        return getTypeClass().equals(Void.class);
    }

    protected OperationType operationType = OperationType.READ_WRITE;

    public OperationType getOperationType() {
        return operationType;
    }

    //Class<? extends E> getEffectType() {
    //    return effectType.get();
    //}

    //private Optional<Class<? extends E>> effectType;

    public Guard<E> copy(){
        return new Guard(eventClazz,predicate,operationType);
    }

    public Guard<E> copyWithClass(Class<?> clazz){
        return new Guard(clazz,predicate,operationType);
    }

    protected Class<E> eventClazz;

    public IPredicate<Object> getPredicate() {
        return predicate;
    }

    protected IPredicate<Object> predicate;

//    public Guard(){}

//    public void where(Class<E> eventClazz){
//        this.eventClazz = eventClazz;
//    }
//
//    public void matches(IPredicate<E> predicate){
//        this.predicate = (IPredicate<Object>) predicate;
//    }

    public Guard(Class<E> eventClazz) {
        this.eventClazz = eventClazz;
        this.predicate = x->true;
        this.operationType = null;
    }

    public Guard(Class<E> eventClazz, IPredicate<E> predicate, OperationType operationType) {
//        assertNotNull(eventClazz);
//        assertNotNull(predicate);
        this.eventClazz = eventClazz;
        this.predicate = (IPredicate<Object>) predicate;
        this.operationType = operationType;
    }

    public Guard(IPredicate predicate, OperationType operationType) {
//        assertNotNull(eventClazz);
//        assertNotNull(predicate);
        this.eventClazz = (Class<E>) Object.class;
        this.predicate = (IPredicate<Object>) predicate;
        this.operationType = operationType;
    }

    public Guard(Optional<Class<? extends E>> effectType) {
        this(null,null);
    }

//    public Guard(IPredicate predicate, Class<? extends PEdge>[] xorReturnTypes) {
//        this(predicate,null,xorReturnTypes);
//    }
//
//    public Guard(IPredicate predicate, Optional<Class<? extends PEdge>> effectType) {
//        this(predicate,effectType,null);
//    }
//
//    public Guard(Class<PEdge> eventClazz, IPredicate<PEdge> predicate, Class<? extends PEdge>[] xorReturnTypes) {
//        this(eventClazz,predicate,null,xorReturnTypes);
//    }
//
//    public Guard(Class<PEdge> eventClazz, IPredicate<PEdge> predicate, Optional<Class<? extends PEdge>> effectType) {
//        this(eventClazz,predicate,effectType,null);
//    }

//    public Guard(Class<PEdge> eventClazz, IPredicate<PEdge> predicate) {
//        this(eventClazz,predicate,null,null);
//    }
//
//    public Guard(Class<PEdge> eventClazz) {
//        this(eventClazz, x -> true);
//    }

    public Guard(IPredicate<Object> predicate) {
        super();
        assertNotNull(predicate);
        this.eventClazz = (Class<E>) Object.class;
        this.predicate = predicate;
    }

    private void assertNotNull(Object o) {
        if (o == null) {
            throw new UnsupportedOperationException("cannot be null");
        }
    }

    Guard<Object> generic() {
        return (Guard<Object>) this;
    }

    public Class<E> getTypeClass() {
        return eventClazz;
    }

    @Override
    public boolean test(Object value) {
        Object x = value;
//        if (rw instanceof Ref){
//            x = ((Ref) rw).get();
//        }
        if (x==null && !Void.class.equals(eventClazz)){
            return false;
        }
        /*
         * Arpad's Kotlin Hack
         */
        if (this.eventClazz.equals(int.class) && Integer.class.isInstance(x)) {
            try {
                return this.predicate.test(x);
            } catch (Exception e){
                throw new TypeEvalException(e);
            }
        }

        // allows nulls to be return, allowing for an "option" rw
        if (x==null || x== vd){
            try {
                return this.predicate.test(x);
            } catch (IllegalArgumentException e){
                // for kotlin support
                if (e.getMessage().contains("Parameter specified as non-null is null")){
                    return true;
                }
            } catch (Exception e){
                throw new TypeEvalException(e);
            }
            return false;
        }

        if (this.eventClazz.isInstance(x)) {
            Set<Class<?>> classesLockKey = null;
            try {
                Object xToUse = x;
                if (operationType==OperationType.READ_WRITE){// && this.eventClazz.isAnnotationPresent(Exclusive.class)){
                    classesLockKey =
                            ReflectUtils.getAllMethodsAccessibleFromObject(this.eventClazz)
                                    .stream()
                                    .filter(m->m.isAnnotationPresent(Exclusive.class) &&
                                                m.getReturnType().isAnnotationPresent(Exclusive.class) &&
                                                    m.getParameterCount()==0 &&
                                                        Modifier.isPublic(m.getModifiers()))
                                    .map(m->m.getReturnType())
                                    .collect(Collectors.toSet());
                    if (classesLockKey!=null && !classesLockKey.isEmpty() && Exclusives.tryAquireExclusive(classesLockKey)) {
                        Exclusives.load(x, getTypeClass());
                    }
                } else

                    if (RGraphComputer.getConfig().isDefensiveCopyAllInputs()){
                    try {
                        xToUse = copyer.copy(x);
                    } catch (Exception e){
                        return false;
                    }
                }
                return this.predicate.test(x);
            } catch (Exception e){
                throw new TypeEvalException(e);
            } finally {
                if (classesLockKey!=null && !classesLockKey.isEmpty() && operationType==OperationType.READ_WRITE){// && this.eventClazz.isAnnotationPresent(Exclusive.class)){
                    Exclusives.returnExclusive(classesLockKey);
                }
            }
        }
        return false;
    }

    private ObjectCopyerViaSerialization copyer = new ObjectCopyerViaSerialization();

}
