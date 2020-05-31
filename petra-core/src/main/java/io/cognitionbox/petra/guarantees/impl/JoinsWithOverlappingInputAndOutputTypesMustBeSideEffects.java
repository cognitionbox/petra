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
package io.cognitionbox.petra.guarantees.impl;

import io.cognitionbox.petra.lang.*;
import io.cognitionbox.petra.lang.PJoin2;
import io.cognitionbox.petra.lang.PJoin;
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

