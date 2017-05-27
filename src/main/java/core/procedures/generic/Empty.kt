package core.procedures.generic

import core.procedures.AFn
import core.procedures.FnArgsBuilder
import core.scm.Cons
import core.scm.MutableVector
import core.scm.Vector

import java.util.*

class Empty : AFn(FnArgsBuilder().min(1).max(1).build()) {

    override val isPure = true
    override val name = "empty"

    override operator fun invoke(arg: Any?): Any? {
        when (arg) {
            is List<*>   -> return Cons.list<Any>()
            is Set<*>    -> return HashSet<Any>()
            is Vector    -> return MutableVector()
            is Map<*, *> -> return HashMap<Any, Any>()
            else         -> return null
        }
    }
}
