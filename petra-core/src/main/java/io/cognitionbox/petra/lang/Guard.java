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
package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.core.impl.ObjectCopyerViaSerialization;
import io.cognitionbox.petra.core.impl.OperationType;
import io.cognitionbox.petra.exceptions.TypeEvalException;
import io.cognitionbox.petra.util.function.IPredicate;

import java.io.Serializable;

import static io.cognitionbox.petra.lang.Void.vd;

public class Guard<E> implements IPredicate<E> {

    private final ObjectCopyerViaSerialization copyer = new ObjectCopyerViaSerialization();

    protected final OperationType operationType;
    protected final Class<E> eventClazz;
    protected final IPredicate<Object> predicate;

    public Guard(Class<E> eventClazz) {
        this(eventClazz, x -> true, OperationType.READ_WRITE);
    }

    public Guard(Class<E> eventClazz, IPredicate<E> predicate, OperationType operationType) {
        assertNotNull(eventClazz);
        assertNotNull(predicate);
        this.eventClazz = eventClazz;
        this.predicate = (IPredicate<Object>) predicate;
        this.operationType = operationType;
    }

    public boolean isVoid() {
        return getTypeClass().equals(Void.class);
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public Guard<E> copy() {
        return new Guard(eventClazz, predicate, operationType);
    }

    public Guard<E> copyWithClass(Class<?> clazz) {
        return new Guard(clazz, predicate, operationType);
    }

    public IPredicate<Object> getPredicate() {
        return predicate;
    }

    private void assertNotNull(Object o) {
        if (o == null) {
            throw new UnsupportedOperationException("cannot be null");
        }
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
        if (x == null && !Void.class.equals(eventClazz)) {
            return false;
        }
        /*
         * Arpad's Kotlin Hack
         */
        if (this.eventClazz.equals(int.class) && Integer.class.isInstance(x)) {
            try {
                return this.predicate.test(x);
            } catch (Exception e) {
                throw new TypeEvalException(e);
            }
        }

        // allows nulls to be return, allowing for an "option" rw
        if (x == null || x == vd) {
            try {
                return this.predicate.test(x);
            } catch (IllegalArgumentException e) {
                // for kotlin support
                if (e.getMessage().contains("Parameter specified as non-null is null")) {
                    return true;
                }
            } catch (Exception e) {
                throw new TypeEvalException(e);
            }
            return false;
        }

        if (this.eventClazz.isInstance(x)) {
            try {
                Object xToUse = x;
                if (RGraphComputer.getConfig().isDefensiveCopyAllInputs()) {
                    try {
                        xToUse = copyer.copy((Serializable) x);
                    } catch (Exception e) {
                        return false;
                    }
                }
                return this.predicate.test(xToUse);
            } catch (Exception e) {
                throw new TypeEvalException(e);
            }
        }
        return false;
    }

}
