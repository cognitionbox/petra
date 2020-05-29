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

import io.cognitionbox.petra.guarantees.GraphCheck;
import io.cognitionbox.petra.core.IGraph;

public class GraphMustHaveAtLeastOneStepOrJoin implements GraphCheck {
        @Override
        public boolean test(IGraph<?> graphSafe) {
            if (graphSafe.getParallizable().size() > 0 || graphSafe.getNoOfJoins() > 0) {
                return true;
            } else {
                return false;
            }
        }
    }