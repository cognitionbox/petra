package io.cognitionbox.petra.ktln

import io.cognitionbox.petra.lang.PGraph
import kotlin.reflect.KClass

open class KGraph<X:Any> : PGraph<X>() {
    fun type(klass:KClass<X>){
        super.type(klass.java);
    }
}