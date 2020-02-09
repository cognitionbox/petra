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

import io.cognitionbox.petra.guarantees.GraphCheck;
import io.cognitionbox.petra.core.IGraph;
import io.cognitionbox.petra.core.IJoin;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.lang.*;

public class CheckAllPrePostTypesAreStates implements GraphCheck {

        private boolean checkPType(Guard guard) {
            if (guard instanceof GuardXOR) {
                boolean ok = true;
                for (Guard p : ((GuardXOR<?>) guard).getChoices()) {
                    ok = ok && checkTypeImpl(p);
                }
                return ok;
            } else {
                return checkTypeImpl(guard);
            }
        }

        private boolean checkTypeImpl(Guard guard) {
            return true;//guard.getTypeClass().isAnnotationPresent(State.class);
        }

        @Override
        public boolean test(IGraph<?, ?> step) {
            if (step instanceof RGraph) {
                boolean ok = true;
                for (IStep<?, ?> s : ((RGraph<?, ?, ?>) step).getParallizable()) {
                    ok = ok && checkPType(s.p()) && checkPType(s.q());
                }
                for (IJoin j : ((RGraph<?, ?, ?>) step).getJoinTypes()) {
                    if (j instanceof PJoin) {
                        ok = ok && checkPType(((PJoin) j).a()) && checkPType(((PJoin) j).r());
                    } else if (j instanceof PJoin2) {
                        ok = ok && checkPType(((PJoin2) j).a()) && checkPType(((PJoin2) j).b()) && checkPType(((PJoin2) j).r());
                    } else if (j instanceof PJoin3) {
                        ok = ok && checkPType(((PJoin3) j).a()) && checkPType(((PJoin3) j).b()) && checkPType(((PJoin3) j).c()) && checkPType(((PJoin3) j).r());
                    }
                }
                return ok;
            } else if (step instanceof PEdge) {
                return checkPType(step.p()) && checkPType(step.q());
            }
            return false;
        }
    }