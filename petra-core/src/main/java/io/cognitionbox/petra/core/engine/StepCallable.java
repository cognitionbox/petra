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

import io.cognitionbox.petra.core.engine.petri.impl.Token;
import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.lang.AbstractStep;
import io.cognitionbox.petra.lang.RGraph;
import io.cognitionbox.petra.lang.Ref;
import io.cognitionbox.petra.util.function.ICallable;
import io.cognitionbox.petra.util.impl.PLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;

import static io.cognitionbox.petra.util.Petra.ref;

public class StepCallable extends Identifyable implements Callable<StepResult>, Serializable, ICallable<StepResult> {
    final static Logger LOG = LoggerFactory.getLogger(StepCallable.class);
    private static final long serialVersionUID = 1L;
    protected Lock lock = new PLock();
    protected RGraph parent;
    protected AbstractStep step;
    protected Ref<StepResult> result = ref();
    protected Ref<Boolean> done = ref(false);

    public StepCallable(RGraph parent, AbstractStep step) {
        this.parent = parent;
        this.step = step;
    }

    public StepResult get() {
        return result.get();
    }

    @Override
    public StepResult call() throws Exception {
        if (lock.tryLock()) {
            try {
                if (!done.get()) {
                    Object out = step.call();
                    result.set(new StepResult(step.getInput(), new Token(out)));
                    done.set(true);
                    //LOG.info(this.getUniqueId() + ": step processed!");
                }
                return result.get();
            } finally {
                lock.unlock();
            }
        }
        return null;
    }

    public boolean isDone() {
        return done != null && done.get();
    }
}
