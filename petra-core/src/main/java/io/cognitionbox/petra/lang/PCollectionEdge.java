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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static io.cognitionbox.petra.util.Petra.throwRandomException;


public class PCollectionEdge<X,Y> extends PEdge<X> {
    public IFunction<X, Collection<Y>> collection() {
        return collection;
    }

    private IFunction<X,Collection<Y>> collection;

    public PCollectionEdge(){}
    public PCollectionEdge(String partitionKey) {
        super(partitionKey);
    }

    final public void collection(IFunction<X,Collection<Y>> collection) {
        this.collection = collection;
    }

    IBiConsumer<X,Y> biConsumer;

    public PEdge<X> func(IBiConsumer<X,Y> biConsumer) {
        this.biConsumer = biConsumer;
        return this;
    }

    @Override
    public void kase(IPredicate<X> pre, IPredicate<X> post) {
        super.kase(x->collection.apply(x).size()==1 && pre.test(x), post);
        super.kase(x->collection.apply(x).size()>1 && pre.test(x), post);
    }

    public IBiConsumer getBiConsumer() {
        return biConsumer;
    }
}
