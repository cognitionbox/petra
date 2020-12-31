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
package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.exceptions.GraphException;
import io.cognitionbox.petra.exceptions.conditions.PostConditionFailure;
import io.cognitionbox.petra.lang.annotations.DoesNotTerminate;
import io.cognitionbox.petra.util.function.IFunction;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CGraph<X extends PIterableCollection<Y>,Y> extends PGraph<X> {

    public IFunction<X, Collection<Y>> collection() {
        return collection;
    }

    private IFunction<X,Collection<Y>> collection;

    public CGraph(){}
    public CGraph(String partitionKey) {
        super(partitionKey);
    }

    final public void collection(IFunction<X,Collection<Y>> collection) {
         this.collection = collection;
     }

    X executeMatchingLoopUntilPostCondition() {

        currentIteration = 0;
        X out = null;

        boolean doesNotTerminate = getStepClazz().isAnnotationPresent(DoesNotTerminate.class);

        if (!(setActiveKase(getInput().getValue()))){
            return out;
        }

        if (this.collection!=null){
            // use in while loop to prevent termination.
            for (Y y : this.collection.apply(getInput().getValue())) {
                if (iterationTimer!=null && !iterationTimer.periodHasPassed(LocalDateTime.now())){
                    continue;
                }
                this.getInput().getValue().setIterationValue(y);
                iterationId.getAndIncrement();
                currentIteration++;
                try {
                    Lg();
                    iteration();
                    List<Throwable> exceptions = exceptions();
                    if (!exceptions.isEmpty()) {
                        return (X) new GraphException(this,(X) this.getInput().getValue(), null, exceptions);
                    }
                    // breach of loop invariant
                    if (!this.loopInvariant.test(getInput().getValue())) {
                        return (X) new GraphException(this,(X) this.getInput().getValue(), null, Arrays.asList(new IllegalStateException("invariant broken.")));
                    }

                    // post con check for non terminating processes
                    if (this.getStepClazz().isAnnotationPresent(DoesNotTerminate.class) &&
                            !getActiveKase().q(getInput().getValue())) {
                        return (X) new GraphException(this,(X) this.getInput().getValue(), null, Arrays.asList(new IllegalStateException("cycle not correct.")));
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        // post con check for non terminating processes
        if (!getActiveKase().q(getInput().getValue())) {
            return (X) new GraphException(this,(X) this.getInput().getValue(), null, Arrays.asList(new PostConditionFailure()));
        }

//        if (out!=null){
//            return out;
//        }
//        // loop terminated before post condition reached
//        throw new PostConditionFailure();

        return getInput().getValue();
    }
}
