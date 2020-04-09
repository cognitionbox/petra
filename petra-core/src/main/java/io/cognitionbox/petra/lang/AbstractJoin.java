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

import io.cognitionbox.petra.core.IJoin;
import io.cognitionbox.petra.core.IMaybeEffect;
import io.cognitionbox.petra.google.Optional;
import io.cognitionbox.petra.util.PetraUtils;
import io.cognitionbox.petra.util.function.IPredicate;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJoin implements IJoin {

    protected AbstractJoin() {}

    List<Guard<?>> getInputTypes() {
        return inputTypes;
    }

    final protected List<Guard<?>> inputTypes = new ArrayList<>();

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

}
