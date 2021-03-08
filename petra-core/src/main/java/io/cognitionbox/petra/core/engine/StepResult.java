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
package io.cognitionbox.petra.core.engine;

import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.core.impl.OperationType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

public final class StepResult implements Serializable {

    private final OperationType operationType;
    private final IToken input;
    private final IToken outputValue;

    public StepResult(OperationType operationType, IToken input, IToken outputValue) {
        this.operationType = operationType;
        this.input = input;
        this.outputValue = outputValue;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public IToken getInput() {
        return input;
    }

    public IToken getOutputValue() {
        return outputValue;
    }

}
