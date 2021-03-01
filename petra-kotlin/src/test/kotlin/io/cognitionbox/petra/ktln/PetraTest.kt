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

import io.cognitionbox.petra.config.ExecMode
import io.cognitionbox.petra.factory.PetraParallelComponentsFactory
import io.cognitionbox.petra.lang.*
import io.cognitionbox.petra.util.Petra.choice
import org.junit.Assert.assertEquals
import org.junit.Test

class PetraTest{
    @Test fun test1(){
        RGraphComputer.getConfig().setMode(ExecMode.PAR)
        RGraphComputer.getConfig().setParallelModeFactory(PetraParallelComponentsFactory())
        RGraphComputer.getConfig().enableStatesLogging()
        RGraphComputer.getConfig().setConstructionGuaranteeChecks(true)
        RGraphComputer.getConfig().setStrictModeExtraConstructionGuarantee(false)

        class Foo(var value:Int){}
        class A : KEdge<Foo>(){
            init {
                type(Foo::class)
                pre({x->x.value==0})
                func({x->x.value++})
                post({x->true})
            }
        }
        class B : KEdge<Foo>(){
            init {
                type(Foo::class)
                pre({x->x.value==1})
                func({x->x.value--})
                post({x->true})
            }
        }
        class FooMachine : KGraph<Foo>(){
            init {
                type(Foo::class)
                pre({x->true})
                begin()
                step(choice(),A())
                step(choice(),B())
                end()
                post({x->true})
            }
        }
        val lc = PComputer<Foo>()
        val res = lc.eval(FooMachine(), Foo(1))
        assertEquals(res.value,0)
    }
}