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
import io.cognitionbox.petra.guarantees.StepCheck;
import io.cognitionbox.petra.core.IJoin;
import io.cognitionbox.petra.core.IStep;

import java.util.HashSet;
import java.util.Set;

public class NoJoinsCanHaveSameInputPreconditionTypes implements StepCheck {
        @Override
        public boolean test(IStep<?> graphSafe) {
            if (graphSafe instanceof RGraph) {
                for (IJoin jt : ((RGraph<?, ?>) graphSafe).getJoinTypes()) {
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
                if (((RGraph<?, ?>) graphSafe).getJoinTypes().isEmpty()) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        }
    }