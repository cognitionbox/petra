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

import io.cognitionbox.petra.google.Optional;
import io.cognitionbox.petra.core.IJoin;
import io.cognitionbox.petra.core.IMaybeEffect;
import io.cognitionbox.petra.util.function.IPredicate;
import io.cognitionbox.petra.util.PetraUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPureJoin<R> extends AbstractJoin implements IJoin, IMaybeEffect {

    AbstractPureJoin(Guard<? super R> r) {
        this.r = r;
    }

    protected AbstractPureJoin() {}

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
