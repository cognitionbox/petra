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
package io.cognitionbox.petra.exceptions;

import io.cognitionbox.petra.core.IStep;

import java.util.List;

public class PetraException extends RuntimeException{

    private IStep step;
    private Object input;
    private Object output;

    public Object getInput() {
        return input;
    }

    public Object getOutput() {
        return output;
    }

    @Override
    public Throwable getCause() {
        return causes.get(0);
    }

    public List<Throwable> getCauses() {
        return causes;
    }

    private List<Throwable> causes;
    public PetraException(IStep step, Object input, Object output, List<Throwable> causes){
        this.step = step;
        this.input = input;
        this.output = output;
        this.causes = causes;
    }

    @Override
    public String toString() {
        return "PetraException{" +
                "step=" + step +
                ", input=" + input +
                ", output=" + output +
                ", causes=" + causes +
                '}';
    }
}
