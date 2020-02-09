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
package io.cognitionbox.petra.guarantees.impl;

import io.cognitionbox.petra.lang.PJoin;
import io.cognitionbox.petra.lang.PJoin2;
import io.cognitionbox.petra.lang.PJoin3;
import io.cognitionbox.petra.lang.RGraph;
import io.cognitionbox.petra.core.IJoin;

import java.util.function.Predicate;

import static io.cognitionbox.petra.guarantees.impl.ConstructionGuarantees.isSideEffect;

public class JoinsWithOverlappingInputAndOutputTypesMustBeSideEffects implements Predicate<RGraph<?, ?, ?>> {
        @Override
        public boolean test(RGraph<?, ?, ?> step) {
            boolean ok = true;
            for (IJoin j : step.getJoinTypes()) {
                if (j instanceof PJoin) {
                    Class<?> a = ((PJoin) j).a().getTypeClass();
                    Class<?> r = ((PJoin) j).r().getTypeClass();
                    ok = ok && ((a.equals(r) && isSideEffect(step)) || !a.equals(r) || !isSideEffect(step));
                } else if (j instanceof PJoin2) {
                    Class<?> a = ((PJoin2) j).a().getTypeClass();
                    Class<?> b = ((PJoin2) j).b().getTypeClass();
                    Class<?> r = ((PJoin2) j).r().getTypeClass();
                    ok = ok && (((a.equals(r) || b.equals(r)) && isSideEffect(step)) || !(a.equals(r) || b.equals(r)) || !isSideEffect(step));
                } else if (j instanceof PJoin3) {
                    Class<?> a = ((PJoin3) j).a().getTypeClass();
                    Class<?> b = ((PJoin3) j).b().getTypeClass();
                    Class<?> c = ((PJoin3) j).c().getTypeClass();
                    Class<?> r = ((PJoin3) j).r().getTypeClass();
                    ok = ok && (((a.equals(r) || b.equals(r) || c.equals(r)) && isSideEffect(step)) || !(a.equals(r) || b.equals(r) || c.equals(r)) || !isSideEffect(step));
                }
            }
            return ok;
        }
    }

