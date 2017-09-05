package core.procedures.generic

import core.exceptions.WrongTypeException
import core.procedures.AFn
import core.scm.Cons
import core.scm.MapEntry
import core.scm.MutableVector
import core.scm.Vector

class Reverse : AFn<Any?, Any?>(name = "reverse", isPure = true, minArgs = 1, maxArgs = 1) {

    override operator fun invoke(arg: Any?): Any? = when (arg) {
        is List<*>         -> Cons.list<Any>().apply { for (o in (arg as List<*>?)!!) { add(0, o) } }
        is Set<*>          -> Cons.list(arg as Collection<Any?>)
        is Map.Entry<*, *> -> MapEntry(arg.value, arg.key)
        is CharSequence    -> StringBuilder((arg as CharSequence?)!!).reverse().toString()
        is CharArray       -> arg.copyOf().apply { reverse() }
        is BooleanArray    -> arg.copyOf().apply { reverse() }
        is ByteArray       -> arg.copyOf().apply { reverse() }
        is ShortArray      -> arg.copyOf().apply { reverse() }
        is IntArray        -> arg.copyOf().apply { reverse() }
        is LongArray       -> arg.copyOf().apply { reverse() }
        is DoubleArray     -> arg.copyOf().apply { reverse() }
        is FloatArray      -> arg.copyOf().apply { reverse() }
        is Array<*>        -> arg.copyOf().apply { reverse() }
        is Vector          -> MutableVector(arg).apply  { array.reverse() }
        else -> throw WrongTypeException(name, "List or Vector or Set or String", arg)
    }
}