/**
 * Copyright 2016-2020 Aran Hakki
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.guarantees.impl;

import io.cognitionbox.petra.core.IRollback;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.guarantees.StepCheck;
import io.cognitionbox.petra.lang.PEdge;

// No need for capture and rollback if we say developer must implement PEdge such that
// it can only produce P or Q, whether or not C completes successfully.
// this can be checked for at runtime.
public class OnlyEdgesWithSideEffectsMustImplementIRollback implements StepCheck {
    @Override
    public boolean test(IStep<?> step) {
        if (step instanceof PEdge) {
            if (step.getEffectType().isPresent()) {
                return step instanceof IRollback;
            } else {
                return !(step instanceof IRollback);
            }
        }
        return true;
    }
}