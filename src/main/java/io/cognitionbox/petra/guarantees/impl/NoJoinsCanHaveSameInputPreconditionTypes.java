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
import io.cognitionbox.petra.guarantees.StepCheck;
import io.cognitionbox.petra.core.IJoin;
import io.cognitionbox.petra.core.IStep;

import java.util.HashSet;
import java.util.Set;

public class NoJoinsCanHaveSameInputPreconditionTypes implements StepCheck {
        @Override
        public boolean test(IStep<?, ?> graphSafe) {
            if (graphSafe instanceof RGraph) {
                for (IJoin jt : ((RGraph<?, ?, ?>) graphSafe).getJoinTypes()) {
                    Set<Class<?>> clazzes = new HashSet<>();
                    if (jt instanceof PJoin) {
                        clazzes.add(((PJoin) jt).a().getTypeClass());
                        clazzes.add(((PJoin) jt).r().getTypeClass());
                        if (clazzes.size() == 2) {
                            return true;
                        }
                    } else if (jt instanceof PJoin2) {
                        clazzes.add(((PJoin2) jt).a().getTypeClass());
                        clazzes.add(((PJoin2) jt).b().getTypeClass());
                        clazzes.add(((PJoin2) jt).r().getTypeClass());
                        if (clazzes.size() == 3) {
                            return true;
                        }
                    } else if (jt instanceof PJoin3) {
                        clazzes.add(((PJoin3) jt).a().getTypeClass());
                        clazzes.add(((PJoin3) jt).b().getTypeClass());
                        clazzes.add(((PJoin3) jt).c().getTypeClass());
                        clazzes.add(((PJoin3) jt).r().getTypeClass());
                        if (clazzes.size() == 4) {
                            return true;
                        }
                    }
                }
                if (((RGraph<?, ?, ?>) graphSafe).getJoinTypes().isEmpty()) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }
    }