package core.procedures.generic

import core.procedures.AFn
import core.scm.Cons
import core.scm.Hashmap
import core.scm.MutableVector
import core.scm.Vector

class Empty : AFn<Any?, Any?>(name = "empty", isPure = true, minArgs = 1, maxArgs = 1) {

    override operator fun invoke(arg: Any?): Any? = when (arg) {
        is List<*>      -> Cons.list<Any>()
        is Set<*>       -> HashSet<Any>()
        is Vector       -> MutableVector()
        is Map<*, *>    -> Hashmap()
        is BooleanArray -> BooleanArray(0)
        is CharArray    -> CharArray(0)
        is ByteArray    -> ByteArray(0)
        is ShortArray   -> ShortArray(0)
        is IntArray     -> IntArray(0)
        is LongArray    -> LongArray(0)
        is DoubleArray  -> DoubleArray(0)
        is FloatArray   -> FloatArray(0)
        is Array<*>     -> arrayOf<Any?>()
        else            -> null
    }
}