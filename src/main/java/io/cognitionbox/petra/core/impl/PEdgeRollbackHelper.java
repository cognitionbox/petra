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

    private void waitBeforeRetry(){
        try {
            Thread.sleep(this.millisBeforeRetry);
        } catch (Exception e){}
    }

    public void capture(Object input, AbstractStep<?,?> abstractStep){
        if (abstractStep instanceof IRollback){
            while(true){
                try {
                    IStep step = abstractStep.getStepClazz().newInstance();
                    synchronized (this){
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

    public void rollback(Object input, AbstractStep<?,?> abstractStep){
        if (abstractStep instanceof IRollback){
            while(true){
                try {
                    IStep step = abstractStep.getStepClazz().newInstance();
                    synchronized (this){
                        ((IRollback) step).rollback(input);
                    }
                    if (abstractStep.p().test(input)){
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
