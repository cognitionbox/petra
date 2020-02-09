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

import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.core.IRingbuffer;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class StepWorker implements Callable<Boolean>, Serializable {
    private static final long serialVersionUID = 3L;
    public volatile boolean done = false;

    @Override
    public Boolean call() throws Exception {
        long seq = 0;
        IRingbuffer<Serializable> tasks = RGraphComputer.getTaskQueue();
        int count = 0;
        while(true){//count<10){
            if (seq<=tasks.tailSequence()){
                Serializable read = tasks.readOne(seq);
                if (read instanceof StepCallable){
                    if (read!=null && !((StepCallable) read).isDone()){
                        try {
                            ((ThreadPoolExecutor) RGraphComputer.getWorkerExecutor())
                                    .setRejectedExecutionHandler(new RejectedExecutionHandler() {
                                        @Override
                                        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                                            RGraphComputer.getTaskQueue().add(read);
                                        }
                                    });
                            RGraphComputer.getWorkerExecutor().submit(((StepCallable) read)); // .get() to see errors
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                seq++;
                if (seq>=tasks.capacity()){
                    seq = 0;
                }
            } else {
                Thread.sleep(20);
            }
            count++;
        }
        //return true;
    }
}
