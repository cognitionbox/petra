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

import io.cognitionbox.petra.core.IRollback;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.lang.AbstractStep;

import java.io.Serializable;

public class PEdgeRollbackHelper implements Serializable {

    private long millisBeforeRetry;

    public PEdgeRollbackHelper(long millisBeforeRetry) {
        this.millisBeforeRetry = millisBeforeRetry;
    }

    private void waitBeforeRetry() {
        try {
            Thread.sleep(this.millisBeforeRetry);
        } catch (Exception e) {
        }
    }

    public void capture(Object input, AbstractStep<?> abstractStep) {
        if (abstractStep instanceof IRollback) {
            while (true) {
                try {
                    IStep step = abstractStep.getStepClazz().newInstance();
                    synchronized (this) {
                        ((IRollback) step).capture(input);
                        break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    waitBeforeRetry();
                }
            }
        }
    }

    public void rollback(Object input, AbstractStep<?> abstractStep) {
        if (abstractStep instanceof IRollback) {
            while (true) {
                try {
                    IStep step = abstractStep.getStepClazz().newInstance();
                    synchronized (this) {
                        ((IRollback) step).rollback(input);
                    }
                    if (abstractStep.p().test(input)) {
                        break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    waitBeforeRetry();
                }
            }
        }
    }
}
