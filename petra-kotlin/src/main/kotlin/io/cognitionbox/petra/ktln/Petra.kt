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
package io.cognitionbox.petra.ktln

import io.cognitionbox.petra.core.impl.OperationType
import io.cognitionbox.petra.lang.*
import io.cognitionbox.petra.util.function.IBiFunction
import io.cognitionbox.petra.util.function.IFunction
import io.cognitionbox.petra.util.function.IPredicate
import io.cognitionbox.petra.util.function.ITriFunction
import kotlin.reflect.KClass

object Petra {

    fun <T:Any> ro(eventClazz: KClass<T>, predicate: (T) -> Boolean): GuardRead<T> {
        return GuardRead(eventClazz.java, predicate)
    }

    fun <T:Any> rw(eventClazz: KClass<T>, predicate: (T) -> Boolean): GuardWrite<T> {
        return GuardWrite(eventClazz.java, predicate)
    }

    fun <T:Any> rc(eventClazz: KClass<T>, predicate: (T) -> Boolean): GuardConsume<T> {
        return GuardConsume(eventClazz.java, predicate)
    }

    fun <T:Any> rt(eventClazz: KClass<T>, predicate: (T) -> Boolean): GuardReturn<T> {
        return GuardReturn(eventClazz.java, predicate)
    }

    fun <I, O> anonymous(p: Guard<I>,
                         function: (I) -> O,
                         vararg qs: Guard<O>): PEdge<I, O> {
        val pTypeXOR = GuardXOR<O>(OperationType.RETURN)
        for (q in qs) {
            pTypeXOR.addChoice(q)
        }
        return PEdge(p, function, pTypeXOR)
    }

    fun <A, R> anonymousJ1(a: Guard<A>, function:(List<A>) -> R, r: Guard<R>): PJoin<A, R> {
        return PJoin(a, function, r)
    }

    fun <A, B, R> anonymousJ2(a: Guard<A>, b: Guard<B>, function:(List<A>,List<B>) -> R, r: Guard<R>): PJoin2<A, B, R> {
        return PJoin2(a, b, function, r)
    }

    fun <A, B, C, R> anonymousJ3(a: Guard<A>, b: Guard<B>, c: Guard<C>, function:(List<A>,List<B>,List<C>) -> R, r: Guard<R>): PJoin3<A, B, C, R> {
        return PJoin3(a, b, c, function, r)
    }

}