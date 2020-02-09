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
import io.cognitionbox.petra.core.IJoin3;
import io.cognitionbox.petra.util.function.ITriFunction;

import java.util.List;

@Pure
public class PJoin3<A,B,C,R> extends AbstractJoin3<A,B,C,R> implements IJoin3<A, B, C, R> {
    public PJoin3(){}
    public PJoin3(Guard<? super A> a, Guard<? super B> b, Guard<? super C> c, ITriFunction<List<A>, List<B>, List<C>, R> function, Guard<? super R> r) {
        super(a, b, c, function, r);
    }
}