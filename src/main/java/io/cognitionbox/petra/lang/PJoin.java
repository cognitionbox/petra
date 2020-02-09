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
