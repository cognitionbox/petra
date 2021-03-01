package io.cognitionbox.petra.ktln

import io.cognitionbox.petra.lang.PEdge
import io.cognitionbox.petra.lang.PGraph
import kotlin.reflect.KClass

open class KEdge<X:Any> : PEdge<X>() {
    fun type(klass:KClass<X>){
        super.type(klass.java);
    }
}