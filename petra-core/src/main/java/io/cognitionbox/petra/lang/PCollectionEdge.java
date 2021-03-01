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

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.core.impl.ObjectCopyerViaSerialization;
import io.cognitionbox.petra.core.impl.PEdgeRollbackHelper;
import io.cognitionbox.petra.exceptions.EdgeException;
import io.cognitionbox.petra.util.function.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static io.cognitionbox.petra.util.Petra.throwRandomException;


public class PCollectionEdge<X,Y,Z> extends PEdge<X> {
    public IFunction<X, Collection<Z>> collection() {
        return collection;
    }
    public IFunction<X,Y> shared() {
        return shared;
    }

    private IFunction<X,Collection<Z>> collection;
    private IFunction<X,Y> shared;

    public PCollectionEdge(){
        setP(new Guard(Object.class));
        setQ(new Guard(Object.class));
    }
    public PCollectionEdge(String partitionKey) {
        super(partitionKey);
    }

    final public void collection(IFunction<X,Collection<Z>> collection) {
        this.collection = collection;
    }

    public ExecMode getExecMode() {
        return execMode;
    }

    private ExecMode execMode;
    final public void shared(ExecMode mode, IFunction<X,Y> shared) {
        this.shared = shared;
        this.execMode = mode;
    }

    IBiConsumer<Y,Z> biConsumer;

    public PEdge<X> func(IBiConsumer<Y,Z> biConsumer) {
        this.biConsumer = biConsumer;
        return this;
    }

    public IBiConsumer getBiConsumer() {
        return biConsumer;
    }

    private IBiPredicate<Y,Z> before;
    private IBiPredicate<Y,Z> after;

    public IBiPredicate<Y,Z> getBefore() {
        return before;
    }

    public IBiPredicate<Y,Z> getAfter() {
        return after;
    }

    public void pre(IBiPredicate<Y,Z> pre) {
        this.before = pre;
    }
    public void post(IBiPredicate<Y,Z> post) {
        this.after = post;
    }
}
