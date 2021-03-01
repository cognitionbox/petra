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

import io.cognitionbox.petra.core.impl.OperationType;
import io.cognitionbox.petra.core.impl.ReflectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class GuardXOR<E> extends Guard<E> {
    public List<Guard<? super E>> getChoices() {
        return choices;
    }

    private List<Guard<? super E>> choices = new ArrayList<>();

    public void addChoice(Guard<? super E> choice) {
        choices.add(choice);
    }

    public GuardXOR(OperationType operationType) {
        this.eventClazz = (Class<E>) Object.class;
        this.operationType = operationType;
        this.predicate = x -> {
            AtomicInteger matches = new AtomicInteger(0);
            this.choices.stream()
                    .forEach(p -> {
                        if (p.test(x)) {
                            matches.incrementAndGet();
                        }
                    });
            if (matches.get() > 1 || matches.get() == 0) {
                return false;
            } else {
                return true;
            }
        };
        this.operationType = operationType;
    }

    public boolean isVoid() {
        return this.choices.size() == 1 && choices.get(0).getTypeClass().equals(Void.class);
    }

    private Class<?> commonSubtypeOfChoices = null;

    @Override
    public Class<E> getTypeClass() {
        if (choices.size() == 1) {
            return (Class<E>) this.choices.get(0).getTypeClass();
        } else {
            if (commonSubtypeOfChoices == null) {
                Set<Class<?>> subsToRetainOn = null;
                for (Guard<? super E> c : choices) {
                    if (subsToRetainOn == null) {
                        subsToRetainOn = ReflectUtils.getAllSubTypes((Class<Object>) c.eventClazz);
                        continue;
                    } else {
                        subsToRetainOn.retainAll(ReflectUtils.getAllSubTypes((Class<Object>) c.eventClazz));
                    }
                }
                Optional<Class<?>> opt = subsToRetainOn.stream().sorted((a, b) -> {
                    if (a.isAssignableFrom(b) && b.isAssignableFrom(a)) {
                        return 0;
                    } else if (a.isAssignableFrom(b)) {
                        return -1;
                    } else if (b.isAssignableFrom(a)) {
                        return 1;
                    }
                    return 0;
                }).findFirst();
                if (opt.isPresent()) {
                    commonSubtypeOfChoices = (Class<E>) opt.get();
                }
            }
            return (Class<E>) commonSubtypeOfChoices;
        }
    }
}
