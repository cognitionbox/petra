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

import io.cognitionbox.petra.factory.PetraParallelComponentsFactory
import io.cognitionbox.petra.ktln.Petra.anonymous
import io.cognitionbox.petra.ktln.Petra.anonymousJ1
import io.cognitionbox.petra.ktln.Petra.rc
import io.cognitionbox.petra.ktln.Petra.rt
import io.cognitionbox.petra.lang.*
import io.cognitionbox.petra.lang.annotations.Extract
import io.cognitionbox.petra.util.impl.PList
import org.junit.Assert.assertEquals
import org.junit.Test

class PetraTest{
    @Test fun test1(){
        RGraphComputer.getConfig().setParallelModeFactory(PetraParallelComponentsFactory());
        RGraphComputer.getConfig().enableStatesLogging()
        RGraphComputer.getConfig().setConstructionGuaranteeChecks(true)
        RGraphComputer.getConfig().setStrictModeExtraConstructionGuarantee(false)

        open class IntList : PList<Int>()
        @Extract
        class IntListEx : IntList()

        class Fibonacci : PGraph<Int, IntList>() {
            init {
                pre(rc(Int::class) { a -> true })
                step(anonymous(Petra.rc(Int::class, { x -> true }), { i: Int ->
                    var il: IntList? = null
                    if (i < 2) {
                        il = IntList()
                        il.add(i)
                    } else {
                        il = IntListEx()
                        il.add(i - 1)
                        il.add(i - 2)
                    }
                    il
                }, rt(IntList::class, { x -> true })))
                joinAll(anonymousJ1(Petra.rc(IntList::class, { i -> i.size === 1 }), { i ->
                    val il = IntList()
                    il.add(i.stream().flatMap({ x -> x.stream() }).mapToInt({ y -> y }).sum())
                    il
                }, rt(IntList::class, { i -> true })))
                post(rt(IntList::class, { i -> i.size === 1 }))
            }
        }

        val lc = PGraphComputer<Int,IntList>()
        val res = lc.computeWithInput(Fibonacci(), 8)
        assertEquals(res[0],21)
    }

    @Test fun test2(){
        RGraphComputer.getConfig().setParallelModeFactory(PetraParallelComponentsFactory());
        RGraphComputer.getConfig().enableStatesLogging()
        RGraphComputer.getConfig().setConstructionGuaranteeChecks(true)
        RGraphComputer.getConfig().setStrictModeExtraConstructionGuarantee(false)

        open class IntList : PList<Int>()
        @Extract
        class IntListEx : IntList()

        class FibSplit : PEdge<Int, IntList>() {
            init {
                pre(rc(Int::class, { i -> true }))
                func { i ->
                    if (i < 2) {
                        val il = IntList()
                        il.add(i)
                        il;
                    } else {
                        val il = IntListEx()
                        il.add(i - 1)
                        il.add(i - 2)
                        il;
                    }
                }
                post(rt(IntList::class, { il -> il.size === 2 }))
                post(rt(IntList::class, { il -> il.size === 1 }))
            }
        }

        class FibJoin : PJoin<IntList, IntList>() {
            init {
                pre(rc(IntList::class, { i -> i.size === 1 }))
                func { i ->
                    val il = IntList()
                    il.add(i.stream().flatMap<Int> { x -> x.stream() }.mapToInt { y -> y }.sum())
                    il
                }
                post(rt(IntList::class, { i -> true }))
            }
        }

        class Fibonacci : PGraph<Int, IntList>() {
            init {
                pre(rc(Int::class, { i -> true }))
                step(FibSplit())
                joinAll(FibJoin())
                post(rt(IntList::class, { i -> i.size === 1 }))
            }
        }

        val lc = PGraphComputer<Int,IntList>()
        val res = lc.computeWithInput(Fibonacci(), 8)
        assertEquals(res[0],21)
    }
}