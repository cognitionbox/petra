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

    public void addChoice(Guard<? super E> choice){
        choices.add(choice);
    }

    public GuardXOR(OperationType operationType){
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

    public boolean isVoid(){
        return this.choices.size()==1 && choices.get(0).getTypeClass().equals(Void.class);
    }

    private Class<?> commonSubtypeOfChoices = null;
    @Override
    public Class<E> getTypeClass() {
        if (choices.size()==1){
            return (Class<E>) this.choices.get(0).getTypeClass();
        } else {
            if (commonSubtypeOfChoices==null){
                Set<Class<?>> subsToRetainOn = null;
                for (Guard<? super E> c : choices){
                    if (subsToRetainOn==null){
                        subsToRetainOn = ReflectUtils.getAllSubTypes((Class<Object>) c.eventClazz);
                        continue;
                    } else {
                        subsToRetainOn.retainAll(ReflectUtils.getAllSubTypes((Class<Object>) c.eventClazz));
                    }
                }
                 Optional<Class<?>> opt = subsToRetainOn.stream().sorted((a, b)->{
                    if (a.isAssignableFrom(b) && b.isAssignableFrom(a)){
                        return 0;
                    } else if (a.isAssignableFrom(b)){
                        return -1;
                    } else if (b.isAssignableFrom(a)){
                        return 1;
                    }
                    return 0;
                }).findFirst();
                if (opt.isPresent()){
                    commonSubtypeOfChoices = (Class<E>) opt.get();
                }
            }
            return (Class<E>) commonSubtypeOfChoices;
        }
    }
}
