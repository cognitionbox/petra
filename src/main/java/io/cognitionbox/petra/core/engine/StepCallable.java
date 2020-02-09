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
                if (!done.get()){
                    Object out = step.call();
                    result.set(new StepResult(step.p().getOperationType(), step.getInput(),out));
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
