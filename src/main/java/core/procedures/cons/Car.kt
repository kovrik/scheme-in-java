package core.procedures.cons

import core.procedures.AFn
import core.scm.Cons
import core.scm.Type

class Car : AFn<Any?, Any?>(name = "car", isPure = true, minArgs = 1, maxArgs = 1,
                            mandatoryArgsTypes = arrayOf<Class<*>>(Type.Pair::class.java)) {

    override operator fun invoke(arg: Any?) = when (arg) {
        is Cons<*>    -> arg.car()
        is Pair<*, *> -> arg.first
        else          -> (arg as List<*>)[0]
    }
}
