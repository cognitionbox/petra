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

import io.cognitionbox.petra.google.Optional;
import io.cognitionbox.petra.core.IJoin;
import io.cognitionbox.petra.core.IMaybeEffect;
import io.cognitionbox.petra.util.function.IPredicate;
import io.cognitionbox.petra.util.PetraUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJoin<R> implements IJoin, IMaybeEffect {

    AbstractJoin(Guard<? super R> r) {
        this.r = r;
    }

    protected AbstractJoin() {}

    List<Guard<?>> getInputTypes() {
        return inputTypes;
    }

    private List<Guard<?>> inputTypes = new ArrayList<>();

    void addInputType(Guard<?> guard){
        inputTypes.add(guard);
    }

    private Class<? extends AbstractJoin> clazz = this.getClass();

    Class<? extends AbstractJoin> getJoinClazz() {
        return clazz;
    }

    void setJoinClazz(Class<? extends AbstractJoin> aClass) {
        this.clazz = aClass;
    }

    private long millisBeforeRetry = 100;
    public long getMillisBeforeRetry() {
        return millisBeforeRetry;
    }

    protected void setMillisBeforeRetry(long millisBeforeRetry) {
        this.millisBeforeRetry = millisBeforeRetry;
    }

    void setEffectType(Optional<Class<?>> effectType) {
        this.effectType = effectType;
    }

    private Optional<Class<?>> effectType = null;
    @Override
    public Optional<Class<?>> getEffectType() {
        if (effectType==null){
            effectType = PetraUtils.getEffectTypeForJoinWithInputTypesAndReturnType(inputTypes,r);
        }
        return effectType;
    }

    Guard<? super R> r;
    public Guard<? super R> r() {
        return r;
    }

    public void post(GuardReturn<? super R> r) {
        this.r = r;
    }
    public void post(Class<? super R> clazz, IPredicate<? super R> predicate) {
        post(new GuardReturn(clazz,predicate));
    }

    public void postVoid() {
        post(new GuardReturn(Void.class, x->true));
    }
}
