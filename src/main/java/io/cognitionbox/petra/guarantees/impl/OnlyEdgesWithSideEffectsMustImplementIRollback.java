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

import io.cognitionbox.petra.guarantees.StepCheck;
import io.cognitionbox.petra.lang.AbstractJoin;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.RGraph;
import io.cognitionbox.petra.core.IMaybeEffect;
import io.cognitionbox.petra.core.IRollback;
import io.cognitionbox.petra.core.IStep;

// No need for capture and rollback if we say developer must implement PEdge such that
    // it can only produce P or Q, whether or not C completes successfully.
    // this can be checked for at runtime.
    public class OnlyEdgesWithSideEffectsMustImplementIRollback implements StepCheck {
        @Override
        public boolean test(IStep<?, ?> step) {
            if (step instanceof PEdge) {
                if (step.getEffectType().isPresent()) {
                    return step instanceof IRollback;
                } else {
                    return !(step instanceof IRollback);
                }
            } else if (step instanceof RGraph) {
                boolean ok = true;
                for (Object jt : ((RGraph) step).getJoinTypes()) {
                    if (jt instanceof IMaybeEffect && jt instanceof AbstractJoin) {
                        if (((AbstractJoin) jt).getEffectType().isPresent()) {
                            ok = ok && (jt instanceof IRollback);
                        } else {
                            ok = ok && !(jt instanceof IRollback);
                        }
                    }
                }
                return ok;
            }
            return true;
        }
    }