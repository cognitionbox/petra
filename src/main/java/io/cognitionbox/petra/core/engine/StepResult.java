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
package io.cognitionbox.petra.core.engine;

import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.core.impl.OperationType;

import java.io.Serializable;

public final class StepResult implements Serializable {
    private OperationType operationType;
    private IToken input;
    private Object outputValue;

    public StepResult(OperationType operationType, IToken input, Object outputValue) {
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

    public Object getOutputValue() {
        return outputValue;
    }
}
