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
