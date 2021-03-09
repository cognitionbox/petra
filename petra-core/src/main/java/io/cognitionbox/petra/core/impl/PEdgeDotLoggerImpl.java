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
package io.cognitionbox.petra.core.impl;

import io.cognitionbox.petra.core.IEdgeDotLogger;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.lang.PGraph;

public class PEdgeDotLoggerImpl implements IEdgeDotLogger {

    private StringBuilder diagram = new StringBuilder();

    @Override
    public void logCompletedStep(IStep from, IStep to) {
        if (from.getPartitionKey().contains("extractThrowablesStep") ||
                to.getPartitionKey().contains("extractThrowablesStep")) {
            return;
        }
        diagram.append(from + "->" + to + " [style=dotted, label=owns];\n");
        if (to instanceof PGraph) {
            diagram.append(((PGraph) to).getTransitions());
        }
    }

    @Override
    public void logJoin(IStep from, int joinIndex) {
        diagram.append(from + "_join_" + joinIndex + " [shape=rect style=filled, fillcolor=orange fontcolor=black];\n");
        diagram.append(from + "->" + from + "_join_" + joinIndex + " [style=dotted, label=owns];\n");
    }

    @Override
    public String getTransitions() {
        return diagram.toString();
    }
}
