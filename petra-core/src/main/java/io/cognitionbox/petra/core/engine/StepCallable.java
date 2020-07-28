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

import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.core.engine.petri.impl.Token;
import io.cognitionbox.petra.exceptions.EdgeException;
import io.cognitionbox.petra.exceptions.GraphException;
import io.cognitionbox.petra.exceptions.conditions.PostConditionFailure;
import io.cognitionbox.petra.lang.AbstractStep;
import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.lang.RGraph;
import io.cognitionbox.petra.util.function.ICallable;
import io.cognitionbox.petra.lang.Ref;
import io.cognitionbox.petra.util.Petra;
import io.cognitionbox.petra.util.impl.PLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;

import static io.cognitionbox.petra.util.Petra.ref;

public class StepCallable extends Identifyable implements Callable<StepResult>, Serializable, ICallable<StepResult> {
    final static Logger LOG = LoggerFactory.getLogger(StepCallable.class);
    private static final long serialVersionUID = 1L;
    protected RGraph parent;
    protected AbstractStep step;

    public StepCallable(RGraph parent, AbstractStep step) {
        this.parent = parent;
        this.step = step;
    }

    @Override
    public StepResult call() throws Exception {
        try {
            Object out = step.call();
            return new StepResult(step.p().getOperationType(), step.getInput(),new Token(out));
//            if (step instanceof RGraph){
//                if (out instanceof GraphException){
//                    return new StepResult(step.p().getOperationType(), step.getInput(),new Token(out),((GraphException) out).getCauses());
//                } else {
//                    return new StepResult(step.p().getOperationType(), step.getInput(),new Token(out));
//                }
//            } else if (step instanceof IStep){
//                if (out instanceof EdgeException){
//                    return new StepResult(step.p().getOperationType(), step.getInput(),new Token(out),((EdgeException) out).getCause());
//                } else {
//                    return new StepResult(step.p().getOperationType(), step.getInput(),new Token(out));
//                }
//            }
        } catch (Exception e) {
            LOG.error(this.getUniqueId(),e);
        }
        return null;
    }
}
