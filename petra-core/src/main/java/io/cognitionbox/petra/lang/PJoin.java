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

import io.cognitionbox.petra.lang.annotations.Pure;
import io.cognitionbox.petra.core.IJoin1;
import io.cognitionbox.petra.util.function.IFunction;

import java.util.List;

@Pure
public class PJoin<A,R> extends AbstractJoin1<A,R> implements IJoin1<A, R> {

    private long millisBeforeRetry = 100;

    public PJoin(){}
    public PJoin(Guard<? super A> a, IFunction<List<A>, R> function, Guard<? super R> r) {
        super(a, function, r);
    }

    public long getMillisBeforeRetry() {
        return millisBeforeRetry;
    }

    protected void setMillisBeforeRetry(long millisBeforeRetry) {
        this.millisBeforeRetry = millisBeforeRetry;
    }

}
