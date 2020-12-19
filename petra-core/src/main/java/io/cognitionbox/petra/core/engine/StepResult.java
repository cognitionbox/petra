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
package io.cognitionbox.petra.core.engine;

import io.cognitionbox.petra.core.engine.petri.IToken;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

public final class StepResult implements Serializable {
    private IToken input;
    private IToken outputValue;

    public Collection<Throwable> getErrors() {
        return errors;
    }

    private Collection<Throwable> errors;

    public StepResult(IToken input, IToken outputValue) {
        this(input,outputValue, Arrays.asList());
    }

    public StepResult(IToken input, IToken outputValue, Throwable error) {
        this(input,outputValue, Arrays.asList(error));
    }

    public StepResult(IToken input, IToken outputValue, Collection<Throwable> errors) {
        this.input = input;
        this.outputValue = outputValue;
        this.errors = errors;
    }

    public IToken getInput() {
        return input;
    }

    public IToken getOutputValue() {
        return outputValue;
    }
}
