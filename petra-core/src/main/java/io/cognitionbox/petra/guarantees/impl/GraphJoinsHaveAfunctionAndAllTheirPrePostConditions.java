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
import io.cognitionbox.petra.lang.PJoin3;
import io.cognitionbox.petra.lang.PJoin;
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