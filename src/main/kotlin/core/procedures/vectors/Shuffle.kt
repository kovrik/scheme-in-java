package core.procedures.vectors

import core.exceptions.WrongTypeException
import core.procedures.AFn
import core.scm.MutableVector
import core.scm.Vector
import java.util.Collections
import kotlin.collections.ArrayList
import kotlin.collections.Collection
import kotlin.collections.toTypedArray

class Shuffle : AFn<Collection<*>?, Vector>(name = "shuffle", isPure = true, minArgs = 1, maxArgs = 1) {

    override operator fun invoke(arg: Collection<*>?) = when (arg) {
        is Collection<*> -> MutableVector(ArrayList(arg).apply(Collections::shuffle).toTypedArray())
        else             -> throw WrongTypeException(name, Collection::class.java, arg)
    }
}