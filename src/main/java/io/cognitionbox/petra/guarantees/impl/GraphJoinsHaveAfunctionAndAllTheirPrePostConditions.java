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

public class GraphJoinsHaveAfunctionAndAllTheirPrePostConditions implements Predicate<RGraph<?, ?, ?>> {
        @Override
        public boolean test(RGraph<?, ?, ?> graphSafe) {
            boolean ok = true;
            for (IJoin j : graphSafe.getJoinTypes()) {
                if (j instanceof PJoin) {
                    ok = ok && ((PJoin) j).func() != null;
                    ok = ok && ((PJoin) j).a() != null;
                    ok = ok && ((PJoin) j).r() != null;
                } else if (j instanceof PJoin2) {
                    ok = ok && ((PJoin2) j).func() != null;
                    ok = ok && ((PJoin2) j).a() != null;
                    ok = ok && ((PJoin2) j).b() != null;
                    ok = ok && ((PJoin2) j).r() != null;
                } else if (j instanceof PJoin3) {
                    ok = ok && ((PJoin3) j).func() != null;
                    ok = ok && ((PJoin3) j).a() != null;
                    ok = ok && ((PJoin3) j).b() != null;
                    ok = ok && ((PJoin3) j).c() != null;
                    ok = ok && ((PJoin3) j).r() != null;
                }
            }
            return ok;
        }
    }